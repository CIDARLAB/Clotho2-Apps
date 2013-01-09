/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.blasto;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author cassie
 */
public class blastoTask extends SwingWorker<Void, Void> {
    private String query;
    private String subject;
    private JTextArea display;
    
     blastoTask(String inputSeq, JTextArea blastTextArea) {
        this.query = inputSeq;
        this.subject = "";
        this.display = blastTextArea;
     }

     blastoTask(String query, String subject, JTextArea blastTextArea){
        this.query = query;
        this.subject = subject;
        this.display = blastTextArea;
     }
    @Override
    public Void doInBackground() {
        int progress = 0;
        setProgress(progress);
        if (subject.isEmpty()){
            blastoBLAST.blastn(query, display);
        } else {
            blastoBLAST.blast2seq(query, subject, display);
        }
        setProgress(100);
        return null;
    }

    @Override
    protected void done() {
    }
    
}
