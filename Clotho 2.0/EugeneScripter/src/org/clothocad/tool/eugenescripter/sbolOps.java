/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cassie
 */
package org.clothocad.tool.eugenescripter;

import java.io.*;
import java.net.URI;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Format;
import org.clothocore.api.data.NucSeq;
import org.clothocore.api.data.Part;
import org.openide.util.Exceptions;
import org.sbolstandard.core.*;
import org.sbolstandard.xml.*;
import javax.xml.bind.JAXBException;

public class sbolOps {

    protected static HashMap<String, Part> hmSBOLImportParts;  //saves as clotho parts
    protected static HashMap<String, DnaComponent> hmSBOLExportParts;  //parts already made, for easy lookup later

    public static void initHashMaps() {
        hmSBOLImportParts = new HashMap<String, org.clothocore.api.data.Part>();
        hmSBOLExportParts = new HashMap<String, DnaComponent>();
    }

    public static void cleanUp() {
        hmSBOLImportParts.clear();
        hmSBOLExportParts.clear();
        hmSBOLImportParts = null;
        hmSBOLExportParts = null;
    }

    public static void exportToSBOL(String name, String uri, String description, MessageConsole mc) throws IOException, JAXBException {
        mc.setEnable(true);        
        System.out.print("Exporting to SBOL...");
        mc.setEnable(false);        
        String newFileName = System.getProperty("user.dir") + "/" + name + "_SBOL.xml";
        File xmlFile = new File(newFileName);
        BufferedWriter output = new BufferedWriter(new FileWriter(xmlFile, false));
        // 2. create sbol collections
        org.sbolstandard.xml.CollectionImpl sbolCollection = new CollectionImpl();
        sbolCollection.setURI(org.sbolstandard.xml.UtilURI.Create(uri));
        sbolCollection.setName(name);
        sbolCollection.setDisplayId(name);
        sbolCollection.setDescription(description);
        // 3. iterate over clotho parts and add to sbolCollection              
        Set basicParts = clothoOps.hmClothoBasicParts.entrySet();
        Set compositeParts = clothoOps.hmClothoCompositeParts.entrySet();
        Iterator it = basicParts.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Part newPart = (Part) entry.getValue();
            makeSBOLDnaComponent(newPart, sbolCollection, mc);
        }

        it = compositeParts.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Part newPart = (Part) entry.getValue();
            makeSBOLDnaComponent(newPart, sbolCollection, mc);
        }
        // 4.  create parser and write to file
        Parser parser = new Parser();
        output.write(parser.serialize(sbolCollection));
        mc.setEnable(true);
        System.out.print("SBOL file saved to " + newFileName + ".");
        mc.setEnable(false);        
        output.close();
    }

    public static void exportToSBOLSelection(JList list, String name, String uri, String description, MessageConsole mc) throws IOException, JAXBException {
        mc.setEnable(true);        
        System.out.println("Exporting to SBOL...");
        mc.setEnable(false);        
        String newFileName = System.getProperty("user.dir") + "/" + name + "_SBOL.xml";
        File xmlFile = new File(newFileName);
        BufferedWriter output = new BufferedWriter(new FileWriter(xmlFile, false));
        // 2. create sbol collections
        org.sbolstandard.xml.CollectionImpl sbolCollection = new CollectionImpl();
        sbolCollection.setURI(org.sbolstandard.xml.UtilURI.Create(uri));
        sbolCollection.setName(name);
        sbolCollection.setDisplayId(name);
        sbolCollection.setDescription(description);
        // 3. iterate over clotho parts and add to sbolCollection              
        for (int i = list.getMinSelectionIndex(); i < list.getMaxSelectionIndex() +1; i++) {
            if (list.isSelectedIndex(i)) {
                if (clothoOps.hmClothoBasicParts.get((String) list.getModel().getElementAt(i)) != null) {
                    Part newPart = clothoOps.hmClothoBasicParts.get((String) list.getModel().getElementAt(i));
                    makeSBOLDnaComponent(newPart, sbolCollection, mc);
                } else if (clothoOps.hmClothoCompositeParts.get((String) list.getModel().getElementAt(i)) != null) {
                    Part newPart = clothoOps.hmClothoCompositeParts.get((String) list.getModel().getElementAt(i));
                    makeSBOLDnaComponent(newPart, sbolCollection, mc);
                }
            }
        }
        // 4.  create parser and write to file
        Parser parser = new Parser();
        output.write(parser.serialize(sbolCollection));
        mc.setEnable(true);
        System.out.println("SBOL file saved to " + newFileName + ".");
        mc.setEnable(false);        
        output.close();

    }

    private static DnaComponent makeSBOLDnaComponent(org.clothocore.api.data.Part clothoPart, CollectionImpl sbolCollection, MessageConsole mc) {
        if (hmSBOLExportParts.get(clothoPart.getName()) != null) {
            return hmSBOLExportParts.get(clothoPart.getName());
        }
        if (clothoPart.getPartType() == org.clothocore.api.data.Part.partType.Basic) {
            DnaComponent sbolDnaComponent = new DnaComponentImpl();
            sbolDnaComponent.setName(clothoPart.getName());
            sbolDnaComponent.setDisplayId(clothoPart.getName());
            sbolDnaComponent.setDescription(clothoPart.getShortDescription());
            String uri = "http://clotho.org/Part:" + clothoPart.getName().replace(" ", "_");
            sbolDnaComponent.setURI(URI.create(uri));
            DnaSequence sbolDnaSequence = new DnaSequenceImpl();
            sbolDnaSequence.setURI(org.sbolstandard.xml.UtilURI.Create(clothoPart.getSeq().getUUID()));
            sbolDnaSequence.setNucleotides(clothoPart.getSeq().getSeq().toLowerCase());
            sbolDnaComponent.setDnaSequence(sbolDnaSequence);
            mc.setEnable(true);
            System.out.print("Clotho Part " + clothoPart.getName() + " added successfully to SBOL Collection.\n");
            mc.setEnable(false);            
            sbolCollection.addComponent(sbolDnaComponent);
            hmSBOLExportParts.put(clothoPart.getName(), sbolDnaComponent);
            return sbolDnaComponent;
        } else {
            ArrayList<Part> subparts = (ArrayList<Part>) clothoPart.getCompositeParts();
            DnaComponent sbolDnaComponent = new DnaComponentImpl();
            sbolDnaComponent.setName(clothoPart.getName());
            sbolDnaComponent.setDisplayId(clothoPart.getName());
            sbolDnaComponent.setDescription(clothoPart.getShortDescription());
            String uri = "http://clotho.org/Part:" + clothoPart.getName().replace(" ", "_");
            sbolDnaComponent.setURI(URI.create(uri));
            DnaSequence sbolDnaSequence = new DnaSequenceImpl();
            sbolDnaSequence.setURI(org.sbolstandard.xml.UtilURI.Create(clothoPart.getSeq().getUUID()));
            sbolDnaSequence.setNucleotides(clothoPart.getSeq().getSeq().toLowerCase());
            sbolDnaComponent.setDnaSequence(sbolDnaSequence);
            int bioStart, bioEnd, currLength = 0;
            for (int i = 0; i < subparts.size(); i++) {
                    SequenceAnnotation sbolSequenceAnnotation = new SequenceAnnotationImpl();
                    sbolSequenceAnnotation.setURI(org.sbolstandard.xml.UtilURI.Create("Sequence_Annotation_" + Integer.toString(i)));
                    DnaComponent sbolSubcomponent = makeSBOLDnaComponent(subparts.get(i), sbolCollection, mc);
                    sbolSequenceAnnotation.setSubComponent(sbolSubcomponent);
                    bioStart = currLength + 1;
                    bioEnd = bioStart + sbolSubcomponent.getDnaSequence().getNucleotides().length() -1;
                    currLength = bioEnd;
                    sbolSequenceAnnotation.setBioStart((Integer)bioStart);
                    sbolSequenceAnnotation.setBioEnd(bioEnd);
                    sbolDnaComponent.addAnnotation(sbolSequenceAnnotation);
            }
            sbolCollection.addComponent(sbolDnaComponent);
            hmSBOLExportParts.put(clothoPart.getName(), sbolDnaComponent);
            return sbolDnaComponent;
        }
    }

    public static void importFromSBOL(File sbolFile, JList list, DefaultListModel listModel, JTextField textField) throws JAXBException {
        try {
            //import from SBOL to eugene, store sbol dna components in hashmap
        //gets an SBOL XML file and proceeds to convert it into a clotho collection
        BufferedReader in = new BufferedReader(new FileReader(sbolFile));
        String str;
        String xml = "";
        while ((str = in.readLine()) != null) {
            //topPane.append(str + "\n");
            xml += str;
        }
        in.close();
        // declare parser
        Parser parser = new Parser();
        org.sbolstandard.xml.CollectionImpl sbolCollection = parser.parse(xml);
                java.util.Collection<DnaComponent> sbolPartsList = sbolCollection.getComponents();
        Iterator<DnaComponent> it = sbolPartsList.iterator();
            //iterate through list, get all DNA components
            //if dna component w/ no sequence annotations, make basic part.  tag with type if supplied
            //otherwise make composite part.  tag each sequence annotation individually.
            while (it.hasNext()) {
                if (it.next() instanceof DnaComponent) {
                    DnaComponent newComp = (DnaComponent) it.next();
                    Part clothoPart = makeClothoPart(newComp);
                    listModel.addElement(clothoPart.getName());
                } else {
                    continue;
                }
            }
            list.setModel(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static Part makeClothoPart(org.sbolstandard.core.DnaComponent sbolDnaComponent) {
        // 1.  get dna sequence of sbolDna Component.  if sequence = null, write error to outputPane.
        String sbolSequence = sbolDnaComponent.getDnaSequence().getNucleotides();
        if (sbolSequence == null) {
            return null;
        } else if (hmSBOLImportParts.get(sbolDnaComponent.getName()) != null) {
            return hmSBOLImportParts.get(sbolDnaComponent.getName());
        } else {
            String name = sbolDnaComponent.getName();
            String format = "freeform";
            // 2.  if sequence exists, crelotate clothoNucSeq with that sequence
            NucSeq seq = new NucSeq(sbolSequence);
            seq.setTransient();
            String key = seq.getSeq() + Format.retrieveByName(format).getUUID();
            String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
            // checks to make sure part does not already exist before making it.
            // cuts down on the number of popups that happen
            // add search tag
            //org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
            org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByName(name);

            if (prexistingName != null) {
                hmSBOLImportParts.put(sbolDnaComponent.getName(), prexistingName);
                return prexistingName;
            }

            org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);

            if (prexistingName != null) {
                hmSBOLImportParts.put(sbolDnaComponent.getName(), prexistingSeq);
                return prexistingSeq;
            }

            // 3.  get list of sequence annotations
            java.util.Collection<SequenceAnnotation> sbolSequenceAnnotations = sbolDnaComponent.getAnnotations();
            if (sbolSequenceAnnotations == null) {
                //has no annoations and thus no subcomponents.  create basic part and return
                org.clothocore.api.data.Part partInstance = org.clothocore.api.data.Part.generateBasic(sbolDnaComponent.getName(), sbolDnaComponent.getDescription(), sbolSequence, Format.retrieveByName(format), Collector.getCurrentUser());
                hmSBOLImportParts.put(sbolDnaComponent.getName(), partInstance);
                return partInstance;
            }
            // 4.  create list of clotho parts for potential subcomponents
            ArrayList<org.clothocore.api.data.Part> clothoPartList = new ArrayList<org.clothocore.api.data.Part>();
            // 5.  for each sequence annotation, get the subcomponent if it exists
            Iterator<SequenceAnnotation> iter = sbolSequenceAnnotations.iterator();
            while (iter.hasNext()) {
                DnaComponent sbolSubcomponent = iter.next().getSubComponent();
                if (sbolSubcomponent == null) {
                    continue;
                }
                Part clothoSubcomponent = makeClothoPart(sbolSubcomponent);
                // 7.  add clothopart to list
                clothoPartList.add(clothoSubcomponent);
            }
            // 8.  check to see if list is nil if yes, create basic part.  else create composite part
            if (clothoPartList.isEmpty()) {
                org.clothocore.api.data.Part partInstance = org.clothocore.api.data.Part.generateBasic(sbolDnaComponent.getName(), sbolDnaComponent.getDescription(), sbolSequence, Format.retrieveByName(format), Collector.getCurrentUser());
                clothoPartList.clear();
                hmSBOLImportParts.put(sbolDnaComponent.getName(), partInstance);
                return partInstance;
            } else {
                org.clothocore.api.data.Part partInstance = org.clothocore.api.data.Part.generateComposite(clothoPartList, null, Format.retrieveByName(format), Collector.getCurrentUser(), sbolDnaComponent.getName(), sbolDnaComponent.getDescription());
                clothoPartList.clear();
                hmSBOLImportParts.put(sbolDnaComponent.getName(), partInstance);
                return partInstance;
            }
        }
    }
}
