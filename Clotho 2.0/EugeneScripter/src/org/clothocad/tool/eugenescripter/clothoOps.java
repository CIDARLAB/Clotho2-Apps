/*import javax.swing.*;
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cassie
 */
package org.clothocad.tool.eugenescripter;

import eugene.dom.PropertyValue;
import java.util.*;
import javax.swing.*;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Collection;
import org.clothocore.api.data.*;

public class clothoOps {

    //method 1:  load collections into list
    //method 2:  save to database
    public static HashMap<String, org.clothocore.api.data.Part> hmClothoBasicParts;
    public static HashMap<String, org.clothocore.api.data.Part> hmClothoCompositeParts;
    public static HashMap<String, org.clothocore.api.data.Part> hmClothoImportParts;
    private static ArrayList<eugene.dom.components.Part> pList;
    private static ArrayList<eugene.dom.components.Device> dList;
    private static ArrayList<eugene.dom.arrays.PartArray> pArrayList;
    private static ArrayList<eugene.dom.arrays.DeviceArray> dArrayList;

    public static void initHashMaps() {
        hmClothoBasicParts = new HashMap<String, org.clothocore.api.data.Part>();
        hmClothoCompositeParts = new HashMap<String, org.clothocore.api.data.Part>();
        hmClothoImportParts = new HashMap<String, org.clothocore.api.data.Part>();
        pList = new ArrayList<eugene.dom.components.Part>();
        dList = new ArrayList<eugene.dom.components.Device>();
        pArrayList = new ArrayList<eugene.dom.arrays.PartArray>();
        dArrayList = new ArrayList<eugene.dom.arrays.DeviceArray>();
    }

    public static void cleanUp() {
        hmClothoBasicParts.clear();
        hmClothoCompositeParts.clear();
        hmClothoImportParts.clear();
        hmClothoBasicParts = null;
        hmClothoCompositeParts = null;
        hmClothoImportParts = null;
    }

    private static void createClothoBasicParts(String format, ArrayList<eugene.dom.components.Part> partList, MessageConsole mc) {
        //take all Eugene Part declarations and create clotho basic parts

        Iterator i = partList.iterator();
        while (i.hasNext()) {
            eugene.dom.components.Part currentPart = (eugene.dom.components.Part) i.next();
            /*
            mc.setEnable(true);
            System.out.print(currentPart.getInstanceName() + "\n");
            mc.setEnable(false);             * 
            */
            saveBasicPart(currentPart, format, mc);
        }
    }

    private static void createClothoCompositeParts(String format, ArrayList<eugene.dom.components.Device> deviceList, MessageConsole mc) {
        // populate partList with component list from eugene Device
        Iterator i = deviceList.iterator();
        int index = 0;
        while (i.hasNext()) {
            eugene.dom.components.Device currentDevice = (eugene.dom.components.Device) i.next();
            mc.setEnable(true);
            System.out.println(currentDevice.getName() + "started.");
            mc.setEnable(false);
            // check for device in Clotho database
            saveCompositePart(currentDevice, index, format, mc);
        }
    }

    private static void unwrapDeviceArrays(String format, ArrayList<eugene.dom.arrays.DeviceArray> dArrayList, MessageConsole mc) {
        //take device arrays from symbol table hmArrays, put in arraylist.  for each element in array list, 
        //iterate over element and save each index of element as device
        Iterator i = dArrayList.iterator();
        while (i.hasNext()) {
            eugene.dom.arrays.DeviceArray dArray = (eugene.dom.arrays.DeviceArray) i.next();
            Iterator j = dArray.getComponents().iterator();
            int index = 0;
            while (j.hasNext()) {
                saveCompositePart((eugene.dom.components.Device) j.next(), index, format, mc);
            }
        }
    }

    private static void unwrapPartArrays(String format, ArrayList<eugene.dom.arrays.PartArray> pArrayList, MessageConsole mc) {
        //todo       
        Iterator i = pArrayList.iterator();
        while (i.hasNext()) {
            eugene.dom.arrays.PartArray pArray = (eugene.dom.arrays.PartArray) i.next();
            Iterator j = pArray.getParts().iterator();
            while (j.hasNext()) {
                saveBasicPart((eugene.dom.components.Part) j.next(), format, mc);
            }
        }

    }

    private static void saveCompositePart(eugene.dom.components.Device currentDevice, int index, String format, MessageConsole mc) {
        org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(currentDevice.getName());
        String deviceName;
        if (prexistingName != null) {
            deviceName = currentDevice.getName() + '_' + Integer.toString(index);
        } else {
            deviceName = currentDevice.getName();
        }
        org.clothocore.api.data.Part compPart = makeCompositePart(format, currentDevice.getComponents(), deviceName, mc);
        mc.setEnable(true);
        System.out.println(currentDevice.getName() + " finished.");
        mc.setEnable(false);        
        compPart = null;
        index++;
    }

    private static void saveBasicPart(eugene.dom.components.Part currentPart, String format, MessageConsole mc) {
        String nameDefault = "DefaultName";
        String sdDefault = "DefaultShortDescription";
        String seqDefault = "GATCTAAAAAAG";
        nameDefault = currentPart.getName();
        if (currentPart.getPartType().getProperty("sequence") != null) {
            PropertyValue sequenceValue = (PropertyValue) currentPart.get("sequence");
            seqDefault = sequenceValue.getValue();
        } else if (currentPart.getPartType().getProperty("Sequence") != null) {
            PropertyValue sequenceValue = (PropertyValue) currentPart.get("Sequence");
            seqDefault = sequenceValue.getValue();
        }
        //tag only if part type is 'promoter', 'RBS', 'CDS', 'terminator', recombinase_f or recombinase_r
        String type = currentPart.getPartType().getName();
        String tag;
        if (type.equals("promoter") || type.equals("RBS") || type.equals("CDS") || type.equals("terminator") || type.equals("recombinase_f") || type.equals("recombinase_r")) {
            tag = type;
        } else {
            tag = null;
        }
        //check for repeats in database
        org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(currentPart.getName());
        if (prexistingName != null) {
            mc.setEnable(true);            
            System.out.print("Skipping part " + currentPart.getName() + " because name already exists in database.\n");
            mc.setEnable(false);            
            if (tag != null) {
                prexistingName.addSearchTag(tag);
            }
            prexistingName.saveDefault();
            hmClothoBasicParts.put(currentPart.getName(), prexistingName);
            return;
        }
        NucSeq aseq = new NucSeq(seqDefault);
        aseq.setTransient();
        String key = aseq.getSeq() + Format.retrieveByName(format).getUUID();
        String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);

        org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
        if (prexistingSeq != null) {
            mc.setEnable(true);
            System.out.print("Skipping part " + currentPart.getName() + " because name already exists in database.\n");
            mc.setEnable(false);            
            if (tag != null) {
                prexistingSeq.addSearchTag(tag);
            }
            prexistingSeq.saveDefault();
            hmClothoBasicParts.put(currentPart.getName(), prexistingSeq);
            return;
        }

        if (currentPart.getPropertyValue("description") != null) {
            sdDefault = currentPart.getPropertyValue("description").toString();
        }
        // parser catches parts being declared twice.  i don't have to care about it here.
        // in that case, make a new clotho part, add it to the collection and the hash map
        org.clothocore.api.data.Part partInstance = org.clothocore.api.data.Part.generateBasic(nameDefault, sdDefault, seqDefault, Format.retrieveByName(format), Collector.getCurrentUser());
        if (tag != null) {
            partInstance.addSearchTag(tag);
        }
        hmClothoBasicParts.put(currentPart.getInstanceName(), partInstance);
        //add the part to the collection
    }

    private static org.clothocore.api.data.Part makeCompositePart(String format, ArrayList<eugene.dom.components.Component> partsList, String name, MessageConsole mc) {
        ArrayList<org.clothocore.api.data.Part> tempPartsList = new ArrayList<org.clothocore.api.data.Part>();
        for (int i = 0; i < partsList.size(); i++) {
            eugene.dom.components.Component objComponent = partsList.get(i);
            if (null != objComponent && objComponent instanceof eugene.dom.components.Device) {
                org.clothocore.api.data.Part tempPart = hmClothoCompositeParts.get(objComponent.getName());
                if (tempPart != null) {
                    tempPartsList.add(tempPart);
                } else {
                    eugene.dom.components.Device containingDevice = (eugene.dom.components.Device) objComponent;
                    tempPart = makeCompositePart(format, containingDevice.getComponents(), containingDevice.getName(), mc);
                    tempPartsList.add(tempPart);
                }
            } else if (null != objComponent && objComponent instanceof eugene.dom.components.Part) {
                org.clothocore.api.data.Part tempPart = hmClothoBasicParts.get(objComponent.getName());
                if (tempPart != null) {
                    tempPartsList.add(tempPart);
                } else {
                    saveBasicPart((eugene.dom.components.Part) objComponent, format, mc);
                    tempPart = hmClothoBasicParts.get(objComponent.getName());
                    tempPartsList.add(tempPart);
                }
            } else {
                mc.setEnable(true);
                System.err.println(objComponent.toString() + " cannot be added to a Clotho Composite Part.");
                mc.setEnable(false);                
                tempPartsList.clear();
                return null;
            }
        }
        org.clothocore.api.data.Part compPart = org.clothocore.api.data.Part.generateComposite(tempPartsList, null, Format.retrieveByName(format), Collector.getCurrentUser(), name, "DefaultDescription");
        hmClothoCompositeParts.put(name, compPart);
        tempPartsList.clear();
        return compPart;
    }

    public static void decomposeEugene(HashMap<String, eugene.dom.SavableElement> results, MessageConsole mc) {
        hmClothoBasicParts.clear();
        hmClothoCompositeParts.clear();
        String format = "Freeform";
        Iterator<eugene.dom.SavableElement> i = results.values().iterator();
        while (i.hasNext()) {
            boolean b;
            eugene.dom.SavableElement e = i.next();
            if (e instanceof eugene.dom.components.Device) {
                b = dList.add((eugene.dom.components.Device) e);
            } else if (e instanceof eugene.dom.components.Part) {
                b = pList.add((eugene.dom.components.Part) e);
            } else if (e instanceof eugene.dom.arrays.DeviceArray) {
                b = dArrayList.add((eugene.dom.arrays.DeviceArray) e);
            } else if (e instanceof eugene.dom.arrays.PartArray) {
                b = pArrayList.add((eugene.dom.arrays.PartArray) e);
            }
        }
        createClothoBasicParts(format, pList, mc);
        unwrapPartArrays(format, pArrayList, mc);
        createClothoCompositeParts(format, dList, mc);
        unwrapDeviceArrays(format, dArrayList, mc);
        pList.clear();
        dList.clear();
        dArrayList.clear();
        pArrayList.clear();
    }

    public static void saveToClotho(MessageConsole mc) {
        //create a collection
        Collection coll = new Collection();
        //get format to create clothoParts in
        //Select Format
        Object[] allNames = Collector.getAllLinksOf(ObjType.FORMAT).toArray();
        ObjLink link = (ObjLink) JOptionPane.showInputDialog(null, "Choose format to save parts in. \n", "Format",
                JOptionPane.INFORMATION_MESSAGE, null, allNames, allNames[0]);

        //Create a new part
        String format;
        if (link != null) {
            format = link.name;
        } else {
            format = "Freeform";
        }

        //iterate over both hash maps, make parts in new format if parts are not freeform.
        //save parts to coll
        Set basicParts = hmClothoBasicParts.entrySet();
        Set compositeParts = hmClothoCompositeParts.entrySet();

        Iterator it = basicParts.iterator();
        while (it.hasNext()) {
            String tag;
            Map.Entry entry = (Map.Entry) it.next();
            org.clothocore.api.data.Part newPart = (org.clothocore.api.data.Part) entry.getValue();
            if (newPart.getSearchTags().get(0) != null) {
                tag = newPart.getSearchTags().get(0);
            } else {
                tag = null;
            }
            NucSeq aseq = newPart.getSeq();
            aseq.setTransient();
            String key = aseq.getSeq() + Format.retrieveByName(format).getUUID();
            String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
            org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
            org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(newPart.getName());
            if ((prexistingName != null) || format.equals("Freeform")) {
                coll.addObject(newPart);
                continue;
            } else if (prexistingSeq != null) {
                if (tag != null) {
                    prexistingSeq.addSearchTag(tag);
                }
                coll.addObject(prexistingSeq);
                continue;
            } else {
                org.clothocore.api.data.Part tempPart = org.clothocore.api.data.Part.generateBasic(newPart.getName(), newPart.getShortDescription(), newPart.getSeq().getSeq(), Format.retrieveByName(format), Collector.getCurrentUser());
                if (tag != null) {
                    tempPart.addSearchTag(tag);
                }
                coll.addObject(tempPart);
            }
        }

        it = compositeParts.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            org.clothocore.api.data.Part newPart = (org.clothocore.api.data.Part) entry.getValue();
            NucSeq aseq = newPart.getSeq();
            aseq.setTransient();
            String key = aseq.getSeq() + Format.retrieveByName(format).getUUID();
            String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
            org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
            org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(newPart.getName());
            if (prexistingName != null) {
                mc.setEnable(true);
                System.out.print("Skipping part " + newPart.getName() + " because name already exists in database.\n");
                mc.setEnable(false);                
                continue;
            } else if (prexistingSeq != null) {
                mc.setEnable(true);
                System.out.print("Skipping part " + newPart.getName() + " because sequence already exists in database.\n");
                mc.setEnable(false);                
                continue;
            } else {
                if (!format.equals("Freeform")) {
                    org.clothocore.api.data.Part tempPart = org.clothocore.api.data.Part.generateComposite(newPart.getCompositeParts(), null, Format.retrieveByName(format), Collector.getCurrentUser(), newPart.getName(), "DefaultDescription");
                    coll.addObject(tempPart);
                } else {
                    coll.addObject(newPart);
                }
            }
        }
        coll.launchDefaultViewer();
    }

    public static void saveToClothoSelection(JList list, MessageConsole mc) {
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

        for (int i = list.getMinSelectionIndex(); i < list.getMaxSelectionIndex() + 1; i++) {
            if (list.isSelectedIndex(i)) {
                if (hmClothoBasicParts.get((String) list.getModel().getElementAt(i)) != null) {
                    org.clothocore.api.data.Part newPart = hmClothoBasicParts.get((String) list.getModel().getElementAt(i));
                    String tag;
                    if (newPart.getSearchTags().get(0) != null) {
                        tag = newPart.getSearchTags().get(0);
                    } else {
                        tag = null;
                    }
                    NucSeq aseq = newPart.getSeq();
                    aseq.setTransient();
                    String key = aseq.getSeq() + Format.retrieveByName(format).getUUID();
                    String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
                    org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
                    org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(newPart.getName());
                    if ((prexistingName != null) || format.equals("Freeform")) {
                        coll.addObject(newPart);
                        continue;
                    } else if (prexistingSeq != null) {
                        if (tag != null) {
                            prexistingSeq.addSearchTag(tag);
                        }
                        coll.addObject(prexistingSeq);
                        continue;
                    } else {
                        org.clothocore.api.data.Part tempPart = org.clothocore.api.data.Part.generateBasic(newPart.getName(), newPart.getShortDescription(), newPart.getSeq().getSeq(), Format.retrieveByName(format), Collector.getCurrentUser());
                        if (tag != null) {
                            tempPart.addSearchTag(tag);
                        }
                        coll.addObject(tempPart);
                    }
                } else if (hmClothoCompositeParts.get((String) list.getModel().getElementAt(i)) != null) {
                    org.clothocore.api.data.Part newPart = hmClothoCompositeParts.get((String) list.getModel().getElementAt(i));
                    NucSeq aseq = newPart.getSeq();
                    aseq.setTransient();
                    String key = aseq.getSeq() + Format.retrieveByName(format).getUUID();
                    String uuidKey = org.clothocore.api.data.Part.generateUUIDAsHash(key);
                    org.clothocore.api.data.Part prexistingSeq = org.clothocore.api.data.Part.retrieveByHash(uuidKey);
                    org.clothocore.api.data.Part prexistingName = org.clothocore.api.data.Part.retrieveByExactName(newPart.getName());
                    if (prexistingName != null) {
                        mc.setEnable(true);
                        System.out.print("Skipping part " + newPart.getName() + " because name already exists in database.\n");
                        mc.setEnable(false);                        
                        continue;
                    } else if (prexistingSeq != null) {
                        mc.setEnable(true);
                        System.out.print("Skipping part " + newPart.getName() + " because sequence already exists in database.\n");
                        mc.setEnable(false);                        
                        continue;
                    } else {
                        if (!format.equals("Freeform")) {
                            org.clothocore.api.data.Part tempPart = org.clothocore.api.data.Part.generateComposite(newPart.getCompositeParts(), null, Format.retrieveByName(format), Collector.getCurrentUser(), newPart.getName(), "DefaultDescription");
                            coll.addObject(tempPart);
                        } else {
                            coll.addObject(newPart);
                        }
                    }
                }
            }
        }
        coll.launchDefaultViewer();
    }

    public static void displayOutputs(JList list, DefaultListModel listModel) {
        //takes hashmaps, displays in lists
        Set basicParts = hmClothoBasicParts.entrySet();
        Set compositeParts = hmClothoCompositeParts.entrySet();
        listModel.clear();
        Iterator it = basicParts.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            org.clothocore.api.data.Part newPart = (org.clothocore.api.data.Part) entry.getValue();
            listModel.addElement(newPart.getName());
        }
        it = compositeParts.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            org.clothocore.api.data.Part newPart = (org.clothocore.api.data.Part) entry.getValue();
            listModel.addElement(newPart.getName());
        }
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        //list.setVisibleRowCount(visibleRowCount);
        /*
         * TO DO: What to do when you select and index 1. bring up name, parts
         * list, and sequence? 2. save only selected elements?
         */
    }

    public static void connect(JList list, DefaultListModel listModel, JTextField textField) {
        hmClothoImportParts.clear();
        listModel.clear();
        ArrayList<ObjLink> allColl = Collector.getAllLinksOf(ObjType.COLLECTION);
        ArrayList<org.clothocore.api.data.Part> parts;
        if (allColl.isEmpty()) {
            return;
        }
        Object[] allNames = allColl.toArray();
        ObjLink link = (ObjLink) JOptionPane.showInputDialog(null,
                "Choose one \n",
                "Collection",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                allNames,
                allNames[0]);
        if (link != null) {
            Collection chosen = Collector.getCollection(link.uuid);
            parts = (ArrayList<org.clothocore.api.data.Part>) chosen.getAll(ObjType.PART);
            textField.setText(chosen.getName());
        } else {
            Object[] options = {"Yes",
                "No",
                "Cancel"};
            int n = JOptionPane.showOptionDialog(null,
                    "Would you like to import all Collections?",
                    "Import all collections",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
            if (n == JOptionPane.YES_OPTION) {
                parts = (ArrayList<org.clothocore.api.data.Part>) Collector.getAll(ObjType.PART);
                textField.setText("All Collections");
            } else {
                return;
            }
        }
        Iterator it = parts.iterator();
        while (it.hasNext()) {
            org.clothocore.api.data.Part nextPart = (org.clothocore.api.data.Part) it.next();
            listModel.addElement(nextPart.getName());
            hmClothoImportParts.put(nextPart.getName(), nextPart);
        }
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);

    }
}
