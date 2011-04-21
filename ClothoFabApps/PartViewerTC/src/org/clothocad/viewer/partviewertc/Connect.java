/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.partviewertc;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import org.clothocad.viewer.partviewertc.panels.PartImagePanel;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.Part;
import org.clothocore.api.plugin.ClothoViewer;

/**
 *
 * @author jcanderson
 */
public class Connect implements ClothoViewer {

    @Override
    public void launch(ObjBase o) {
        if(!o.getType().equals(ObjType.PART)) {
            return;
        }
        final Part apart = (Part) o;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ImageWindowTopComponent ptc = new ImageWindowTopComponent();
                ptc.setName("Part: " + apart.getName());
                PartImagePanel pp = new PartImagePanel(apart);
                ptc.add(pp, BorderLayout.CENTER);

//                HeaderPanel hp = new HeaderPanel(aplate);
//                ptc.add(hp, BorderLayout.NORTH);

                ptc.open();
                ptc.requestActive();
            }
        });

    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }

}
