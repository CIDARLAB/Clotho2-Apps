/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.plateeditortc.guis;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.clothocore.api.data.Container;
import org.clothocore.api.data.Sample;

/**
 *
 * @author jcanderson
 */
class WellPanel extends JPanel {

    WellPanel(Container acon) {
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                setBackground(Color.gray);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        _con = acon;
        if(acon==null) {
            setBackground(Color.RED);
            return;
        }
        _sam = acon.getSample();
        if(_sam==null) {
            setBackground(Color.YELLOW);
            return;
        }
        fillInWell();
    }

    private void fillInWell() {
        setBackground(Color.GREEN);
        JLabel label = new JLabel(_sam.getName());
        add(label);
    }

    private Container _con;
    private Sample _sam;
}
