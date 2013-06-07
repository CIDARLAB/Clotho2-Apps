/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.pigeongenerator;

import java.util.ArrayList;
import org.clothocad.tool.weyekin.WeyekinPoster;
import org.clothocore.api.data.Part;
import org.openide.util.Exceptions;

/**
 *
 * @author jenhan
 */
public class controller {

    public static ArrayList<Part> getComposition(org.clothocore.api.data.Part compositePart) throws Exception {
        ArrayList<Part> toReturn = new ArrayList<Part>();
        if (compositePart.getPartType().equals(Part.partType.Basic)) {
            throw (new Exception("parseForBasicPartAttributes should only be invoked using a composite Clotho part"));
        } else {
            ArrayList<org.clothocore.api.data.Part> composition = compositePart.getCompositeParts();
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

    //helper for recursion method to discover all basic parts
    private static ArrayList<Part> getCompositionHelper(org.clothocore.api.data.Part somePart, ArrayList<Part> partsList) throws Exception {
        ArrayList<Part> toReturn = partsList;
        Part compositePart = somePart;
        if (compositePart.getPartType().equals(Part.partType.Basic)) {
            throw (new Exception("parseForBasicPartAttributesHelper should only be invoked using a composite Clotho part"));
        } else {
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
    }

    static void postPigeon(String currentName) {
        try {
            Part currentPart = Part.retrieveByExactName(currentName);
            ArrayList<Part> composition = controller.getComposition(currentPart);
            String pigeonString = "";

            for (Part part : composition) {

                ArrayList<String> searchTags = part.getSearchTags();
                if (searchTags.contains("promoter")) {
                    pigeonString = pigeonString + "p p(" + part.getName() + ")\n";
                } else if (searchTags.contains("rbs")) {
                    pigeonString = pigeonString + "r\n";
                } else if (searchTags.contains("terminator")) {
                    pigeonString = pigeonString + "t\n";
                } else {
                    pigeonString = pigeonString + "c g(" + part.getName() + ")\n";
                }


            }
            pigeonString = pigeonString + "# Arcs";
            System.out.println("pigeon string: " + pigeonString);
            WeyekinPoster.setPigeonText(pigeonString);
            WeyekinPoster.postMyBird();


        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }



    }
}
