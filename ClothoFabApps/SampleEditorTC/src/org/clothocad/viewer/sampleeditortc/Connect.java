/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.sampleeditortc;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.clothocad.viewer.sampleeditortc.guis.OligoSamplePanel;
import org.clothocad.viewer.sampleeditortc.guis.PlasmidSamplePanel;
import org.clothocad.viewer.sampleeditortc.guis.StrainSamplePanel;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.ObjType;
import org.clothocore.api.data.OligoSample;
import org.clothocore.api.data.PlasmidSample;
import org.clothocore.api.data.Sample;
import org.clothocore.api.data.StrainSample;
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
        if(!o.getType().equals(ObjType.SAMPLE)) {
            return;
        }
        final Sample asam = (Sample) o;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JPanel pp = null;
                switch(asam.getSampleType()) {
                    case OLIGO_SAMPLE:
                        pp = new OligoSamplePanel((OligoSample) asam);
                        break;
                    case PLASMID_SAMPLE:
                        pp = new PlasmidSamplePanel((PlasmidSample) asam);
                        break;
                    case STRAIN_SAMPLE:
                        pp = new StrainSamplePanel((StrainSample) asam);
                        break;
                    default:
                        return;
                }

                if(_stc==null) {
                    _stc = new SampleTopComponent();
                    Mode m = WindowManager.getDefault().findMode("properties");
                    if(m!=null) {
                        m.dockInto(_stc);
                    }
                    _stc.setName("Sample: " + asam.getName());
                    _stc.add(pp, BorderLayout.CENTER);
                    _stc.open();
                    _stc.requestActive();
                } else {
                    _stc.setName("Sample: " + asam.getName());
                    _stc.removeAll();
                    _stc.add(pp, BorderLayout.CENTER);
                    _stc.validate();
                    _stc.open();
                    _stc.requestActive();
                }
            }
        });

    }

    @Override
    public void close() {
    }

    @Override
    public void init() {
    }
///////////////////////////////////////////////////////////////////
////                      private variables                    ////
    SampleTopComponent _stc;

}
