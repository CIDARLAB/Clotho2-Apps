/*
 * 
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

/*
 * CherryPicker.java
 *
 * Created on Mar 29, 2011, 7:46:36 AM
 */

package org.clothocad.tool.biomekcompanion;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.clothocore.api.core.Collector;
import org.clothocore.api.data.Container;
import org.clothocore.api.data.ObjLink;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Plate;
import org.clothocore.api.data.Sample;

/**
 *
 * @author jcanderson_Home
 */
public class CherryPicker extends javax.swing.JFrame {

    /** Creates new form CherryPicker */
    public CherryPicker() {
        initComponents();
        validationMessage.setText("");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        runIt = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        commandArea = new javax.swing.JTextArea();
        addPlate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        platepos = new javax.swing.JTextArea();
        clearAll = new javax.swing.JButton();
        validateBtn = new javax.swing.JButton();
        validationMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        runIt.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.runIt.text")); // NOI18N
        runIt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runItActionPerformed(evt);
            }
        });

        commandArea.setColumns(20);
        commandArea.setRows(5);
        commandArea.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.commandArea.text")); // NOI18N
        jScrollPane1.setViewportView(commandArea);

        addPlate.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.addPlate.text")); // NOI18N
        addPlate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlateActionPerformed(evt);
            }
        });

        platepos.setColumns(20);
        platepos.setEditable(false);
        platepos.setRows(5);
        jScrollPane2.setViewportView(platepos);

        clearAll.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.clearAll.text")); // NOI18N
        clearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllActionPerformed(evt);
            }
        });

        validateBtn.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.validateBtn.text")); // NOI18N
        validateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateBtnActionPerformed(evt);
            }
        });

        validationMessage.setText(org.openide.util.NbBundle.getMessage(CherryPicker.class, "CherryPicker.validationMessage.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(validationMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(clearAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(validateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(addPlate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runIt))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(validationMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runIt)
                    .addComponent(addPlate)
                    .addComponent(clearAll)
                    .addComponent(validateBtn)))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPlateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlateActionPerformed
        try {
            //Throw up a dialog and get user to select the collection stored as 'chosen'
            ArrayList<ObjLink> allColl = Collector.getAllLinksOf(ObjType.PLATE);
            if(allColl.isEmpty()) {
                return;
            }
            Object[] allNames = allColl.toArray();
            ObjLink link = (ObjLink) JOptionPane.showInputDialog(null, "Choose plate", "Plate",
                JOptionPane.INFORMATION_MESSAGE, null, allNames, allNames[0]);
            Plate chosen = Collector.getPlate(link.uuid);

            //Thow up a dialog to get the plate deck position for the plate
            String pos  = JOptionPane.showInputDialog( "Type in the Deck position (eg P1, P2, P3...)" );
            plateMapping.put(pos, chosen);

            StringBuffer sb = new StringBuffer();
            for(String str : plateMapping.keySet()) {
                sb.append(str);
                sb.append("\t");
                sb.append(plateMapping.get(str).getName());
                sb.append("\n");
            }
            platepos.setText(sb.toString());
        } catch(Exception err) {
            err.printStackTrace();
        }
    }//GEN-LAST:event_addPlateActionPerformed

    private void runItActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runItActionPerformed
        try {
            String cmds = commandArea.getText();
            String[] lines = cmds.split("\\r?\\n");

            //Create the steps
            ArrayList<Step> steps = new ArrayList<Step>();
            for(String aline : lines) {
                String[] parsed = aline.split("\\,");
                String vol = "3";
                if(parsed.length>4) {
                    vol = parsed[4];
                }
                Step astep = new Step(parsed[0],parsed[1],parsed[2],parsed[3],vol);
                if(!astep.validate()) {
                    System.out.println("There's an error here!!!");
                    return;
                }
                steps.add(astep);
            }

            //Run each step
            for(Step astep : steps) {
                astep.execute();
            }
        } catch(Exception err) {
        }
    }//GEN-LAST:event_runItActionPerformed


    private class Step {
        //VARIABLES//
        Plate fromPlate;
        Plate toPlate;
        int fromRow=-1;
        int fromCol=-1;
        int toRow=-1;
        int toCol=-1;
        double volume=3;

        public Step(String frompos, String fromwell, String topos, String towell, String vol) {
            try {
                fromPlate = plateMapping.get(frompos);
                toPlate = plateMapping.get(topos);

                //Convert the first letter to a row index
                char frow = fromwell.charAt(0);
                int a=frow;
                fromRow = a-65;
                char trow = towell.charAt(0);
                a = trow;
                toRow = a-65;

                //Get the columns
                String fcol = fromwell.substring(1);
                fromCol = Integer.parseInt(fcol)-1;
                String tcol = towell.substring(1);
                toCol = Integer.parseInt(tcol)-1;

                if(vol!=null) {
                    volume = Integer.parseInt(vol);
                }

                System.out.println("The wells are " + fromRow + ", " + fromCol + "; " + toRow  + ", " + toCol+ ": " + volume);

            } catch(Exception err) {
                System.out.println("I had a bad step");
            }
        }

        /**
         * Run the step (move the liquid)
         * @return true if worked, false if failed
         */
        public boolean execute() {
            try {
                Container fromcon = fromPlate.getContainerAt(fromRow, fromCol);
                Sample fromsam = fromcon.getSample();
                Container tocon = toPlate.getContainerAt(toRow, toCol);
                fromsam.transferAnAliquot(tocon, volume);

            } catch(Exception err) {
                return false;
            }
            return true;
        }

        public boolean validate() {
            if(fromPlate==null) {
                return false;
            }
            if(toPlate==null) {
                return false;
            }
            if(fromRow<0) {
                return false;
            }
            if(fromCol<0) {
                return false;
            }
            if(toRow<0) {
                return false;
            }
            if(toCol<0) {
                return false;
            }
            return true;
        }
    }
    private void clearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllActionPerformed
        platepos.setText("");
        plateMapping.clear();
        commandArea.setText("");
    }//GEN-LAST:event_clearAllActionPerformed

    private void validateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateBtnActionPerformed
        boolean isOK = true;
        try {
            String cmds = commandArea.getText();
            String[] lines = cmds.split("\\r?\\n");

            for(String aline : lines) {
                String[] parsed = aline.split("\\,");
                String vol = "3";
                if(parsed.length>4) {
                    vol = parsed[4];
                }
                Step astep = new Step(parsed[0],parsed[1],parsed[2],parsed[3],vol);
                if(!astep.validate()) {
                    isOK=false;
                }
            }
        } catch(Exception err) {
            isOK = false;
        }
        if(isOK) {
            validationMessage.setText("The information is OK");
        } else  {
            validationMessage.setText("!!!There is an error!!!");
        }
    }//GEN-LAST:event_validateBtnActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CherryPicker().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPlate;
    private javax.swing.JButton clearAll;
    private javax.swing.JTextArea commandArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea platepos;
    private javax.swing.JButton runIt;
    private javax.swing.JButton validateBtn;
    private javax.swing.JLabel validationMessage;
    // End of variables declaration//GEN-END:variables

    private HashMap<String, Plate> plateMapping = new HashMap<String, Plate>();
}