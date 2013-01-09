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
import javax.swing.JList;
import javax.swing.text.*;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.openide.util.Exceptions;
import eugene.EugeneExecutor;
import eugene.parser.EugeneLexer;
import eugene.parser.EugeneParser;
import eugene.dom.SavableElement;
import eugene.output.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.clothocore.api.data.Part;

public class eugeneOps {

    public static boolean runEugene(String code, MessageConsole mc) throws IOException {
        mc.setEnable(true);
        System.out.print("\nRunning Eugene\n");
        if (!code.equals("") && code != null) {
            HashMap<String, SavableElement> results = new HashMap<String, SavableElement>();

            //Write code to a file
/*
             * String newFileName = System.getProperty("java.io.tmpdir") +
             * "/tmpIn.eug"; File eugFile = new File(newFileName); Writer output
             * = new BufferedWriter(new FileWriter(eugFile));
             * output.write(code); output.close();
             */
            try {         
                results = (HashMap<String, SavableElement>) EugeneExecutor.execute(code, 2);
            } catch (RecognitionException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                mc.setEnable(false);
            }

            clothoOps.decomposeEugene(results, mc);

            results.clear();
            results = null;
        }
// insert info window here
        return true;
    }

    public static void addClothoPart(JList list, int index, EugenePane editorPane, MessageConsole mc) {
        Document doc = editorPane.getDocument();
        Element e = doc.getDefaultRootElement();
        AttributeSet attr = e.getAttributes().copyAttributes();
        Part newPart = getPartFromList(list, index);
        if (newPart.getPartType() == org.clothocore.api.data.Part.partType.Basic) {
            addPartCode(newPart, editorPane, doc, attr, mc);
        } else {
            ArrayList<org.clothocore.api.data.Part> subparts = (ArrayList<org.clothocore.api.data.Part>) newPart.getCompositeParts();
            String compParts = "";
            for (int i = 0; i < subparts.size(); i++) {
                addPartCode(subparts.get(i), editorPane, doc, attr, mc);
                compParts = compParts + subparts.get(i).getName().replace(" ", "_") + ",";
            }
            String partName = newPart.getName().replace(" ", "_");

            String declDevice = "Device " + partName + "(" + compParts.substring(0, compParts.length() - 1) + ")";
            if (!editorPane.getText().contains(partName)) {
                try {
                    doc.insertString(doc.getLength(), declDevice, attr);
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } else {
                mc.setEnable(true);
                System.out.println(partName + " has already been declared in this file.");
                mc.setEnable(false);
            }

        }

    }

    public static void addPartCode(Part inputPart, EugenePane editorPane, Document doc, AttributeSet attr, MessageConsole mc) {
        String partType = getPartType(inputPart);
        if (partType == null) {
            return;
        }
        String propName = "Property name(txt);";
        String propSeq = "Property sequence(txt);";
        String propSD = "Property description(txt);";
        String partDecl = "Part " + partType + "(name, sequence, description);\n";
        try {
            if (!editorPane.getText().contains(propName)) {
                doc.insertString(doc.getLength(), propName + "\n", attr);
            }
            if (!editorPane.getText().contains(propSeq)) {
                doc.insertString(doc.getLength(), propSeq + "\n", attr);
            }
            if (!editorPane.getText().contains(propSD)) {
                doc.insertString(doc.getLength(), propSD + "\n", attr);
            }
            if (!editorPane.getText().contains(partDecl)) {
                doc.insertString(doc.getLength(), partDecl, attr);
            }
            String partName = inputPart.getName().replace(" ", "_");
            String sd = inputPart.getShortDescription();
            String seq = inputPart.getSeq().getSeq();
            String newPart = partType + " " + partName + "(\"" + partName + "\", \"" + seq + "\", \"" + sd + "\");\n";

            if (!editorPane.getText().contains(partName)) {
                doc.insertString(doc.getLength(), newPart, attr);
            } else {
                mc.setEnable(true);
                System.out.println(partName + " has already been declared in this file.");
                mc.setEnable(false);
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static Part getPartFromList(JList list, int index) {
        String partName = (String) list.getModel().getElementAt(index);
        partName.replace(" ", "_");
        org.clothocore.api.data.Part selPart = clothoOps.hmClothoImportParts.get(partName);
        //check clotho import first.  else check sbol import
        if (selPart == null) {
            selPart = sbolOps.hmSBOLImportParts.get(partName);
        }
        return selPart;
    }

    public static String getPartType(Part inputPart) {
        if (inputPart.hasSearchTag("promoter") || inputPart.hasSearchTag("Promoter")) {
            return "promoter";
        } else if (inputPart.hasSearchTag("RBS") || inputPart.hasSearchTag("rbs")) {
            return "RBS";
        } else if (inputPart.hasSearchTag("CDS") || inputPart.hasSearchTag("cds")) {
            return "CDS";
        } else if (inputPart.hasSearchTag("terminator") || inputPart.hasSearchTag("Terminator")) {
            return "terminator";
        } else if (inputPart.hasSearchTag("recombinase_f") || inputPart.hasSearchTag("Recombinase_f")) {
            return "recombinase_f";
        } else if (inputPart.hasSearchTag("recombinase_r") || inputPart.hasSearchTag("Recombinase_r")) {
            return "recombinase_r";
        }
        Object[] possibilities = {"promoter", "RBS", "CDS", "terminator", "recombinase_f", "recombinase_r"};
        String s = (String) JOptionPane.showInputDialog(
                null,
                "Enter the Part Type of " + inputPart.getName() + ":\n",
                "Declare Part Type of " + inputPart.getName(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "clothoPart");
        return s;
    }
}
