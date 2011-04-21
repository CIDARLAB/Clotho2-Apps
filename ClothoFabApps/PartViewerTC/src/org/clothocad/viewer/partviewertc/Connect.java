/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.partviewertc;

import org.clothocore.api.data.ObjBase;
import org.clothocore.api.plugin.ClothoViewer;

/**
 *
 * @author jcanderson
 */
public class Connect implements ClothoViewer {

    @Override
    public void launch(ObjBase o) {
        System.out.println(o.getName());
    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }

}
