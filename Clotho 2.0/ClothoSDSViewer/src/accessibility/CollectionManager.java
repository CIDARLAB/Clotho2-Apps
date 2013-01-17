/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accessibility;

import datastructures.SRSGraph;
import datastructures.SRSNode;
import java.util.ArrayList;
import java.util.HashSet;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Collection;
import org.clothocore.api.data.Part;
import org.clothocore.api.data.Vector;

/**
 *
 * @author evanappleton
 */
public class CollectionManager {

    public CollectionManager() {
        _collection = Collector.getCurrentUser().getHerCollection();
    }
    
    public void addToCollection(SRSGraph graph, boolean saveToDB) {
        
        //Traverse graph to get all parts
        ArrayList<SRSNode> queue = new ArrayList();
        HashSet<SRSNode> seenNodes = new HashSet();
        queue.add(graph.getRootNode());
        while (!queue.isEmpty()) {
            SRSNode current = queue.get(0);
            queue.remove(0);
            seenNodes.add(current);
            for (SRSNode neighbor : current.getNeighbors()) {
                if (!seenNodes.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
            seenNodes.add(current);    
        }
        
        //Add all intermediates to the collection
        for (SRSNode current: seenNodes) {
            System.out.println("Collection manager part add composition: " + current.getComposition());
            Vector vector = Vector.retrieveByName(current.getVector().toString());
            Part part = Part.retrieveByExactName(current.getName());
            _collection.addObject(part);
        }
        
        //If box in viewer is checked to save to the current user's collection
        if (saveToDB) {
            _collection.saveDefault();
        }        
    }
    
    //Field
    private static Collection _collection;
}
