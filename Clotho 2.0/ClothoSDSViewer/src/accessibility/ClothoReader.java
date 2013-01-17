/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accessibility;

import java.util.ArrayList;
import java.util.HashSet;
import datastructures.SRSGraph;
import datastructures.SRSNode;
import datastructures.SRSVector;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Person;
import org.clothocore.api.data.Vector;

/**
 * Provides utility methods for interpreting Clotho composite parts
 *
 * @author Tao
 */
public class ClothoReader {

    /**
     * Clotho reader constructor *
     */
    public ClothoReader() {
        _allCompositeParts = new ArrayList<Part>();
        _allBasicParts = new ArrayList<Part>();
    }

    /**
     * Generate Clotho parts with uuids from intermediates without uuids *
     */
    public void nodesToClothoPartsVectors(SRSGraph graph) throws Exception {
        String nameRoot = Collector.getPart(graph.getRootNode().getUUID()).getName();
        ArrayList<SRSNode> queue = new ArrayList<SRSNode>();
        HashSet<SRSNode> seenNodes = new HashSet<SRSNode>();
        queue.add(graph.getRootNode());
        while (!queue.isEmpty()) {
            SRSNode current = queue.get(0);
            seenNodes.add(current);
            queue.remove(0);

            for (SRSNode neighbor : current.getNeighbors()) {
                if (!seenNodes.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }

            //If the node has no uuid
            System.out.println("current uuid: " + current.getUUID());
            if (current.getUUID() == null) {
                System.out.println("assigning new uuid");
                //Get new intermediate name
                String partName = nameRoot + "_intermediate" + Math.random() * 999999999;
                if (partName.length() > 255) {
                    partName = partName.substring(0, 255);
                }

                //Get new intermediate composition
                ArrayList<String> UUIDcomposition = new ArrayList();
                for (String s : current.getComposition()) {
                    UUIDcomposition.add(Part.retrieveByExactName(s).getUUID());
                }

                //Get new intermediate overhangs
                String LO = current.getLOverhang();
                String RO = current.getROverhang();

                //If there's overhangs, add search tags
                Part newPart = generateNewClothoPart(partName, "", UUIDcomposition, LO, RO);
                current.setName(partName);
                newPart.saveDefault();
                System.out.println("setting new uuid for " + current.getComposition() + " uuid: " + newPart.getUUID());
                current.setUUID(newPart.getUUID());

            }
            seenNodes.add(current);

            //Get the vector and save a new vector if it does not have a uuid
            SRSVector vector = current.getVector();
            if (vector != null) {
                if (vector.getUUID() == null) {

                    //Get new intermediate name
                    String vecName = nameRoot + "_vector" + Math.random() * 999999999;
                    if (vecName.length() > 255) {
                        vecName = vecName.substring(0, 255);
                    }

                    //Get vector overhangs
                    String LO = vector.getLOverhang();
                    String RO = vector.getROverhang();
                    String resistance = vector.getResistance();
                    int level = vector.getLevel();

                    Vector newVector = generateNewClothoVector(vecName, "", "", "", "", -1);
//                    Vector newVector = generateNewClothoVector(vecName, "", LO, RO, resistance, level);
                    vector.setName(vecName);
                    vector.setUUID(newVector.getUUID());
                    newVector.saveDefault();
                }
            }
        }
        for (SRSNode node : seenNodes) {
            System.out.println("uuid: " + node.getUUID());
            System.out.println("composition: " + node.getComposition());
        }
    }

    /**
     * Make intermediate parts of a graph into Clotho parts (typically only done
     * for solution graphs) *
     */
    private Part generateNewClothoPart(String name, String description, ArrayList<String> UUIDcomposition, String LO, String RO) {
        if (_allCompositeParts == null || _allBasicParts == null) {
            refreshPartVectorList();
        }
        ArrayList<String> inputPartUUIDComp = new ArrayList<String>();

        //For each composite part, get the basic part uuids
        for (String uuid : UUIDcomposition) {
            Part inputPart = Collector.getPart(uuid);
            if (inputPart.getPartType().equals(Part.partType.Composite)) {
                try {
                    ArrayList<Part> inputPartComposition = getComposition(inputPart);
                    for (Part basicPart : inputPartComposition) {
                        inputPartUUIDComp.add(basicPart.getUUID());
                    }
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            } else {
                inputPartUUIDComp.add(uuid);
            }
        }

        //Every time a new composite part can be made, search to see there's nothing made from the same components before saving
        for (Part existingPart : _allCompositeParts) {
            ArrayList<String> existingPartUUIDComp = new ArrayList<String>();

            //Get an existing part's overhangs
            ArrayList<String> sTags = existingPart.getSearchTags();
            String existingPartLO = new String();
            String existingPartRO = new String();
            for (int k = 0; k < sTags.size(); k++) {
                if (sTags.get(k).startsWith("LO:")) {
                    existingPartLO = sTags.get(k).substring(4);
                } else if (sTags.get(k).startsWith("RO:")) {
                    existingPartRO = sTags.get(k).substring(4);
                }
            }

            //Obtain the basic part uuids
            try {
                ArrayList<Part> existingPartComposition = getComposition(existingPart);
                for (Part basicPart : existingPartComposition) {
                    existingPartUUIDComp.add(basicPart.getUUID());
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }

            //If the number of uuids is the same as the number of input composition uuids and the number of uuids in the composition of somePart and the overhangs match, return the part
            if (inputPartUUIDComp.equals(existingPartUUIDComp)) {
                if (existingPartLO.equalsIgnoreCase(LO) && existingPartRO.equalsIgnoreCase(RO)) {
                    return existingPart;
                }
            }
        }

        Format form = Format.retrieveByName("freeform");
        Person per = Collector.getCurrentUser();

        //If a new composite part needs to be made
        if (inputPartUUIDComp.size() > 1) {
            ArrayList<Part> newComposition = new ArrayList<Part>();
            for (String uuid : inputPartUUIDComp) {
                newComposition.add(Collector.getPart(uuid));
            }
            Part newPart = Part.generateComposite(newComposition, null, form, per, name, description);
            if (!LO.isEmpty()) {
                newPart.addSearchTag("LO: " + LO);
            }
            if (!RO.isEmpty()) {
                newPart.addSearchTag("RO: " + RO);
            }
            return newPart;

            //Make a new basic part
        } else {
            Part newPart = Part.generateBasic(name, description, Collector.getPart(inputPartUUIDComp.get(0)).getSeq().toString(), form, per);
            if (!LO.isEmpty()) {
                newPart.addSearchTag("LO: " + LO);
            }
            if (!RO.isEmpty()) {
                newPart.addSearchTag("RO: " + RO);
            }
            newPart.saveDefault();
            return newPart;
        }

    }

    /**
     * Make intermediate parts of a graph into Clotho parts (typically only done
     * for solution graphs) *
     */
    private Vector generateNewClothoVector(String name, String desc, String LO, String RO, String resistance, int level) {
        if (_allVectors == null) {
            refreshPartVectorList();
        }

        //Search all existing vectors to for vectors with same overhangs and level before saving
        for (Vector vector : _allVectors) {

            //Get an existing part's overhangs
            ArrayList<String> sTags = vector.getSearchTags();
            String existingVecLO = new String();
            String existingVecRO = new String();
            String existResistance = new String();
            int existLevel = -1;
            for (int i = 0; i < sTags.size(); i++) {
                if (sTags.get(i).startsWith("LO:")) {
                    existingVecLO = sTags.get(i).substring(4);
                } else if (sTags.get(i).startsWith("RO:")) {
                    existingVecRO = sTags.get(i).substring(4);
                } else if (sTags.get(i).startsWith("Level:")) {
                    String aLevel = sTags.get(i).substring(7);
                    level = Integer.parseInt(aLevel);
                } else if (sTags.get(i).startsWith("Resistance:")) {
                    resistance = sTags.get(i).substring(12);
                }
            }

            //If all of these things match, just return the vector that is found
            if (existingVecLO.equalsIgnoreCase(LO) && existingVecRO.equalsIgnoreCase(RO)) {
                if (existResistance.equalsIgnoreCase(resistance) && existLevel == level) {
                    return vector;
                }
            }
        }

        Format form = Format.retrieveByName("freeform");
        Person per = Collector.getCurrentUser();
        Vector newVector = Vector.generateVector(name, desc, "", form, per);
        if (!LO.isEmpty()) {
            newVector.addSearchTag("LO: " + LO);
        }
        if (!RO.isEmpty()) {
            newVector.addSearchTag("RO: " + RO);
        }
        if (!resistance.isEmpty()) {
            newVector.addSearchTag("Resistance: " + resistance);
        }
        if (level > -1) {
            newVector.addSearchTag("Level: " + level);
        }
        return newVector;
    }

    /**
     * Refresh a part list (used by the viewer) *
     */
    private static void refreshPartVectorList() {
        _allCompositeParts = new ArrayList<Part>();
        _allBasicParts = new ArrayList<Part>();
        _allVectors = new ArrayList<Vector>();
        ArrayList<Vector> allVectors = Collector.getAll(ObjType.VECTOR);
        _allVectors.addAll(allVectors);
        ArrayList<Part> allParts = Collector.getAll(ObjType.PART);
        for (Part somePart : allParts) {
            if (somePart.getPartType().equals(Part.partType.Composite)) {
                _allCompositeParts.add(somePart);
            } else if (somePart.getPartType().equals(Part.partType.Basic)) {
                _allBasicParts.add(somePart);
            }
        }
    }

    /**
     * Return the composition of a Clotho part *
     */
    public static ArrayList<Part> getComposition(org.clothocore.api.data.Part part) throws Exception {
        ArrayList<Part> toReturn = new ArrayList<Part>();
        if (part.getPartType().equals(Part.partType.Basic)) {
            toReturn.add(part);
        } else {
            ArrayList<org.clothocore.api.data.Part> composition = part.getCompositeParts();
            for (int i = 0; i < composition.size(); i++) {
                Part currentPart = composition.get(i);
                if (currentPart.getPartType().equals(Part.partType.Basic)) {
                    toReturn.add(currentPart);
                } else {
                    toReturn = getCompositionHelper(currentPart, toReturn);
                }
            }
        }
        return toReturn;
    }

    /**
     * Helper for recursion method to discover all basic parts *
     */
    private static ArrayList<Part> getCompositionHelper(org.clothocore.api.data.Part somePart, ArrayList<Part> partsList) throws Exception {
        ArrayList<Part> toReturn = partsList;
        Part compositePart = somePart;
        ArrayList<org.clothocore.api.data.Part> composition = compositePart.getCompositeParts();
        for (int i = 0; i < composition.size(); i++) {
            Part currentPart = composition.get(i);
            if (currentPart.getPartType().equals(Part.partType.Basic)) {
                toReturn.add(currentPart);
            } else {
                toReturn = getCompositionHelper(currentPart, toReturn);
            }
        }
        return toReturn;
    }
    //Fields
    static ArrayList<Part> _allCompositeParts;
    static ArrayList<Part> _allBasicParts;
    static ArrayList<Vector> _allVectors;
}