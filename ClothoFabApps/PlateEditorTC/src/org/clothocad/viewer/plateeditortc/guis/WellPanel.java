/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.plateeditortc.guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.clothocore.api.data.Container;
import org.clothocore.api.data.ObjBase;
import org.clothocore.api.data.Oligo;
import org.clothocore.api.data.OligoSample;
import org.clothocore.api.data.Person;
import org.clothocore.api.data.Sample;
import org.clothocore.api.dnd.ObjBaseObserver;
import org.clothocore.api.dnd.RefreshEvent;
import org.clothocore.util.basic.ObjBasePopup;

/**
 *
 * @author jcanderson
 */
public class WellPanel extends JPanel {

    WellPanel(Container acon) {
        setBorder(raisedetched);
        setLayout(new BorderLayout());
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getModifiers()==4) {
                    rightClickPressed(e.getPoint());
                    return;
                } else if(e.getClickCount()==2) {
                    doubleClickPressed();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEnteredEvent(e.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseExitedEvent();
            }

        });

        _con = acon;

        preinit();
        init();

    }

    private void preinit() {
        //Observe the Container
        _conobo = new ObjBaseObserver() {
            @Override
            public void update(ObjBase obj, RefreshEvent evt) {
                init();
            }
        };
        _con.isObservedBy(_conobo);
        _sam = _con.getSample();
        if(_sam==null) {
            return;
        }

        //Observe the Sample
        _samobo = new ObjBaseObserver() {
            @Override
            public void update(ObjBase obj, RefreshEvent evt) {
                init();
            }
        };
        _sam.isObservedBy(_samobo);
    }

    private void init() {
        System.out.println("WellPanel init called");

        if(_con==null) {
            setBackground(Color.WHITE);
            return;
        }

        _sam = _con.getSample();
        if(_sam==null) {
            removeAll();
            validate();
            repaint();
            setBackground(Color.gray);
            return;
        }
        fillInWell();
    }

    private void fillInWell() {
        removeAll();
        validate();
        repaint();
        int quality = _sam.getQuality();
        setBackground(colors[quality]);
        JLabel well = new JLabel(_con.getWell());
        add(well, BorderLayout.NORTH);
        JLabel label = new JLabel(_sam.getName());
        add(label, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private void rightClickPressed(Point point) {
        ObjBase ob = null;
        if(_con==null) {
            return;
        } else if(_sam==null) {
            ob = _con;
        } else {
            ob = _sam;
        }

        ObjBasePopup obp = new ObjBasePopup(this, ob, point);
    }
    
    private void doubleClickPressed() {
        if(_con==null) {
            createContainer();
            return;
        } else if(_sam==null) {
            createSample();
            return;
        } else {
            _sam.launchDefaultViewer();
        }
    }

    private void createContainer() {
        System.out.println("Create a new container called");
    }

    private void createSample() {
        AddSampleDialog asd = new AddSampleDialog(this);
        asd.setVisible(true);
    }


    void receiveSample(AddSampleDialog asd) {
        if(asd.canceled) {
            return;
        }
        Oligo anoligo = Oligo.retrieveByName(asd.oligoName);
        if(anoligo==null) {
            return;
        }
        Double vol;
        try {
            vol = Double.parseDouble(asd.volume);
        }catch(Exception e) {
            return;
        }
        Person auth = Person.retrieveByName(asd.authorName);
        if(auth==null) {
            return;
        }
        Sample asam = OligoSample.generateOligoSample( anoligo, _con, vol, auth );
        if(asam!=null) {
            _sam = asam;
        }
        init();
    }
    
    private void mouseEnteredEvent(Point point) {
        ObjBase ob = null;
        if(_con==null) {
            return;
        } else if(_sam==null) {
            ob = _con;
        } else {
            ob = _sam;
        }
        System.out.println("mouse entered");
    }

    private void mouseExitedEvent() {
        ObjBase ob = null;
        if(_con==null) {
            return;
        } else if(_sam==null) {
            ob = _con;
        } else {
            ob = _sam;
        }
        System.out.println("mouse exited");
    }
///////////////////////////////////////////////////////////////////
////                      private variables                    ////
    private Container _con;
    private Sample _sam;
    private ObjBaseObserver _conobo;
    private ObjBaseObserver _samobo;

    private static Color[] colors = new Color[5];
    private static Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    static {
        colors[0] = new Color(226,113,135);
        colors[1] = new Color(132,162,175);
        colors[2] = new Color(226,221,118);
        colors[3] = new Color(156,206,110);
        colors[4] = new Color(156,206,110);
    }

}
