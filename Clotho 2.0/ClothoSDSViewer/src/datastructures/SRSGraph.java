/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Stack;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Part;

/**
 *
 * @author evanappleton
 */
public class SRSGraph {

    /**
     * SDSGraph constructor, no specified root node *
     */
    public SRSGraph() {
        _node = new SRSNode();
        _subGraphs = new ArrayList<SRSGraph>();
        _stages = 0;
        _steps = 0;
        _recCnt = 0;
        _sharing = 0;
        _modularity = 0;
        _efficiency = new ArrayList<Double>();
    }

    /**
     * SDSGraph constructor, specified root node *
     */
    public SRSGraph(SRSNode node) {
        _node = node;
        _subGraphs = new ArrayList<SRSGraph>();
        _stages = 0;
        _steps = 0;
        _recCnt = 0;
        _sharing = 0;
        _modularity = 0;
        _efficiency = new ArrayList<Double>();
    }

    /**
     * Clone method for an SDSGraph *
     */
    @Override
    public SRSGraph clone() {
        SRSGraph clone = new SRSGraph();
        clone._node = this._node.clone();
        clone._recCnt = this._recCnt;
        clone._stages = this._stages;
        clone._steps = this._steps;
        clone._sharing = this._sharing;
        clone._modularity = this._modularity;
        clone._efficiency = this._efficiency;
        return clone;
    }

    /**
     * Pin a graph - pin and set steps to 0 *
     */
    public void pin() {
        this._pinned = true;
        this._steps = 0;
    }

    /**
     * ************************************************************************
     *
     * GRAPH EXPORT METHODS
     *
     *************************************************************************
     */
    /**
     * Get all the edges of an SDSGraph in Post Order *
     */
    public ArrayList<String> getPostOrderEdges() {
        System.out.println("**********START getPostOrderEdges*****************");
        ArrayList<String> edges = new ArrayList();
        HashSet<String> seenCompositions = new HashSet();

        //Start at the root node and look at all children
        for (SRSNode neighbor : this._node.getNeighbors()) {
            seenCompositions.add(neighbor.getComposition().toString());
            edges = getPostOrderEdgesHelper(neighbor, this._node, edges, seenCompositions, true);
        }
        System.out.println("************END getPostOrderEdges*****************");
        return edges;
    }

    /**
     * Return graph edges in an order specified for puppetshow *
     */
    private ArrayList<String> getPostOrderEdgesHelper(SRSNode current, SRSNode parent, ArrayList<String> edges, HashSet<String> seenCompositions, boolean recurse) {
        ArrayList<String> edgesToAdd = new ArrayList();

        //Do a recursive call if there are unseen neighbors
        if (recurse) {

            //For all of this node's neighbors
            for (SRSNode neighbor : current.getNeighbors()) {

                //If the neighbor's composition is not that of the parent
                if (!parent.getComposition().toString().equals(neighbor.getComposition().toString())) {

                    //If this neighbor's composition hasn't been seen before, add it to the seen composition list and do a recursive call of this node, this time as this node being the parent
                    if (!seenCompositions.contains(neighbor.getComposition().toString())) {
                        seenCompositions.add(neighbor.getComposition().toString());
                        edges = getPostOrderEdgesHelper(neighbor, current, edges, seenCompositions, true);

                        //If this neighbor has been seen, do not recursively call
                    } else {
                        edges = getPostOrderEdgesHelper(neighbor, current, edges, seenCompositions, false);
                    }
                }
            }
        }

        //For all current neighbors... this is always done on any call
        for (SRSNode neighbor : current.getNeighbors()) {

            //Write arc connecting to the parent
            if (neighbor.getComposition().toString().equals(parent.getComposition().toString())) {
//                System.out.println("current: " + current.getName() + " neighbor: " + neighbor.getName());
//                System.out.println("current: " + current.getComposition() + " neighbor: " + neighbor.getComposition());
//                System.out.println("current: " + current.getUUID() + " neighbor: " + neighbor.getUUID());
//                //Make the edge going in the direction of the node with the greatest composition, whether this is parent or child
                if (current.getComposition().size() > neighbor.getComposition().size()) {
                    edgesToAdd.add(current.getUUID() + " -> " + neighbor.getUUID());
                } else if (current.getComposition().size() < neighbor.getComposition().size()) {
                    edgesToAdd.add(neighbor.getUUID() + " -> " + current.getUUID());
                }
            }
        }

        for (String s : edgesToAdd) {
            edges.add(s);
        }
        return edges;
    }

    /**
     * Print edges arc file *
     */
    public String printArcsFile(ArrayList<String> edges) {

        //Build String for export
        //Header
        StringBuilder arcsText = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyy@HHmm");
        Date date = new Date();
        arcsText.append("# AssemblyMethod: BioBrick\n# ").append(Collector.getCurrentUser().toString()).append(" ").append(dateFormat.format(date)).append(" ").append(Collector.getDefaultConnection().toString()).append("\n");
        arcsText.append("# ").append(Collector.getPart(this._node.getUUID())).append("\n");
        arcsText.append("# ").append(this._node.getUUID()).append("\n\n");

        //Build arc file 
        HashMap<String, String> nodeMap = new HashMap<String, String>();//key is uuid, value is name
        for (String s : edges) {
            String[] tokens = s.split("->");
            System.out.println("vertex1: " + tokens[0].trim());
            System.out.println("vertex2: " + tokens[1].trim());
            Part vertex1 = Collector.getPart(tokens[0].trim());
            Part vertex2 = Collector.getPart(tokens[1].trim());
            nodeMap.put(vertex1.getUUID(), vertex1.getName());
            nodeMap.put(vertex2.getUUID(), vertex2.getName());
            arcsText.append("# ").append(vertex1.getName()).append(" -> ").append(vertex2.getName()).append("\n");
            arcsText.append(s).append("\n");
        }

        //Build key
        Stack<SRSNode> stack = new Stack<SRSNode>();
        HashSet<SRSNode> seenNodes = new HashSet<SRSNode>();
        HashMap<String, String> compositionHash = new HashMap<String, String>();
        stack.add(this._node);
        while (!stack.isEmpty()) {
            SRSNode current = stack.pop();
            seenNodes.add(current);
            compositionHash.put(current.getUUID(), current.getComposition().toString());
            for (SRSNode neighbor : current.getNeighbors()) {
                if (!seenNodes.contains(neighbor)) {
                    stack.add(neighbor);
                }
            }
        }
        arcsText.append("\n# Key\n");
        for (String key : nodeMap.keySet()) {
            arcsText.append("# ").append(nodeMap.get(key)).append("\n");
            arcsText.append("# ").append(key).append("\n");
            String compositionString = compositionHash.get(key);
            arcsText.append("# (").append(compositionString.substring(1, compositionString.length() - 1)).append(")\n");
        }
        return arcsText.toString();
    }

    /**
     * Generate a Weyekin image file for a list of edges *
     */
    public String generateWeyekinFile(ArrayList<String> edges, boolean pigeon) {

        //Initiate weyekin file
        StringBuilder weyekinText = new StringBuilder();
        HashMap<String, String> nodeMap = new HashMap<String, String>();//key is uuid, value is name
        weyekinText.append("digraph {\n");

        //If edges are empty (happens when no assembly is necessary)
        if (edges.isEmpty()) {
            nodeMap.put(this.getRootNode().getUUID(), this.getRootNode().getName());
        }

        //Grab composition 
        Stack<SRSNode> stack = new Stack<SRSNode>();
        HashSet<SRSNode> seenNodes = new HashSet<SRSNode>();
        HashMap<String, ArrayList<String>> compositionHash = new HashMap<String, ArrayList<String>>();//key is uuid, value is composition
        HashMap<String, ArrayList<String>> typeHash = new HashMap<String, ArrayList<String>>();//key is uuid, value is type
        HashMap<String, ArrayList<String>> OHHash = new HashMap<String, ArrayList<String>>();//key is uuid, value is arraylist with overhangs

        stack.add(this._node);
        while (!stack.isEmpty()) {
            SRSNode current = stack.pop();
            seenNodes.add(current);
            typeHash.put(current.getUUID(), current.getType());
            ArrayList<String> composition = new ArrayList<String>();
            composition.addAll(current.getComposition());
            composition.add(current.getLOverhang());
            composition.add(current.getROverhang());
            if (!compositionHash.containsKey(current.getUUID())) {
                compositionHash.put(current.getUUID(), composition);
            }
            ArrayList<String> overhangs = new ArrayList<String>();
            overhangs.add(current.getLOverhang());
            overhangs.add(current.getROverhang());
            OHHash.put(current.getUUID(), overhangs);

            //Add all unvisited neighbors... even if neighbors are duplicates
            for (SRSNode neighbor : current.getNeighbors()) {
                if (!seenNodes.contains(neighbor)) {
                    stack.add(neighbor);
                }
            }
        }

        //Store list of edges
        String edgeLines = "";
        for (String s : edges) {
            String[] tokens = s.split("->");
            Part vertex1 = Collector.getPart(tokens[0].trim());
            Part vertex2 = Collector.getPart(tokens[1].trim());
            nodeMap.put(vertex1.getUUID(), vertex1.getName());
            nodeMap.put(vertex2.getUUID(), vertex2.getName());
            edgeLines = edgeLines + "\"" + compositionHash.get(vertex2.getUUID()).toString() + "\"" + " -> " + "\"" + compositionHash.get(vertex1.getUUID()).toString() + "\"" + "\n";

        }

        //If nodes will be written in pigeon code
        if (pigeon) {
            for (String key : nodeMap.keySet()) {
                System.out.println("nodeMap key composition: " + compositionHash.get(key).toString());
                StringBuilder pigeonLine = new StringBuilder();
                pigeonLine.append("PIGEON_START\n");
                pigeonLine.append("\"").append(compositionHash.get(key).toString()).append("\"\n");

                //Assign left overhang if it exists
                ArrayList<String> thisNodeOHs = OHHash.get(key);
                pigeonLine.append("o ").append(thisNodeOHs.get(0)).append(" 1" + "\n");

                ArrayList<String> thisNodeComp = compositionHash.get(key);
                ArrayList<String> thisNodeType = typeHash.get(key);
                for (int i = 0; i < thisNodeType.size(); i++) {
                    if (thisNodeType.get(i).equalsIgnoreCase("promoter") || thisNodeType.get(i).equalsIgnoreCase("p")) {
                        pigeonLine.append("P ").append(thisNodeComp.get(i)).append(" 4" + "\n");
                    } else if (thisNodeType.get(i).equalsIgnoreCase("RBS") || thisNodeType.get(i).equalsIgnoreCase("r")) {
                        pigeonLine.append("r ").append(thisNodeComp.get(i)).append(" 5" + "\n");
                    } else if (thisNodeType.get(i).equalsIgnoreCase("gene") || thisNodeType.get(i).equalsIgnoreCase("g")) {
                        pigeonLine.append("c ").append(thisNodeComp.get(i)).append(" 2" + "\n");
                    } else if (thisNodeType.get(i).equalsIgnoreCase("reporter") || thisNodeType.get(i).equalsIgnoreCase("gr")) {
                        pigeonLine.append("c ").append(thisNodeComp.get(i)).append(" 10" + "\n");
                    } else if (thisNodeType.get(i).equalsIgnoreCase("terminator") || thisNodeType.get(i).equalsIgnoreCase("t")) {
                        pigeonLine.append("T ").append(thisNodeComp.get(i)).append(" 6" + "\n");
                    } else {
                        pigeonLine.append(key);
                    }
                }

                //Assign right overhang
                pigeonLine.append("o ").append(thisNodeOHs.get(1)).append(" 1" + "\n");

                pigeonLine.append("# Arcs\n");
                pigeonLine.append("PIGEON_END\n\n");
                weyekinText.append(pigeonLine.toString());
            }
        } else {

            //Write node properties - purple boxes if cannot pigeon
            for (String key : nodeMap.keySet()) {
                weyekinText.append("\"").append(compositionHash.get(key).toString()).append("\"" + " [shape=box, color=\"#B293C9\", style=\"filled,rounded\"]" + "\n");
            }
        }

        //Write edge lines
        weyekinText.append(edgeLines);
        weyekinText.append("}");
        return weyekinText.toString();
    }

    /**
     * Merge multiple arc files into one file with one graph *
     */
    public static String mergeArcFiles(ArrayList<String> inputFiles) {
        String outFile = "";
        String header = "";

        //Grab the header from the first file; first two lines of header should be the same for all of the files
        String[] firstFileLines = inputFiles.get(0).split("\n"); //should split file into separate lines
        for (int i = 0; i < 2; i++) {
            header = header + firstFileLines[i] + "\n";
        }
        ArrayList<String> keyLines = new ArrayList<String>(); //stores the lines in all of the keys

        //Iterate through each arc file; each one is represented by a string
        for (String inputFile : inputFiles) {
            String[] lines = inputFile.split("\n"); //should split file into separate lines
            boolean seenKey = false;

            //Apend to the header
            for (int j = 2; j < 4; j++) {
                header = header + lines[j] + "\n";
            }

            //Apend to the key section
            for (int k = 4; k < lines.length; k++) {//first 4 lines are the header
                if (lines[k].contains("# Key")) {

                    //Once this line appears, store the following lines (which are lines of the key) into the keyLines arrayList.
                    seenKey = true;
                }
                if (seenKey) {

                    //If the key file doesnt have the current line in the current key, add it
                    if (!keyLines.contains(lines[k])) {
                        keyLines.add(lines[k]);
                    }
                } else {

                    //If the line isn't an empty line
                    if (lines[k].length() > 0) {
                        outFile = outFile + lines[k] + "\n";
                    }
                }
            }
        }

        //Apend key to toReturn
        outFile = outFile + "\n";
        for (int l = 0; l < keyLines.size(); l++) {
            outFile = outFile + keyLines.get(l) + "\n";
        }

        //Add header to toReturn
        outFile = header + "\n" + outFile;
        return outFile;
    }

    /**
     * Merge multiple graphviz files into one file with one graph *
     */
    public static String mergeWeyekinFiles(ArrayList<String> filesToMerge) {

        //Repeated edges should only appear in the same graph; an edge that appears in one graph should not appear in another
        String mergedFile = "";
        HashSet<String> seenLines = new HashSet<String>();
        HashSet<ArrayList<String>> seenEdges = new HashSet<ArrayList<String>>();
        ArrayList<String> edgeList = new ArrayList<String>();

        //For each file to merge
        for (String graphFile : filesToMerge) {
            String[] fileLines = graphFile.split("\n");
            HashSet<String> currentSeenLines = new HashSet<String>();
            boolean keepGoing = false;
            boolean lookAtNext = false;

            //For all the lines in each file
            for (int i = 1; i < fileLines.length - 1; i++) {

                //If this is the line directly after PIGEON_START
                if (lookAtNext) {

                    //If the line PIGEON_END is reached, include it and go on to larger if statement in next iteration
                    if (fileLines[i].equalsIgnoreCase("PIGEON_END")) {
                        if (keepGoing) {
                            mergedFile = mergedFile + "PIGEON_END\n\n";
                        }
                        lookAtNext = false;
                        keepGoing = false;
                        continue;
                    }

                    //If the name of the pigeon node hasn't been seen before, save it and all other lines until PIGEON_END
                    if (keepGoing) {
                        mergedFile = mergedFile + fileLines[i] + "\n";
                    } else if (!seenLines.contains(fileLines[i])) {
                        mergedFile = mergedFile + "PIGEON_START\n" + fileLines[i] + "\n";
                        keepGoing = true;
                    }

                    //All things needed multiple times, never stop them from being added to merged file
                } else if (!(fileLines[i].equalsIgnoreCase("digraph {") || fileLines[i].equalsIgnoreCase("}") || fileLines[i].equalsIgnoreCase("\n"))) {

                    //If the line hasn't been seen before
                    if (!seenLines.contains(fileLines[i])) {

                        //If there appears to be an edge
                        if (fileLines[i].contains("->") || fileLines[i].contains("<-")) {
                            ArrayList<String> nodePair = new ArrayList<String>();
                            String[] twoNodesA = fileLines[i].split(" <- ");
                            String[] twoNodesB = fileLines[i].split(" -> ");
                            if (twoNodesA.length == 2) {
                                nodePair.add(twoNodesA[0]);
                                nodePair.add(twoNodesA[1]);
                            } else if (twoNodesB.length == 2) {
                                nodePair.add(twoNodesB[0]);
                                nodePair.add(twoNodesB[1]);
                            }

                            //Search to see if a pair with these edges exists in another file in the merge in reverse order
                            boolean seenPair = false;
                            for (ArrayList<String> edges : seenEdges) {
                                if (edges.contains(nodePair.get(0)) && edges.contains(nodePair.get(1))) {
                                    seenPair = true;
                                }
                            }

                            //If the pair has already been seen
                            if (!seenPair) {
                                seenEdges.add(nodePair);
                                edgeList.add(fileLines[i]);
                            }
                        } else {

                            //Append this line to the merged file
                            mergedFile = mergedFile + fileLines[i] + "\n";
                        }

                        //Add this line to the lines seen in this file
                        currentSeenLines.add(fileLines[i]);
                    } else if (fileLines[i].equalsIgnoreCase("PIGEON_START")) {
                        lookAtNext = true;
                    }
                }
            }

            Iterator<String> iterator = currentSeenLines.iterator();
            while (iterator.hasNext()) {
                seenLines.add(iterator.next());
            }
        }

        for (String edge : edgeList) {
            mergedFile = mergedFile + edge + "\n";
        }
        mergedFile = "digraph{\n" + mergedFile + "\n}";

        return mergedFile;
    }

    /**
     * Check a graph to see if all of its basic parts *
     */
    public boolean canPigeon() {
        boolean canPigeon = true;
        SRSNode root = this.getRootNode();
        ArrayList<String> types = root.getType();
        for (int i = 0; i < types.size(); i++) {
            if (!(types.get(i).equalsIgnoreCase("promoter") || types.get(i).equalsIgnoreCase("p") || types.get(i).equalsIgnoreCase("RBS") || types.get(i).equalsIgnoreCase("r") || types.get(i).equalsIgnoreCase("gene") || types.get(i).equalsIgnoreCase("g") || types.get(i).equalsIgnoreCase("terminator") || types.get(i).equalsIgnoreCase("t") || types.get(i).equalsIgnoreCase("reporter") || types.get(i).equalsIgnoreCase("r"))) {
                canPigeon = false;
            }
        }
        return canPigeon;
    }

    /**
     * ************************************************************************
     *
     * GETTER AND SETTER METHODS
     *
     *************************************************************************
     */
    /**
     * Add a subgraph to a graph *
     */
    public void addSubgraph(SRSGraph graph) {
        _subGraphs.add(graph);
    }

    /**
     * Get graph root node *
     */
    public SRSNode getRootNode() {
        return _node;
    }

    /**
     * Find how many stages for a given SDSGraph *
     */
    public int getStages() {
        return _stages;
    }

    /**
     * Find how many steps for a given SDSGraph *
     */
    public int getSteps() {
        return _steps;
    }

    /**
     * Find how many recommended intermediates for a given SDSGraph *
     */
    public int getReccomendedCount() {
        return _recCnt;
    }

    /**
     * Determine if the graph in question is pinned *
     */
    public boolean getPinned() {
        return _pinned;
    }

    /**
     * Find sharing score for a given SDSGraph *
     */
    public int getSharing() {
        return _sharing;
    }

    /**
     * Get all subgraphs of this graph *
     */
    public ArrayList<SRSGraph> getSubGraphs() {
        return _subGraphs;
    }

    /**
     * Get the modularity score of a graph *
     */
    public double getModularity() {
        return _modularity;
    }

    /**
     * Get the efficiency score of a graph *
     */
    public ArrayList<Double> getEfficiency() {
        return _efficiency;
    }

    /**
     * Set the number of stages for an SDSGraph *
     */
    public void setStages(int stages) {
        _stages = stages;
    }

    /**
     * Set the number of steps for an SDSGraph *
     */
    public void setSteps(int steps) {
        _steps = steps;
    }

    /**
     * Set the number of recommended intermediates for an SDSGraph *
     */
    public void setReccomendedCount(int count) {
        _recCnt = count;
    }

    /**
     * Set graph root node *
     */
    public void setRootNode(SRSNode newRoot) {
        _node = newRoot;
    }

    /**
     * Find sharing score for a given SDSGraph *
     */
    public void setSharing(int sharing) {
        _sharing = sharing;
    }

    /**
     * Get all subgraphs of this graph *
     */
    public void setSubGraphs(ArrayList<SRSGraph> subGraphs) {
        _subGraphs = subGraphs;
    }

    /**
     * Set modularity score *
     */
    public void setModularity(double modularity) {
        _modularity = modularity;
    }

    /**
     * Set the efficiency score of a graph *
     */
    public void setEfficiency(ArrayList<Double> efficiency) {
        _efficiency = efficiency;
    }
    //FIELDS
    private ArrayList<SRSGraph> _subGraphs;
    private SRSNode _node;
    private int _stages;
    private int _steps;
    private double _modularity;
    private ArrayList<Double> _efficiency;
    private int _recCnt;
    private int _sharing;
    private boolean _pinned;
}