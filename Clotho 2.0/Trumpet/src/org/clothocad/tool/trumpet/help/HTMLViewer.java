/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.trumpet.help;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Original Unkown
 * @author Edits by Craig LaBoda
 *
 */
public class HTMLViewer extends JDialog implements HyperlinkListener {
  private JEditorPane htmlPane;

  public HTMLViewer(String name, JEditorPane pane)
  {
      super(new JFrame(), name);
      this.htmlPane = pane;
       //Put the editor pane in a scroll pane.
        JScrollPane editorScrollPane = new JScrollPane(htmlPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));

        htmlPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent hev) {
            try {
            if (hev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                htmlPane.setPage(hev.getURL());
            }
            catch (IOException e) {
            // Exceptions thrown...............
            }
        }});

        this.add(editorScrollPane);
  }


  public JEditorPane getPane()
   {
      return htmlPane;
  }

  public void hyperlinkUpdate(HyperlinkEvent event) {
  
      if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      try {
          String s = event.toString();
          if (s.startsWith("http"))
          {
            try
            {
//                Desktop.getDesktop().browse(event.getURL().toURI());
                Runtime rt = Runtime.getRuntime();
                // this doesn't support showing urls in the form of "page.html#nameLink"
	        rt.exec( "rundll32 url.dll,FileProtocolHandler " + event.getURL());
            }
            catch (Throwable e)
            {
                JOptionPane.showMessageDialog(null, "Sorry can't launch a browser.");
            }
            }
            else
            {
                htmlPane.setPage(event.getURL());
            }
        } catch(IOException ioe) {
        System.err.println("Could not find: "+event.getURL());
      }
    }


  }
}