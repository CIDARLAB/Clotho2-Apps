/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.modasm;

import algorithms.SRSGeneral;
import datastructures.SRSGraph;
import datastructures.SRSNode;
import datastructures.SRSVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Vector;

/**
 *
 * @author evanappleton
 */
public class SRSMoClo extends SRSGeneral {

    /** Clotho part wrapper for sequence dependent one pot reactions **/
    public ArrayList<SRSGraph> mocloClothoWrapper(ArrayList<Part> goalParts, HashSet<Vector> vectorLibrary, HashSet<String> required, HashSet<String> recommended, HashSet<String> forbidden, ArrayList<Part> partLibrary, boolean modular, HashMap<Integer, Double> efficiencies) {
        try {

            //Designate how many parts can be efficiently ligated in one step
            int max = 0;
            Set<Integer> keySet = efficiencies.keySet();
            for (Integer key : keySet) {
                if (key > max) {
                    max = key;
                }
            }
            _maxNeighbors = max;

            //Create hashMem parameter for createAsmGraph_sgp() call
            HashMap<String, SRSGraph> partHash = partImportClotho(goalParts, partLibrary, required, recommended);
            HashSet<SRSVector> vectorSet = vectorImportClotho(vectorLibrary);

            //Put all parts into hash for mgp algorithm            
            ArrayList<SRSNode> gpsNodes = gpsToNodesClotho(goalParts);

            //Positional scoring of transcriptional units
            HashMap<Integer, HashMap<String, Double>> positionScores = new HashMap<Integer, HashMap<String, Double>>();
            if (modular) {
                ArrayList<ArrayList<String>> TUs = getTranscriptionalUnits(gpsNodes, 1);
                positionScores = getPositionalScoring(TUs);
            }

            //Add single transcriptional units to the required hash
            ArrayList<ArrayList<String>> reqTUs = getSingleTranscriptionalUnits(gpsNodes, 2);
            for (int i = 0; i < reqTUs.size(); i++) {
                required.add(reqTUs.get(i).toString());
            }

            //Run SDS Algorithm for multiple parts
            ArrayList<SRSGraph> optimalGraphs = createAsmGraph_mgp(gpsNodes, required, recommended, forbidden, partHash, positionScores, efficiencies);
            assignOverhangs(optimalGraphs, partHash, vectorSet);

            //Remove transcriptional units from the required set
            for (int j = 0; j < reqTUs.size(); j++) {
                required.remove(reqTUs.get(j).toString());
            }

            return optimalGraphs;
        } catch (Exception E) {
            ArrayList<SRSGraph> blank = new ArrayList<SRSGraph>();
//            E.printStackTrace();
            return blank;
        }
    }

   /**************************************************************************
     * 
     * BASIC OVREHANG ASSIGNMENT ALGORITHMS
     * 
    **************************************************************************/
    
    /** Optimize overhang assignments based on available parts and vectors with overhangs **/
    private void assignOverhangs(ArrayList<SRSGraph> optimalGraphs, HashMap<String, SRSGraph> partHash, HashSet<SRSVector> vectorSet) {

        //Get all nodes in a hash keyed by stage
        HashMap<Integer, ArrayList<SRSNode>> stageHash = getStageStepHash(optimalGraphs);
        _uuidOHHash = new HashMap<String, ArrayList<String>>();
        initialOHAssignment(stageHash);
        optimizedOHAssignment(stageHash, partHash, vectorSet);

    }

    /** Assign all initial implied overhangs from the basic part overhangs **/
    private void initialOHAssignment(HashMap<Integer, ArrayList<SRSNode>> stageHash) {

        System.out.println("INITIAL OVERHANG ASSIGNMENT");
        
        //For all stages, assign adjacent nodes overhangs and record nodes with full overhangs
        Set<Integer> keySet = stageHash.keySet();
        for (Integer stage : keySet) {
            ArrayList<SRSNode> stageNodes = stageHash.get(stage);
            for (int i = 0; i < stageNodes.size(); i++) {

                //Assign adjacent nodes an overhang if the current node has one
                SRSNode node = stageNodes.get(i);
                SRSNode prevNode = new SRSNode();
                SRSNode nextNode = new SRSNode();
                if (i > 0) {
                    prevNode = stageNodes.get(i - 1);
                }
                if (i < stageNodes.size() - 1) {
                    nextNode = stageNodes.get(i + 1);
                }
                if (!node.getLOverhang().isEmpty()) {
                    if (prevNode.getROverhang().isEmpty()) {
                        prevNode.setROverhang(node.getLOverhang());
                    }
                }
                if (!node.getROverhang().isEmpty()) {
                    if (nextNode.getLOverhang().isEmpty()) {
                        nextNode.setLOverhang(node.getROverhang());
                    }
                }
                
                //If a node has two overhangs exiting and has a uuid it exists, so put it in the uuid hash
                if (!node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                    if (node.getUUID() != null) {
                        ArrayList<String> OHs = new ArrayList<String>();
                        OHs.add(node.getLOverhang());
                        OHs.add(node.getROverhang());
                        _uuidOHHash.put(node.getUUID(), OHs);
                    }
                }
            }
        }
    }

    /** Assign overhangs in an optimal way after initial assignment **/
    private void optimizedOHAssignment(HashMap<Integer, ArrayList<SRSNode>> stageHash, HashMap<String, SRSGraph> partHash, HashSet<SRSVector> vectorSet) {

        //Make list of all possible overhangs that can be assigned
        List<String> allOHs = Arrays.asList("A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p".split(","));

        //Search for vectors elligible in this stage
        HashMap<Integer, HashSet<SRSVector>> levelVecs = new HashMap<Integer, HashSet<SRSVector>>();
        for (SRSVector vector : vectorSet) {
            int vecStage = vector.getLevel();
            HashSet<SRSVector> thislevelVecs = levelVecs.get(vecStage);
            thislevelVecs.add(vector);
            levelVecs.put(vecStage, thislevelVecs);
        }

        HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>> typeOHHash = getLibraryPartTypeOHs(partHash);
        HashSet<String> forbiddenOH = new HashSet<String>();

        //For each stage, working backwards from last stage
        Set<Integer> keySet = stageHash.keySet();
        ArrayList<Integer> stages = new ArrayList<Integer>(keySet);
        Collections.sort(stages);
        for (int i = stages.get(stages.size() - 1); i > -1; i--) {

            System.out.println("******************** New stage ***********************");

            ArrayList<SRSNode> stageNodes = stageHash.get(i);

            //Get all available vectors for this stage
            int level = i % 3;
            HashSet<SRSVector> stageVectors = new HashSet<SRSVector>();
            if (levelVecs.containsKey(level)) {
                stageVectors = levelVecs.get(level);
            }

            //Get the set of forbidden overhangs to add in this stage
            forbiddenOH.clear();
            for (SRSNode aNode : stageNodes) {
                String LO = aNode.getLOverhang();
                if (!LO.isEmpty()) {
                    forbiddenOH.add(LO);
                }
                String RO = aNode.getROverhang();
                if (!RO.isEmpty()) {
                    forbiddenOH.add(RO);
                }
            }

            //Assign part types iteratively, first for part types with 2 empty OHs, then empty OH until all OHs are assigned
            
            System.out.println(stageNodes.size() + " stageNodes in stage: " + stageNodes.get(0).getStage());
            System.out.println("<<<<<<<<<<<<<<< Double assignment >>>>>>>>>>>>>>>");
            
            searchOHs(stageNodes, stageVectors, allOHs, forbiddenOH, typeOHHash, 2);
            
            System.out.println("<<<<<<<<<<<<<<< Single assignment >>>>>>>>>>>>>>>");
            
            searchOHs(stageNodes, stageVectors, allOHs, forbiddenOH, typeOHHash, 1);

            //Assign mandatory OHs to level below if there is a level below
            if (i > 0) {
                for (int j = 0; j < stageNodes.size(); j++) {
                    SRSNode node = stageNodes.get(j);
                    if (!node.getNeighbors().isEmpty()) {
                        ArrayList<SRSNode> neighbors = new ArrayList<SRSNode>();
                        neighbors.addAll(node.getNeighbors());

                        //Only look at this node's children i.e. neighbors in the stage below and assign inherited overhangs
                        for (int k = 0; k < neighbors.size(); k++) {
                            if (neighbors.get(k).getStage() != (i - 1)) {
                                neighbors.remove(neighbors.get(k));
                            }
                        }
                        neighbors.get(0).setLOverhang(node.getLOverhang());
                        neighbors.get(neighbors.size() - 1).setROverhang(node.getROverhang());
                    }
                }
            }

            //Add any new vectors created in this stage to the vector set
            vectorSet.addAll(stageVectors);
            levelVecs.put(level, stageVectors);
        }
    }

    /**************************************************************************
     * 
     * OVERHANG SEARCH AND ASSIGNMENT METHODS
     * 
    **************************************************************************/
    
    /** Search for all parts in some set that have two unassigned overhangs and assign them iteratively, optimized for modularity **/
    private void searchOHs(ArrayList<SRSNode> stageNodes, HashSet<SRSVector> stageVectors, List<String> allOHs, HashSet<String> forbiddenOH, HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>> typeOHHash, int num) {

        ArrayList<String> mostSeenType = getMostCommonPartType(stageNodes, num);
        
        while (!mostSeenType.isEmpty()) {

            System.out.println("Most common type: " + mostSeenType.toString());

            //Assign all nodes with most popular type their overhangs  
            for (int i = 0; i < stageNodes.size(); i++) {

                SRSNode node = stageNodes.get(i);
                
                //Double OH assignment
                if (num == 2) {
                    if (node.getType().equals(mostSeenType) && (node.getLOverhang().isEmpty() && node.getROverhang().isEmpty())) {

                        boolean assigned = false;

                        //Try to assign a suitable stageVector first 
                        if (!stageVectors.isEmpty()) {
                            assigned = assignFromStageVectors(node, mostSeenType, forbiddenOH, stageNodes, stageVectors, num);
                            if (assigned) {
                                break;
                            }
                        }
                        
                        //If there are no suitable stageVectors, search existing part types to see if these can be used
                        if (typeOHHash.containsKey(node.getType()) && !assigned) {
                            assigned = assignFromPartTypeHash(node, mostSeenType, forbiddenOH, stageNodes, stageVectors, typeOHHash, num);
                            if (assigned) {
                                break;
                            }
                        }

                        //If there are still no overhangs that can be assigned, remove the forbidden OHs for this stage from the list of available new OHs, make a new vector and parts with new OHs
                        if (!assigned) {
                            assignNew(node, mostSeenType, stageNodes, stageVectors, allOHs, forbiddenOH, num);
                            break;
                        }
                    }
                
                //Single OH assignment
                } else if (num == 1) {
                    if (node.getType().equals(mostSeenType) && ((node.getLOverhang().isEmpty() || node.getROverhang().isEmpty()) && !(node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()))) {

                        boolean assigned = false;

                        HashMap<String, String> mostCommonSingleOH = getMostCommonOHforPartType(stageNodes, mostSeenType);
                        String LO = new String();
                        String RO = new String();
                        
                        if (mostCommonSingleOH.containsKey("L")) {
                            LO = mostCommonSingleOH.get("L");
                        } else if (mostCommonSingleOH.containsKey("R")) {
                            RO = mostCommonSingleOH.get("R");
                        }
                        
                        //If the node has the most common right overhang or left overhang and those are not empty... should never be empty
                        if ((node.getLOverhang().equals(LO) && !LO.isEmpty()) || (node.getROverhang().equals(RO) && !RO.isEmpty())) {

                            //Try to assign a suitable stageVector first 
                            if (!stageVectors.isEmpty()) {
                                assigned = assignFromStageVectors(node, mostSeenType, forbiddenOH, stageNodes, stageVectors, num);
                                if (assigned) {
                                    break;
                                }
                            }

                            //If there are no suitable stageVectors, search existing part types to see if these can be used
                            if (typeOHHash.containsKey(node.getType()) && !assigned) {
                                assigned = assignFromPartTypeHash(node, mostSeenType, forbiddenOH, stageNodes, stageVectors, typeOHHash, num);
                                if (assigned) {
                                    break;
                                }
                            }

                            //If there are still no overhangs that can be assigned, remove the forbidden OHs for this stage from the list of available new OHs, make a new vector and parts with new OHs
                            if (!assigned) {
                                assignNew(node, mostSeenType, stageNodes, stageVectors, allOHs, forbiddenOH, num);
                                break;
                            }
                        }
                    }
                }

            }
            
            //Keep doing this assignment until all part types with two available OHs are assigned
            mostSeenType = getMostCommonPartType(stageNodes, num);
        }
    }

    /** Assign overhangs to all parts of the same type with no overhangs yet assigned in a given list **/
    private void assignAll(ArrayList<SRSNode> stageNodes, ArrayList<String> mostSeen, String LO, String RO, SRSVector vector, int num) {

        SRSNode currentParent = new SRSNode();

        for (int i = 0; i < stageNodes.size(); i++) {
            SRSNode node = stageNodes.get(i);
            
            SRSNode prevNode = new SRSNode();
            SRSNode nextNode = new SRSNode();
            if (i > 0) {
                prevNode = stageNodes.get(i - 1);
            }
            if (i < stageNodes.size() - 1) {
                nextNode = stageNodes.get(i + 1);
            }
            
            if (node.getType().equals(mostSeen)) {

                SRSNode parent = new SRSNode();
                ArrayList<SRSNode> nodeNeighbors = node.getNeighbors();
                for (int j = 0; j < nodeNeighbors.size(); j++) {
                    if (nodeNeighbors.get(j).getStage() == (node.getStage() + 1)) {
                        parent = nodeNeighbors.get(j);
                    }
                }
                
                if (parent != currentParent) {

                    //If both overhangs are not yet assigned, assign values to empty slots only of this node and the ones next to it
                    if (node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()) {
                        node.setLOverhang(LO);
                        node.setROverhang(RO);
                        node.setVector(vector);
                        uuidHashCheck(node);

                        //If assigning adjacent overhang fills both of a node's overhangs, assign the node this vector
                        nextNode.setLOverhang(RO);
                        if (!nextNode.getLOverhang().isEmpty() && !nextNode.getROverhang().isEmpty()) {
                            uuidHashCheck(nextNode);
                            nextNode.setVector(vector);
                        }
                        prevNode.setROverhang(LO);
                        if (!prevNode.getLOverhang().isEmpty() && !prevNode.getROverhang().isEmpty()) {
                            uuidHashCheck(prevNode);
                            prevNode.setVector(vector);
                        }

                        currentParent = parent;

                    //If only the left overhang is empty, only assign the left if RO and the right overhang match
                    } else if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                        if (node.getROverhang().equalsIgnoreCase(RO)) {
                            node.setLOverhang(LO);
                            node.setVector(vector);
                            uuidHashCheck(node);
                            prevNode.setROverhang(LO);

                            //If assigning adjacent overhang fills both of a node's overhangs, assign the node this vector
                            if (!prevNode.getLOverhang().isEmpty() && !prevNode.getROverhang().isEmpty()) {
                                uuidHashCheck(prevNode);
                                prevNode.setVector(vector);
                            }

                            currentParent = parent;
                        }

                    //If only the right overhang is empty, only assign the right if LO and the left overhang match
                    } else if (!node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()) {
                        if (node.getLOverhang().equalsIgnoreCase(LO)) {
                            node.setROverhang(RO);
                            node.setVector(vector);
                            uuidHashCheck(node);
                            nextNode.setLOverhang(RO);

                            //If assigning adjacent overhang fills both of a node's overhangs, assign the node this vector
                            if (!nextNode.getLOverhang().isEmpty() && !nextNode.getROverhang().isEmpty()) {
                                uuidHashCheck(nextNode);
                                nextNode.setVector(vector);
                            }

                            currentParent = parent;
                        }
                    }
                }                       
            }
        }        
    }

    /** Check to see if a node with a uuid already has overhangs and delete the node's uuid if the overhangs do not match **/
    private void uuidHashCheck(SRSNode node) {
        
        ArrayList<String> nodeOHs = new ArrayList<String>();
        nodeOHs.add(node.getLOverhang());
        nodeOHs.add(node.getROverhang());
        Set<String> keySet = _uuidOHHash.keySet();
      
        //If the node has a uuid
        if (node.getUUID() != null) {

            //If the uuid hash contains this node's uuid already
            if (keySet.contains(node.getUUID())) {
                ArrayList<String> hashOHs = _uuidOHHash.get(node.getUUID());

                //If the node OHs and the uuid hash OHs do not match, null the node's uuid, needs to be made into new part
                if (!nodeOHs.equals(hashOHs)) {
                    node.setUUID(null);
                }
                
                
                
            //Otherwise put the node in the uuidOHHash
            } else {
                _uuidOHHash.put(node.getUUID(), nodeOHs);
            }
        }
    }
    
    /**************************************************************************
     * 
     * PART TYPE SEARCH METHODS
     * 
    **************************************************************************/
    
    /** Get all the overhang pairs for each type of part in the partHash **/
    private HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>> getLibraryPartTypeOHs(HashMap<String, SRSGraph> partHash) {

        //For all parts in the hash, see for every type of part, the most common overhang set
        HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>> typeOHCounter = new HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>>();

        //Tally the number of time each overhang combination occurs for 
        Collection<SRSGraph> values = partHash.values();
        for (SRSGraph graph : values) {

            //For each graph in the hash, get the root node overhang information
            SRSNode root = graph.getRootNode();
            HashMap<String, String> OHs = new HashMap<String, String>();
            ArrayList<String> type = root.getType();
            String LO = root.getLOverhang();
            String RO = root.getROverhang();
            OHs.put("L", LO);
            OHs.put("R", RO);

            //If the type was already seen
            if (typeOHCounter.containsKey(type)) {
                HashMap<HashMap<String, String>, Integer> typeOHs = typeOHCounter.get(type);
                if (typeOHs.containsKey(OHs)) {
                    int count = typeOHs.get(OHs);
                    typeOHs.put(OHs, count + 1);
                } else {
                    typeOHs.put(OHs, 1);
                }

            } else {
                HashMap<HashMap<String, String>, Integer> typeOHs = new HashMap<HashMap<String, String>, Integer>();
                typeOHs.put(OHs, 1);
                typeOHCounter.put(type, typeOHs);
            }
        }
        return typeOHCounter;
    }

    /** Find the most common part type with no overhangs yet assigned **/
    private ArrayList<String> getMostCommonPartType(ArrayList<SRSNode> stageNodes, int num) {

        //Find all nodes in a step with unassigned overhangs and record the most seen type
        HashMap<ArrayList<String>, Integer> noOHTypeCount = new HashMap<ArrayList<String>, Integer>();
        HashMap<HashMap<ArrayList<String>, HashMap<String, String>>, Integer> oneOHTypeCount = new HashMap<HashMap<ArrayList<String>, HashMap<String, String>>, Integer>();
        int maxCount = 0;
        ArrayList<String> mostSeen = new ArrayList<String>();
        for (int j = 0; j < stageNodes.size(); j++) {
            SRSNode node = stageNodes.get(j);

            //If looking for parts with no overhangs assigned
            if (num == 2) {
                if (node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()) {
                    if (!noOHTypeCount.containsKey(node.getType())) {
                        int count = 1;
                        if (count > maxCount) {
                            maxCount = count;
                            mostSeen = node.getType();
                        }
                        noOHTypeCount.put(node.getType(), count);
                    } else {
                        int count = noOHTypeCount.get(node.getType()) + 1;
                        if (count > maxCount) {
                            maxCount = count;
                            mostSeen = node.getType();
                        }
                        noOHTypeCount.put(node.getType(), count);
                    }
                }

            //If looking for parts with one overhang already assigned
            } else if (num == 1) {
                if ((node.getLOverhang().isEmpty() || node.getROverhang().isEmpty()) && !(node.getLOverhang().isEmpty() && node.getROverhang().isEmpty())) {
                    
                    //Get the overhangs and part type and put them in a hash
                    HashMap<String, String> OH = new HashMap<String, String>();
                    if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                        OH.put("R", node.getROverhang());
                    } else if (!node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()) {
                        OH.put("L", node.getLOverhang());
                    }                    
                    HashMap<ArrayList<String>, HashMap<String, String>> typeOH = new HashMap<ArrayList<String>, HashMap<String, String>>();
                    typeOH.put(node.getType(), OH);
                    
                    if (!oneOHTypeCount.containsKey(typeOH)) {
                        int count = 1;
                        if (count > maxCount) {
                            maxCount = count;
                            mostSeen = node.getType();
                        }
                        oneOHTypeCount.put(typeOH, count);
                    } else {
                        int count = oneOHTypeCount.get(typeOH) + 1;
                        if (count > maxCount) {
                            maxCount = count;
                            mostSeen = node.getType();
                        }
                        oneOHTypeCount.put(typeOH, count);
                    }
                }
            }
        }
        return mostSeen;
    }
    
    private HashMap<String, String> getMostCommonOHforPartType(ArrayList<SRSNode> stageNodes, ArrayList<String> type) {
        
        HashMap<String, String> mostCommonOH = new HashMap<String, String>();
        HashMap<HashMap<String, String>, Integer> OHs = new HashMap<HashMap<String, String>, Integer>();
        int maxCount = 0;
        
        //For all stageNode of the selected type
        for (int i = 0; i < stageNodes.size(); i++) {
            SRSNode node = stageNodes.get(i);
            if (node.getType().equals(type)) {
                
                //If only the left OH is empty
                if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                    HashMap<String, String> OH = new HashMap<String, String>();
                    OH.put("R", node.getROverhang());
                    
                    int count = 0;
                    if (OHs.containsKey(OH)) {
                        count = OHs.get(OH) + 1;
                        OHs.put(OH, count);                        
                        if (count > maxCount) {
                            maxCount = count;
                            mostCommonOH = OH;
                        }                        
                    } else {
                        count = 1;
                        OHs.put(OH, count);
                        if (count > maxCount) {
                            maxCount = count;
                            mostCommonOH = OH;
                        }
                    }
                
                //If only the right overhang is empty, only assign the right if LO and the left overhang match
                } else if (!node.getLOverhang().isEmpty() && node.getROverhang().isEmpty()) {
                    HashMap<String, String> OH = new HashMap<String, String>();
                    OH.put("L", node.getLOverhang());
                    
                    int count = 0;
                    if (OHs.containsKey(OH)) {
                        count = OHs.get(OH) + 1;
                        OHs.put(OH, count);                        
                        if (count > maxCount) {
                            maxCount = count;
                            mostCommonOH = OH;
                        }                        
                    } else {
                        count = 1;
                        OHs.put(OH, count);
                        if (count > maxCount) {
                            maxCount = count;
                            mostCommonOH = OH;
                        }
                    }
                }
            }
        }
        
        return mostCommonOH;
    }
    
    /**************************************************************************
     * 
     * OVERHANG ASSIGNMENT ASSIGN BASED ON VECTORS, EXISTING PART-OVERHANG PAIRS, OR NEW
     * 
    **************************************************************************/
    
    private boolean assignFromStageVectors(SRSNode node, ArrayList<String> mostSeenType, HashSet<String> forbiddenOH, ArrayList<SRSNode> stageNodes, HashSet<SRSVector> stageVectors, int num) {
        
        boolean assigned = false;
        ArrayList<SRSVector> vectors = new ArrayList<SRSVector>(stageVectors);

        //For all the vectors elligible in this stage
        for (int j = 0; j < vectors.size(); j++) {

            SRSVector vector = vectors.get(j);
            String LO = vector.getLOverhang();
            String RO = vector.getROverhang();

            //Assign the first seen vector and its overhangs to this node and all nodes of the same type if neither overhang is forbidden in this stage and assign that to all nodes of this type
            if (!(forbiddenOH.contains(LO) || forbiddenOH.contains(RO))) {
                
                if (num == 2) {

                    assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                    assigned = true;
                    forbiddenOH.add(LO);
                    forbiddenOH.add(RO);
                    break;
                
                } else if (num == 1) {

                    //If only the left overhang is unassigned
                    if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                        if (node.getROverhang().equalsIgnoreCase(RO)) {
                            assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                            forbiddenOH.add(LO);
                            assigned = true;
                        }

                    //If only the right overhang is unassigned
                    } else if (node.getROverhang().isEmpty() && !node.getLOverhang().isEmpty()) {
                        if (node.getLOverhang().equalsIgnoreCase(LO)) {
                            assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                            forbiddenOH.add(RO);
                            assigned = true;
                        }
                    }
                }
            }
        }
        
        return assigned;
    }

    private boolean assignFromPartTypeHash(SRSNode node, ArrayList<String> mostSeenType, HashSet<String> forbiddenOH, ArrayList<SRSNode> stageNodes, HashSet<SRSVector> stageVectors, HashMap<ArrayList<String>, HashMap<HashMap<String, String>, Integer>> typeOHHash, int num) {

        boolean assigned = false;
        HashMap<HashMap<String, String>, Integer> commonOHs = typeOHHash.get(node.getType());
        Set<HashMap<String, String>> keySet = commonOHs.keySet();

        String LO = new String();
        String RO = new String();

        //Find the most common OH pair from the library
        int maxCount = 0;
        HashMap<String, String> mostSeen = new HashMap<String, String>();
        for (HashMap<String, String> key : keySet) {
            int count = commonOHs.get(key);

            //If this pair is the current most seen
            if (count > maxCount) {
                LO = key.get("L");
                RO = key.get("R");

                //Only record it as the most seen if the overhangs are allowed in this stage and not empty
                if (num == 2) {
                    if (!(forbiddenOH.contains(LO) || forbiddenOH.contains(RO)) && !LO.isEmpty() && !RO.isEmpty()) {
                        maxCount = count;
                        mostSeen = key;
                    }
                } else if (num == 1) {

                    //If only the left overhang is unassigned
                    if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                        if (node.getROverhang().equalsIgnoreCase(RO)) {
                            maxCount = count;
                            mostSeen = key;
                        }

                        //If only the right overhang is unassigned
                    } else if (node.getROverhang().isEmpty() && !node.getLOverhang().isEmpty()) {
                        if (node.getLOverhang().equalsIgnoreCase(LO)) {
                            maxCount = count;
                            mostSeen = key;
                        }
                    }
                }

            }
        }
        if (mostSeen.containsKey("L")) {
            LO = mostSeen.get("L");
        } 
        if (mostSeen.containsKey("R")) {
            RO = mostSeen.get("R");
        }
        
        //Make a new vector and assign overhangs if they aren't empty
        if (!LO.isEmpty() && !RO.isEmpty()) {

            SRSVector vector = new SRSVector();
            vector.setLevel((node.getStage() % 3));
            vector.setLOverhang(LO);
            vector.setROverhang(RO);
            stageVectors.add(vector);

            if (num == 2) {
                forbiddenOH.add(LO);
                forbiddenOH.add(RO);
                assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                assigned = true;
            } else if (num == 1) {

                //If only the left overhang is unassigned
                if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                    if (node.getROverhang().equalsIgnoreCase(RO)) {
                        assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                        forbiddenOH.add(LO);
                        assigned = true;
                    }

                    //If only the right overhang is unassigned
                } else if (node.getROverhang().isEmpty() && !node.getLOverhang().isEmpty()) {
                    if (node.getLOverhang().equalsIgnoreCase(LO)) {
                        assignAll(stageNodes, mostSeenType, LO, RO, vector, num);
                        forbiddenOH.add(RO);
                        assigned = true;
                    }
                }
            }
        }

        return assigned;
    }

    private void assignNew(SRSNode node, ArrayList<String> mostSeenType, ArrayList<SRSNode> stageNodes, HashSet<SRSVector> stageVectors, List<String> allOHs, HashSet<String> forbiddenOH, int num) {
        
        ArrayList<String> allowedNewOH = new ArrayList<String>();
        allowedNewOH.addAll(allOHs);
        allowedNewOH.removeAll(forbiddenOH);
        
        if (num == 2) {
     
            String newLO = allowedNewOH.get(0);
            String newRO = allowedNewOH.get(1);

            //Make a new vector
            SRSVector vector = new SRSVector();
            vector.setLevel((node.getStage() % 3));
            vector.setResistance((node.getStage() % 3));
            vector.setLOverhang(newLO);
            vector.setROverhang(newRO);
            stageVectors.add(vector);
            forbiddenOH.add(newLO);
            forbiddenOH.add(newRO);
            assignAll(stageNodes, mostSeenType, newLO, newRO, vector, num);
            
        } else if (num == 1) {

            String newOH = allowedNewOH.get(0);

            //If only the left overhang is unassigned
            if (node.getLOverhang().isEmpty() && !node.getROverhang().isEmpty()) {
                
                //Make a new vector
                SRSVector vector = new SRSVector();
                vector.setLevel((node.getStage() % 3));
                vector.setResistance((node.getStage() % 3));
                vector.setLOverhang(newOH);
                vector.setROverhang(node.getROverhang());
                stageVectors.add(vector);
                assignAll(stageNodes, mostSeenType, newOH, node.getROverhang(), vector, num);
                forbiddenOH.add(newOH);

            //If only the right overhang is unassigned
            } else if (node.getROverhang().isEmpty() && !node.getLOverhang().isEmpty()) {
                
                //Make a new vector
                SRSVector vector = new SRSVector();
                vector.setLevel((node.getStage() % 3));
                vector.setResistance((node.getStage() % 3));
                vector.setLOverhang(node.getLOverhang());
                vector.setROverhang(newOH);
                stageVectors.add(vector);
                assignAll(stageNodes, mostSeenType, node.getLOverhang(), newOH, vector, num);
                forbiddenOH.add(newOH);
            }
        }
    }   
    
    //Fields
    private HashMap<String, ArrayList<String>> _uuidOHHash;
}