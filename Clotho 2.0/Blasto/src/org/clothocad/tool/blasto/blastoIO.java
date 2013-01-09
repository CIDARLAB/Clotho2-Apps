/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.blasto;

import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Collection;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Part;

/**
 *
 * @author cassie
 */
// for opening and displaying sequence files and clotho part sequences
// also for exporting BLAST results
public class blastoIO {

    public static void importFromClotho(JTextArea seqTextArea, JTextField seqNameTextField) {
        //connect to database, select collection, select part from collection
        ArrayList<ObjLink> allColl = Collector.getAllLinksOf(ObjType.COLLECTION);
        ArrayList<org.clothocore.api.data.Part> allParts;
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
            allParts = (ArrayList<org.clothocore.api.data.Part>) chosen.getAll(ObjType.PART);
            Object[] parts = allParts.toArray();
            Part part = (Part) JOptionPane.showInputDialog(null,
                "Choose one \n",
                "Collection",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                parts,
                parts[0]);
            if (part != null) {
                seqNameTextField.setText(part.getName());
                seqTextArea.setText(part.getSeq().getSeq());
            }
        }
    }

    public static void importFromFile(File file, JTextArea seqTextArea, JTextField seqNameTextField) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            seqTextArea.setText("");
            String str;
            String newString = "";
            while ((str = in.readLine()) != null) {
                //topPane.append(str + "\n");
                newString += str;
                newString += "\n";
            }
            in.close();
            seqTextArea.setText(newString);
        } catch (IOException exception) {
        }
        seqNameTextField.setText(file.getName());
    }

    public static void exportToFile(File file, JTextArea blastTextArea) {
        try {
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(blastTextArea.getText());
            out.close();
        } catch (Exception exception) {
        }
    }
}
