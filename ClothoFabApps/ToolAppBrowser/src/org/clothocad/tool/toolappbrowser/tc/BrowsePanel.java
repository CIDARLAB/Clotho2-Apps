/*
 * 
Copyright (c) 2010 The Regents of the University of California.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS..
 */

package org.clothocad.tool.toolappbrowser.tc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.clothocore.api.core.Collator;
import org.clothocore.api.core.wrapper.ToolWrapper;
import org.clothocore.util.buttons.TransparentButton;

/**
 *
 * @author jcanderson_Home
 */
public class BrowsePanel extends JPanel {

    public BrowsePanel() {
        FlowLayout lay = new FlowLayout();
        lay.setAlignment(FlowLayout.LEFT);
        setLayout(lay);
        ArrayList<ToolWrapper> listy = Collator.getAllTools();
        for(ToolWrapper tw: listy) {
            ToolComponent button = new ToolComponent(tw);
            add(button);
        }
    }

    private class ToolComponent extends JPanel {
        public ToolComponent(final ToolWrapper tw) {
            setLayout(null);
            setPreferredSize(new Dimension(200,150));
            setBackground(Color.LIGHT_GRAY);
            ImageIcon img = tw.getIcon(120);
            TransparentButton button = new TransparentButton(img);
            button.setExitAlpha(1.0f);
            button.setEnterAlpha(0.7f);
            int xpos = (200-img.getIconWidth())/2;
            button.setBounds(30,15,img.getIconWidth(),img.getIconHeight());
            add(button);

            button.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()==2) {
                        tw.launchTool();
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
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                      private variables                    ////
}
