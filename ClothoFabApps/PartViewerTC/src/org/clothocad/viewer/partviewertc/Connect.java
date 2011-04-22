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
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

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
        final PartImagePanel pp = new PartImagePanel(apart);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(_stc==null) {
                    _stc = new ImageWindowTopComponent();
                    Mode m = WindowManager.getDefault().findMode("properties");
                    if(m!=null) {
                        m.dockInto(_stc);
                    }
                    _stc.setName("Part: " + apart.getName());
                    _stc.add(pp, BorderLayout.CENTER);
                    _stc.open();
                    _stc.requestActive();
                } else {
                    _stc.setName("Part: " + apart.getName());
                    _stc.removeAll();
                    _stc.add(pp, BorderLayout.CENTER);
                    _stc.validate();
                    _stc.open();
                    _stc.requestActive();
                }

                _stc.open();
                _stc.requestActive();
            }
        });

    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }

    ImageWindowTopComponent _stc;
}
