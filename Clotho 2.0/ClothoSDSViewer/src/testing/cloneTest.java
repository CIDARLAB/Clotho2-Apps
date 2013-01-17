/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import datastructures.SRSGraph;
import datastructures.SRSNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 *
 * @author evanappleton
 */
public class cloneTest {

    //Node that has neighbors that also has neighbors
    public void testCloneNeighborsNeighbors() {

        String one = "a";
        String two = "b";
        String three = "c";
        String four = "d";

        ArrayList<String> compn1 = new ArrayList<String>();
        compn1.add(one);
        compn1.add(two);
        compn1.add(three);
        compn1.add(four);
        ArrayList<String> compn2 = new ArrayList<String>();
        compn2.add(one);
        compn2.add(two);
        ArrayList<String> compn3 = new ArrayList<String>();
        compn3.add(three);
        compn3.add(four);
        ArrayList<String> compn4 = new ArrayList<String>();
        compn4.add(one);
        ArrayList<String> compn5 = new ArrayList<String>();
        compn5.add(two);
        ArrayList<String> compn6 = new ArrayList<String>();
        compn6.add(three);
        ArrayList<String> compn7 = new ArrayList<String>();
        compn7.add(four);

        SRSNode n1 = new SRSNode(false, null, compn1, null, null, null, null, null, 0);
        SRSNode n2 = new SRSNode(false, null, compn2, null, null, null, null, null, 0);
        SRSNode n3 = new SRSNode(false, null, compn3, null, null, null, null, null, 0);
        SRSNode n4 = new SRSNode(false, null, compn4, null, null, null, null, null, 0);
        SRSNode n5 = new SRSNode(false, null, compn5, null, null, null, null, null, 0);
        SRSNode n6 = new SRSNode(false, null, compn6, null, null, null, null, null, 0);
        SRSNode n7 = new SRSNode(false, null, compn7, null, null, null, null, null, 0);
        n1.addNeighbor(n2);
        n1.addNeighbor(n3);
        n2.addNeighbor(n1);
        n2.addNeighbor(n4);
        n2.addNeighbor(n5);
        n4.addNeighbor(n2);
        n5.addNeighbor(n2);
        n3.addNeighbor(n1);
        n3.addNeighbor(n6);
        n3.addNeighbor(n7);
        n6.addNeighbor(n3);
        n7.addNeighbor(n3);

        SRSGraph g0 = new SRSGraph(n1);
        SRSGraph g1 = g0.clone();

        ArrayList<Integer> graph0 = checkCloneNums(g0);
        ArrayList<Integer> graph1 = checkCloneNums(g1);
        System.out.println("graph0: " + graph0);
        System.out.println("graph1: " + graph1);

    }

    //Node that has neighbors with no neighbors
    public void testCloneNeighbors() {

        String one = "a";
        String two = "b";

        ArrayList<String> compn8 = new ArrayList<String>();
        compn8.add(one);
        compn8.add(two);
        ArrayList<String> compn9 = new ArrayList<String>();
        compn9.add(one);
        ArrayList<String> compn10 = new ArrayList<String>();
        compn10.add(two);

        SRSNode n8 = new SRSNode(false, null, compn8, null, null, null, null, null, 0);
        SRSNode n9 = new SRSNode(false, null, compn9, null, null, null, null, null, 0);
        SRSNode n10 = new SRSNode(false, null, compn10, null, null, null, null, null, 0);
        n8.addNeighbor(n9);
        n8.addNeighbor(n10);
        n9.addNeighbor(n8);
        n10.addNeighbor(n8);

        SRSGraph g2 = new SRSGraph(n8);
        SRSGraph g3 = g2.clone();

        ArrayList<Integer> graph2 = checkCloneNums(g2);
        ArrayList<Integer> graph3 = checkCloneNums(g3);

        System.out.println("graph2: " + graph2);
        System.out.println("graph3: " + graph3);

    }

    //Node with no neighbors
    public void testCloneNoNeighbors() {

        SRSNode n11 = new SRSNode();
        SRSGraph g4 = new SRSGraph(n11);
        SRSGraph g5 = g4.clone();

        ArrayList<Integer> graph4 = checkCloneNums(g4);
        ArrayList<Integer> graph5 = checkCloneNums(g5);

        System.out.println("graph4: " + graph4);
        System.out.println("graph5: " + graph5);

    }

    //Fucked up node
    public void fuckedUpNode() {

        SRSNode n12 = new SRSNode();
        SRSNode n13 = new SRSNode();
        SRSNode n14 = new SRSNode();
        n12.addNeighbor(n13);
        n13.addNeighbor(n14);
        n13.addNeighbor(n12);
        n14.addNeighbor(n13);

        SRSGraph g6 = new SRSGraph(n12);
        SRSGraph g7 = g6.clone();

        ArrayList<Integer> graph6 = checkCloneNums(g6);
        ArrayList<Integer> graph7 = checkCloneNums(g7);

        System.out.println("graph6: " + graph6);
        System.out.println("graph7: " + graph7);

    }

    //Clone blank graph
    public void blankGraph() {

        SRSGraph g8 = new SRSGraph();
        SRSGraph g9 = g8.clone();

        ArrayList<Integer> graph8 = checkCloneNums(g8);
        ArrayList<Integer> graph9 = checkCloneNums(g9);

        System.out.println("graph8: " + graph8);
        System.out.println("graph9: " + graph9);

    }

    //Verify that graphs are equivalent
    private ArrayList<Integer> checkCloneNums(SRSGraph g0) {

        _stack = new Stack<SRSNode>();
        _nodelist = new HashSet<SRSNode>();
        SRSNode root = g0.getRootNode();
        ArrayList<SRSNode> neighbors = root.getNeighbors();
        ArrayList<Integer> nums = new ArrayList<Integer>();
        _nodelist.add(root);
//        nums.add(root.getNodeNum());

        for (int i = 0; i < neighbors.size(); i++) {
            _nodelist.add(neighbors.get(i));
            _stack.add(neighbors.get(i));
//            nums.add(neighbors.get(i).getNodeNum());
        }
        if (!_stack.isEmpty()) {
            SRSNode next = _stack.pop();
            nums = checkCloneNumsHelper(nums, next);
        }

        return nums;

    }

    //Verify helper
    private ArrayList<Integer> checkCloneNumsHelper(ArrayList<Integer> nums, SRSNode node) {

        SRSNode current = node;
        ArrayList<SRSNode> neighbors = current.getNeighbors();
        for (int i = 0; i < neighbors.size(); i++) {
            if (_nodelist.add(neighbors.get(i)) == true) {
                _stack.add(neighbors.get(i));
//                nums.add(neighbors.get(i).getNodeNum());
            }
        }
        if (_stack.isEmpty() == false) {
            SRSNode next = _stack.pop();
            current = next;
            nums = checkCloneNumsHelper(nums, next);
            return nums;
        } else {
            return nums;
        }


    }

    //Verify that graphs are equivalent
    private ArrayList<String> checkCloneNodes(SRSGraph g0) {

        _stack = new Stack<SRSNode>();
        _nodelist = new HashSet<SRSNode>();
        SRSNode root = g0.getRootNode();
        ArrayList<SRSNode> neighbors = root.getNeighbors();
        ArrayList<String> nodes = new ArrayList<String>();
        _nodelist.add(root);

        for (int i = 0; i < neighbors.size(); i++) {
            _nodelist.add(neighbors.get(i));
            _stack.add(neighbors.get(i));
            nodes.add(neighbors.get(i).toString());
        }
        if (!_stack.isEmpty()) {
            SRSNode next = _stack.pop();
            nodes = checkCloneNodesHelper(nodes, next);
        }

        return nodes;

    }

    //Verify helper
    private ArrayList<String> checkCloneNodesHelper(ArrayList<String> nodes, SRSNode node) {

        SRSNode current = node;
        ArrayList<SRSNode> neighbors = current.getNeighbors();
        for (int i = 0; i < neighbors.size(); i++) {
            if (_nodelist.add(neighbors.get(i)) == true) {
                _stack.add(neighbors.get(i));
                nodes.add(neighbors.get(i).toString());
            }
        }
        if (_stack.isEmpty() == false) {
            SRSNode next = _stack.pop();
            current = next;
            nodes = checkCloneNodesHelper(nodes, next);
            return nodes;
        } else {
            return nodes;
        }


    }
    //FIELDS
    private Stack<SRSNode> _stack;
    private HashSet<SRSNode> _nodelist;
}
