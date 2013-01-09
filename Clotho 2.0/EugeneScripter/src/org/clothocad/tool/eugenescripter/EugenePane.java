/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.eugenescripter;

import javax.swing.JEditorPane;

/**
 *
 * @author cassie
 */
public class EugenePane extends JEditorPane{
    
    protected boolean hasClothoImport;
    
    public void EugenePane(){
        hasClothoImport = false;
    }
    
}