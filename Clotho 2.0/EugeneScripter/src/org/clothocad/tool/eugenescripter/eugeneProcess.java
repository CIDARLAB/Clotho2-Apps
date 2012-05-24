/*
Copyright (c) 2010 The Regents of the University of California.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS..
 */
package org.clothocad.tool.eugenescripter;

import eugene.parser.EugeneParser;
import eugene.parser.EugeneLexer;
import eugene.*;
import eugene.parser.SymbolTables;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.*;
import org.clothocore.api.data.ObjType;
import org.openide.util.Exceptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Douglas Densmore
 */
public class eugeneProcess {
    private static HashMap<String, org.clothocore.api.data.Part> hmClothoBasicParts;
    private static HashMap<String, org.clothocore.api.data.Part> hmClothoCompositeParts;
    public static boolean runEugene(String code, JTextPane outputPane) throws IOException {

        outputPane.setText(outputPane.getText() + "\nRunning Eugene\n");

        if (!code.equals("") && code != null) {

            //Write code to a file
            String newFileName = System.getProperty("java.io.tmpdir") + "/tmpIn.eug";
            File eugFile = new File(newFileName);
            Writer output = new BufferedWriter(new FileWriter(eugFile));
            output.write(code);
            output.close();

            //create lexer and parser
            eugene.parser.EugeneLexer lex = new eugene.parser.EugeneLexer(new ANTLRFileStream(eugFile.getAbsolutePath()));
            CommonTokenStream tokens = new CommonTokenStream(lex);
            eugene.parser.EugeneParser parser = new eugene.parser.EugeneParser(tokens);
            parser.initSymbolTables();
            try {
                parser.prog();
            } catch (RecognitionException ex) {
                Exceptions.printStackTrace(ex);
            }
            initHashMaps();
            int n = JOptionPane.showConfirmDialog(
                    null, 
                    "Would you like to save parts and devices in Clotho?",
                    "Save Parts", 
                    JOptionPane.YES_NO_OPTION);
            if (n== JOptionPane.YES_OPTION){
                decomposeEugene(parser, outputPane);
            }
            cleanUp();
            parser.cleanUpNoExit();
        }

        return true;

    }

    private static void decomposeEugene(eugene.parser.EugeneParser parser, JTextPane outputPane) {
        //create a collection
        Collection coll = new Collection();
        //get format to create clothoParts in
        //Select Format
        Object[] allNames = Collector.getAllLinksOf(ObjType.FORMAT).toArray();
        ObjLink link = (ObjLink) JOptionPane.showInputDialog(null, "Choose format to save parts in. \n", "Format",
                JOptionPane.INFORMATION_MESSAGE, null, allNames, allNames[0]);

        //Create a new part
        String format = new String();
        if (link != null) {
            format = link.name;
        } else {
            format = "Freeform";
        }
        
        createClothoBasicParts(outputPane, coll, format);
        createClothoCompositeParts(outputPane, coll, format);
        coll.launchDefaultViewer();
    }

    public static void generateHeaderFiles(JTextPane outputPane) {
        try {
            // create new property def file            
            String newFileName = System.getProperty("user.dir") + "/PropertyDefinition.h";
            File propDefFile = new File(newFileName);
            Writer output = new BufferedWriter(new FileWriter(propDefFile, false));
            output.write("Property name(txt);\n");
            output.write("Property sequence(txt);\n");
            output.write("Property shortDescription(txt);\n");
            output.close();
            outputPane.setText(outputPane.getText() + "Property Definiton File generated.\n");
            // create new part def file
            // how to define part type?  need features?
            // combine parts and features later.  need naming scheme to correlate features/parts
            newFileName = System.getProperty("user.dir") + "/PartDefinition.h";
            File partDefFile = new File(newFileName);
            output = new BufferedWriter(new FileWriter(partDefFile, false));
            output.write("Part clothoPart(name, sequence, shortDescription);\n");
            output.close();
            outputPane.setText(outputPane.getText() + "Part Definiton File generated.\n");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void generatePartList(JTextPane outputPane) {
        Writer output = null;
        try {
            ArrayList<ObjLink> allColl = Collector.getAllLinksOf(ObjType.COLLECTION);
            if (allColl.isEmpty()) {
                return;
            }
            Object[] allNames = allColl.toArray();
            ObjLink link = (ObjLink) JOptionPane.showInputDialog(null, "Choose one \n  Choose 'cancel' to select all collections.", "Collection",
                    JOptionPane.INFORMATION_MESSAGE, null, allNames, allNames[0]);
            if (link != null) {
                Collection chosen = Collector.getCollection(link.uuid);
                String newFileName = System.getProperty("user.dir") + "/" + chosen.getName() + "PartDeclaration.h";
                File partDefFile = new File(newFileName);
                output = new BufferedWriter(new FileWriter(partDefFile, false));
                ArrayList<org.clothocore.api.data.Part> parts = (ArrayList<org.clothocore.api.data.Part>) chosen.getAll(ObjType.PART);
                for (int i = 0; i < parts.size(); i++) {
                    org.clothocore.api.data.Part tempPart = parts.get(i);
                    String partName = tempPart.getName();
                    String partSeq = tempPart.getSeq().getSeq();
                    String partDesc = tempPart.getShortDescription();
                    output.write("clothoPart " + partName + "(" + partName + " " + partSeq + " " + partDesc + ")\n");
                }
            } else {
                String newFileName = System.getProperty("user.dir") + "/PartDeclaration.h";
                File partDefFile = new File(newFileName);
                output = new BufferedWriter(new FileWriter(partDefFile, false));
                ArrayList<org.clothocore.api.data.Part> parts = Collector.getAll(ObjType.PART);
                for (int i = 0; i < parts.size(); i++) {
                    org.clothocore.api.data.Part tempPart = parts.get(i);
                    String partName = tempPart.getName();
                    String partSeq = tempPart.getSeq().getSeq();
                    String partDesc = tempPart.getShortDescription();
                    output.write("clothoPart " + partName + "(" + partName + " " + partSeq + " " + partDesc + ")\n");
                }
            }
            output.close();
            outputPane.setText(outputPane.getText() + "Part Declartion File generated.\n");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    private static void initHashMaps(){
        hmClothoBasicParts=new HashMap<String,org.clothocore.api.data.Part>();
        hmClothoCompositeParts=new HashMap<String,org.clothocore.api.data.Part>();
    }
    private static void cleanUp(){
        hmClothoBasicParts.clear();
        hmClothoCompositeParts.clear();
        hmClothoBasicParts = null;
        hmClothoCompositeParts = null;
    }
    
    private static void createClothoBasicParts(JTextPane outputPane, Collection coll, String format){
        //take all Eugene Part declarations and create clotho basic parts
        String nameDefault = "DefaultName";
        String sdDefault = "DefaultShortDescription";
        String seqDefault = "GATCTAAAAAAG";
        
        Iterator i = SymbolTables.getPartInstances().iterator();
        while(i.hasNext()){
            eugene.dom.Part currentPart = (eugene.dom.Part) i.next();
            outputPane.setText(outputPane.getText() + currentPart.getInstanceName() + "\n");
            if (currentPart.getPropertyValue("name") != null) {
                nameDefault = currentPart.getPropertyValue("name").toString();
            }
            if (currentPart.getPropertyValue("sequence") != null) {
                seqDefault = currentPart.getPropertyValue("sequence").toString();
            }
            if (currentPart.getPropertyValue("description") != null) {
                sdDefault = currentPart.getPropertyValue("description").toString();
            }
            // check for preexisting part/sequence in clotho database
            NucSeq aseq = new NucSeq(seqDefault);
            aseq.setTransient();
            String key = aseq + Format.retrieveByName(format).getUUID();
            String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
            org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
            org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByName(nameDefault);
            if (prexistingName != null) {
                System.out.print("Skipping part " + currentPart.getInstanceName() + " because name already exists in database.\n");
                hmClothoBasicParts.put(currentPart.getInstanceName(), prexistingName);
                continue;
            } else if (prexistingSeq != null) {
                System.out.print("Skipping part " + currentPart.getInstanceName() + " because sequence already exists in database.\n");
                hmClothoBasicParts.put(currentPart.getInstanceName(), prexistingSeq);
                continue;
            }
            // parser catches parts being declared twice.  i don't have to care about it here.
            // in that case, make a new clotho part, add it to the collection and the hash map
            org.clothocore.api.data.Part partInstance = org.clothocore.api.data.Part.generateBasic(nameDefault, sdDefault, seqDefault, Format.retrieveByName(format), Collector.getCurrentUser());
            hmClothoBasicParts.put(currentPart.getInstanceName(), partInstance);
            //add the part to the collection
            coll.addObject(partInstance);
            aseq = null;
            prexistingSeq = null;
            prexistingName = null;
            partInstance = null;
            currentPart = null;
        }        
    }
    private static void createClothoCompositeParts(JTextPane outputPane, Collection coll, String format){
        // populate partList with component list from eugene Device
        Iterator i = SymbolTables.getDevices().iterator();
        while (i.hasNext()) {
            eugene.dom.Device currentDevice = (eugene.dom.Device) i.next();
            System.out.println(currentDevice.getName() + "started.");
            // check for device in Clotho database
            org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByName(currentDevice.getName());
            if (prexistingName != null) {
                System.out.print("Skipping part " + currentDevice.getName() + " because name already exists in database.\n");
                hmClothoCompositeParts.put(currentDevice.getName(), prexistingName);
                continue;
            }
            org.clothocore.api.data.Part compPart = makeCompositePart(format, currentDevice.getComponents(), currentDevice.getName(), coll);
            System.out.println(currentDevice.getName() + " finished.");
            compPart = null;
            currentDevice = null;
        }
    }
    private static org.clothocore.api.data.Part makeCompositePart(String format, ArrayList<eugene.dom.Component> partsList, String name, Collection coll){
        ArrayList<org.clothocore.api.data.Part> tempPartsList = new ArrayList<org.clothocore.api.data.Part>();
        for(int i = 0; i <partsList.size(); i++){
            eugene.dom.Component objComponent = partsList.get(i);
            if(null!=objComponent && objComponent instanceof eugene.dom.Device){
                org.clothocore.api.data.Part tempPart = hmClothoCompositeParts.get(objComponent.getName());
                if (tempPart != null) {
                    System.out.println(tempPartsList);
                    System.out.println(tempPart);
                    tempPartsList.add(tempPart);
                    tempPart = null;
                } else {
                    eugene.dom.Device containingDevice = (eugene.dom.Device)objComponent; 
                    tempPart = makeCompositePart(format, containingDevice.getComponents(), containingDevice.getName(), coll);
                    tempPartsList.add(tempPart);
                    tempPart = null;
                }
            } else if (null != objComponent && objComponent instanceof eugene.dom.Part){
                Part tempPart = hmClothoBasicParts.get(objComponent.getName());
                if (tempPart != null){
                    tempPartsList.add(tempPart);
                }
                tempPart = null;
            } else {
                System.err.println(objComponent.toString() + " cannot be added to a Clotho Composite Part.");
                objComponent = null;
                tempPartsList.clear();
                tempPartsList = null;
                return null;
            }
            
            objComponent = null;
        }
        org.clothocore.api.data.Part compPart = org.clothocore.api.data.Part.generateComposite(tempPartsList, null, Format.retrieveByName(format), Collector.getCurrentUser(), name, "DefaultDescription");
        coll.addObject(compPart);
        hmClothoCompositeParts.put(name, compPart);
        tempPartsList.clear();
        tempPartsList = null;
        return compPart;
    }
}

