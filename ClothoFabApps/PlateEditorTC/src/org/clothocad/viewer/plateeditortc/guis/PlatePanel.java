/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.viewer.plateeditortc.guis;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.clothocore.api.data.Container;
import org.clothocore.api.data.Plate;

/**
 *
 * @author jcanderson
 */
public class PlatePanel extends JPanel {

    public PlatePanel(Plate aplate) {
        setLayout(new GridLayout(aplate.getNumRows(),aplate.getNumCols() ));

        for(int row = 0; row<aplate.getNumRows(); row++) {
            for(int col = 0; col<aplate.getNumCols(); col++) {
                Container acon = aplate.getContainerAt(row, col);
                WellPanel awell = new WellPanel(acon);
                add(awell);
            }
        }
    }

}
