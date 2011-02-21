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
 * EditorPanel.java
 *
 * Created on Feb 21, 2011, 10:17:26 AM
 */

package org.clothocad.viewer.plasmideditortc.editor;

import java.awt.BorderLayout;
import org.clothocore.api.data.Plasmid;

/**
 *
 * @author jcanderson_Home
 */
public class EditorPanel extends javax.swing.JPanel {

    /** Creates new form EditorPanel */
    public EditorPanel() {

        initComponents();
        ConstFileContainer.setLayout(new BorderLayout());

    }

    public void setPlasmid(Plasmid aplas) {
        _plas = aplas;
        ConstFileContainer.add(new ConstructionEditor(_plas));
        nameField.setText(aplas.getName());
        partField.setText(aplas.getPart().getName());
        vectorField.setText(aplas.getVector().getName());
        authorField.setText(aplas.getAuthor().getName());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ConstFileContainer = new javax.swing.JPanel();
        nameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        partField = new javax.swing.JTextField();
        vectorField = new javax.swing.JTextField();
        authorField = new javax.swing.JTextField();
        getSeqBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        revertBtn = new javax.swing.JButton();

        ConstFileContainer.setBackground(new java.awt.Color(255, 153, 153));

        javax.swing.GroupLayout ConstFileContainerLayout = new javax.swing.GroupLayout(ConstFileContainer);
        ConstFileContainer.setLayout(ConstFileContainerLayout);
        ConstFileContainerLayout.setHorizontalGroup(
            ConstFileContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        ConstFileContainerLayout.setVerticalGroup(
            ConstFileContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        nameField.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.nameField.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.jLabel4.text")); // NOI18N

        partField.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.partField.text")); // NOI18N

        vectorField.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.vectorField.text")); // NOI18N

        authorField.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.authorField.text")); // NOI18N

        getSeqBtn.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.getSeqBtn.text")); // NOI18N

        saveBtn.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.saveBtn.text")); // NOI18N
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        revertBtn.setText(org.openide.util.NbBundle.getMessage(EditorPanel.class, "EditorPanel.revertBtn.text")); // NOI18N
        revertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(authorField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(vectorField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(partField, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(getSeqBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addComponent(revertBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveBtn))
            .addComponent(ConstFileContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(partField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vectorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(authorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ConstFileContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(getSeqBtn)
                    .addComponent(saveBtn)
                    .addComponent(revertBtn)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void revertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertBtnActionPerformed
        _plas.revert();
    }//GEN-LAST:event_revertBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        _plas.saveDefault();
    }//GEN-LAST:event_saveBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ConstFileContainer;
    private javax.swing.JTextField authorField;
    private javax.swing.JButton getSeqBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField partField;
    private javax.swing.JButton revertBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField vectorField;
    // End of variables declaration//GEN-END:variables
    private Plasmid _plas;
}
