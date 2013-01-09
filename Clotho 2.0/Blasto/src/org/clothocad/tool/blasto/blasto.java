/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clothocad.tool.blasto;

import java.awt.Window;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.plugin.ClothoTool;


public class blasto implements ClothoTool {

       public void launch() {
        blastoGUI frame = new blastoGUI();
        guis.add(new WeakReference<Window>(frame));
    }


    public void launch(ObjBase o) {
    }

    public void close() {
        for(WeakReference<Window> wrw: guis) {
            Window w = wrw.get();
            if(w!=null) {
                w.dispose();
            }
        }
    }

    public void init() {
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    private ArrayList<WeakReference<Window>> guis = new ArrayList<WeakReference<Window>>();
}

