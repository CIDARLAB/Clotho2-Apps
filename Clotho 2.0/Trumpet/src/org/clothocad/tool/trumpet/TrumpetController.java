/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothocad.tool.trumpet;

import org.clothocad.tool.trumpet.results.ResultsPanel;
import org.clothocad.tool.trumpet.invertaseClasses.InvertSim;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import org.clothocad.tool.trumpet.invertaseClasses.LinkSort;
import org.clothocad.tool.trumpet.invertaseClasses.Pancake;
import org.openide.windows.TopComponent;

/**
 *
 * @author Craig LaBoda
 *
 */
public class TrumpetController{


    public TrumpetController(JFrame frame){
        _isTC = true;
            final JComponent guiContentPane = (JComponent) frame.getContentPane();

            final JMenuBar menu = frame.getRootPane().getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Trumpet");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            frame.dispose();

            _numberOfParts = 0;
            _resultsList = new ArrayList<ResultsPanel>(0);
            _currentParts = new ArrayList<String>(0);
    }



    /**
     * Allows the GUI to close on File->Exit
     */
    public void close() {
       if (_isTC) {
           _tcView.close();
       } else {
           _frameView.dispose();
       }
    }


    
    /**
     * Allows this module to switch in and out of top component and embedded
     * view.
     */
     public void switchViews() {
        if (_isTC)
        {
            Component[] components = _tcView.getComponents();
            _frameView = new JFrame();
            _frameView.setContentPane((Container) components[1]);
            _frameView.setJMenuBar((JMenuBar) components[0]);
            _frameView.pack();
            _frameView.setVisible(true);
            _frameView.setTitle("Trumpet");
            _isTC = false;
            _tcView.close();

        } 
        else
        {
            final JComponent guiContentPane = (JComponent) _frameView.getContentPane();

            final JMenuBar menu = _frameView.getJMenuBar();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    _tcView = new TopComponent();
                    _tcView.setLayout(new BorderLayout());
                    JScrollPane sp = new JScrollPane(guiContentPane);
                    _tcView.add(menu, BorderLayout.NORTH);
                    _tcView.add(sp, BorderLayout.CENTER);
                    _tcView.setName("Trumpet");
                    _tcView.open();
                    _tcView.requestActive();

                }
            });
            _frameView.dispose();
            _isTC = true;
        }
    }



    /**
     * Toggles the invertase key trees on and off.
     */
    public void toggleTrees()
    {
        for (int i=0; i<_resultsList.size(); i++)
        {
            _resultsList.get(i).toggleTreeTab();
        }
    }



    /**
     * Toggles the key tables on and off.
     */
    public void toggleKeys()
    {
        for (int i=0; i<_resultsList.size(); i++)
        {
            _resultsList.get(i).toggleKeyTab();
        }
    }



    /**
     * Clears any JList.
     * @param list - the list being cleared
     */
    public void clearList(JList list)
    {
        DefaultListModel emptyList = new DefaultListModel();
        list.setModel(emptyList);
    }



    /**
     * Checks any JList for a particular object
     * @param list - list being checked
     * @param element - element you wish to check for
     * @return
     */
    public boolean containsElement(JList list, Object element)
    {
        boolean isContained = false;

        ListModel currentModel = list.getModel();

        for (int i=0; i<currentModel.getSize(); i++)
        {
            if (((String)currentModel.getElementAt(i)).equals((String)element))
                isContained = true;
        }

        return isContained;
    }



    /**
     * Returns all objects in a list as a string ArrayList
     * @param list - list being examined
     * @return
     */
    public ArrayList<String> getListElements(JList list)
    {
        ArrayList<String> allElements = new ArrayList<String>(0);
        
        ListModel currentModel = list.getModel();
        
        for (int i=0; i<currentModel.getSize(); i++)
        {
            allElements.add((String)currentModel.getElementAt(i));
        }
        
        return allElements;
    }



    /**
     * Determines whether a list is empty
     * @param list - list being examined
     * @return
     */
    public boolean isListEmpty(JList list)
    {
        ListModel currentModel = list.getModel();

        if (currentModel.getSize()==0)
            return true;
        else
            return false;
    }



    /**
     * Appends a set of elements to the given jList.
     * @param list - list which is being modified
     * @param elements - ArrayList of elements being appended
     */
    public void appendElements(JList list, ArrayList<String> elements)
    {
        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // loop through and append the old model's elements to the new model
        for (int i=0; i<currentModel.getSize();i++)
            newModel.addElement(currentModel.getElementAt(i));

        // now add the new elements
        for (int i=0; i<elements.size(); i++)
        {
            newModel.addElement(elements.get(i));
        }

        list.setModel(newModel);
    }



    /**
     * Updates the controller's model field.
     * @param invObj - type of invertSim object being used
     */
    public void setCurrentModel(InvertSim invObj)
    {
        _currentModel = invObj;
    }



    /**
     * Returns the current InvertSim model that Trumpet is using.
     * @return
     */
    public InvertSim getCurrentModel()
    {
        return _currentModel;
    }


    /**
     * Append only a single element to a list
     * @param list - list which is being modified
     * @param element - element that is being appended
     */
    public void appendElement(JList list, String element)
    {
        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // loop through and append the old model's elements to the new model
        for (int i=0; i<currentModel.getSize();i++)
            newModel.addElement(currentModel.getElementAt(i));

        // append the new element
        newModel.addElement(element);

        // set the new model
        list.setModel(newModel);
    }



    /**
     * Append only a single element to a list
     * @param list - list which is being modified
     * @param element - element that is being appended
     */
    public void appendPart(JList list, String element)
    {
        // get the current model
        ListModel currentModel = list.getModel();

        if (!(_currentParts.contains(element)))
        {
            _currentParts.add(element);

            // create a new model
            DefaultListModel newModel = new DefaultListModel();

            // loop through and append the old model's elements to the new model
            for (int i=0; i<currentModel.getSize();i++)
                newModel.addElement(currentModel.getElementAt(i));

            int index = list.getSelectedIndex();
            if (index!=-1)
                newModel.insertElementAt(element, index);
            else
                // append the new element
                newModel.addElement(element);

            list.setModel(newModel);

            renumberParts(list);
        }
        else
        {
            //custom title, warning icon
            JOptionPane.showMessageDialog(new JFrame(),
            "This part has already been added!",
            "Cannot Add to Set of parts",
            JOptionPane.WARNING_MESSAGE);
        }
    }



    /**
     * Specifically used to remove a part from the parts list.
     * @param list -  this should only be the parts list
     */
    public void removeParts(JList list)
    {
        int[] indices = list.getSelectedIndices();

        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // keep track of the current indice index
        int indiceCounter = 0;

        // keeps track of whether to check indices anymore
        boolean done = false;

        // if there is something to delete
        if (indices.length!=0)
        {
            // nameWithP will be used as a temporary string to delete the part
            // from the array of current parts
            String nameWithP = "";
            // deletionIndex will be used for the index of this part
            int deletionIndex = -1;
            // loop through the parts to be deleted
            for (int i=0; i<indices.length; i++)
            {
                // get the part name with Px:
                nameWithP = (String) currentModel.getElementAt(indices[i]);
                //delete the "Px: "
                deletionIndex = nameWithP.indexOf(": ");
                if (deletionIndex!=-1)
                {
                    deletionIndex = deletionIndex+2;
                    String name = nameWithP.substring(deletionIndex,nameWithP.length());

                    // remove the part from the list
                    if (_currentParts.contains(name))
                        _currentParts.remove(name);     
                }
                deletionIndex = -1;
            }



            // loop through and append the old model's elements to the new model
            for (int i=0; i<currentModel.getSize();i++)
            {
                // if all of the elements have not been removed yet
                if (!done)
                {
                    if (i != indices[indiceCounter])
                    {
                        // add the element
                        newModel.addElement(currentModel.getElementAt(i));
                    }
                    else
                    {
                        indiceCounter++;
                        if (indiceCounter == indices.length)
                            done = true;
                    }
                }
                // if all of the elments have been removed
                else
                {
                    // add the element
                    newModel.addElement(currentModel.getElementAt(i));
                }
            }

            // now that the model is correct
            //clear the old list
            clearList(list);

            // set the new model
            list.setModel(newModel);

            // reindex the parts list
            renumberParts(list);

            // we also need to increment the parts
            setNumberOfParts(newModel.getSize());

            // update the model based on
            setCurrentModel(this._currentModel.cloneFresh());
        }
    }



     /**
     * Removes a particular set of elements from a list.
     * @param list - list which is being modified
     * @param indices - indices of the list which need to be removed
     */
    public void removeElements(JList list, boolean reorder)
    {
        int[] indices = list.getSelectedIndices();

        // get the current model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel newModel = new DefaultListModel();

        // keep track of the current indice index
        int indiceCounter = 0;

        // keeps track of whether to check indices anymore
        boolean done = false;


        if (indices.length!=0)
        {
            // loop through and append the old model's elements to the new model
            for (int i=0; i<currentModel.getSize();i++)
            {
                // if all of the elements have not been removed yet
                if (!done)
                {
                    if (i != indices[indiceCounter])
                    {
                        // add the element
                        newModel.addElement(currentModel.getElementAt(i));
                    }
                    else
                    {
                        indiceCounter++;
                        if (indiceCounter == indices.length)
                            done = true;
                    }
                }
                // if all of the elments have been removed
                else
                {
                    // add the element
                    newModel.addElement(currentModel.getElementAt(i));
                }
            }

            // now that the model is correct
            //clear the old list
            clearList(list);
            list.setModel(newModel);

            // if we are dealing with parts, then we need to reorder the parts
            if (reorder)
            {
                // reindex the parts list
                renumberParts(list);
                
                // we also need to increment the parts
                setNumberOfParts(newModel.getSize());

                // update the model based on
                setCurrentModel(this._currentModel.cloneFresh());
            }
        }
    }

    

    /**
     * Renumber the list of parts based on the currentModel
     * @param list - list which is being renumbered, should only be the parts list
     */
    public void renumberParts(JList list)
    {
        // get the current list model
        ListModel currentModel = list.getModel();

        // create a new model
        DefaultListModel nextModel = new DefaultListModel();

        // create a temporary string for holding elements
        String currentElement = "";

        // deletionIndex is used to determine the index of the substrings
        int deletionIndex = -1;

        // loop through the model and make the appropriate adjustments
        for (int i=0; i<currentModel.getSize(); i++)
        {
            // get the entire element
            currentElement = ((String)currentModel.getElementAt(i));

            // delete the prefix and reunumber the part
            deletionIndex = currentElement.indexOf(": ");
            if (deletionIndex!=-1)
            {
                deletionIndex = deletionIndex+2;
                currentElement = "P"+(i+1)+": "+currentElement.substring(deletionIndex,currentElement.length());
            }
            else
            {
                currentElement = "P"+(i+1)+": "+currentElement;
            }
            nextModel.addElement(currentElement); 
        }

        // set the new model
        list.setModel(nextModel);

        // set the number of parts
        setNumberOfParts(nextModel.getSize());
    }



    /**
     * Get the number of parts in the parts list
     * @return - number of parts
     */
    public int getNumberOfParts()
    {
        return _numberOfParts;
    }



    /**
     * Allows the TrumpetGUI to add a part.
     */
    public void incrementNumberOfParts()
    {
        setNumberOfParts(_numberOfParts+1);
    }



    /**
     * Setting the number of parts also updates the current model
     */
    public void setNumberOfParts(int n)
    {
        _numberOfParts = n;
        boolean combos = _currentModel.isCombos();

        // set the currentModel
        String alg = _currentModel.getAlgorithm();
        if (alg.equals("LinkSort"))
            setCurrentModel(new LinkSort(_numberOfParts,combos));
        else if (alg.equals("Pancake"))
            setCurrentModel(new Pancake(_numberOfParts,combos));
    }



    /**
     * Allows the TrumpetGUI to decrement the number of parts
     */
    public void decrementNumberOfParts()
    {
        setNumberOfParts(_numberOfParts-1);
    }



    /**
     * Adds a panel to the current array list of result panels.
     * @param result - new result panel
     */
    public void addResultPanel(ResultsPanel result)
    {
        _resultsList.add(result);
    }


    /**
     * Retrieve a result panel
     * @param index - index of the panel in the arraylist of results
     * @return
     */
    public ResultsPanel getResultPanel(int index)
    {
        return _resultsList.get(index);
    }



    /**
     * Set the given index to a new result panel.
     * @param index - index of result being set
     * @param results - new result panel
     */
    public void setResultPanel(int index, ResultsPanel results)
    {
        _resultsList.set(index, results);
    }



    /**
     * Removes the result panel from the arraylist of result panels.
     * @param index - index of the panel being removed
     */
    public void removeResult(int index)
    {
        _resultsList.remove(index);
    }



    //\\//\\//\\//\\//\\//\\//\\ FIEDLS //\\//\\//\\//\\//\\//\\//\\//\\//\\//\\

    private int _numberOfParts;
        // keeps track of the current number of parts
    private ArrayList<String> _currentParts;
        // keeps track of which parts are currently in the part set
    private InvertSim _currentModel;
        // current invertSim model being implemented
    private ArrayList<ResultsPanel> _resultsList;
        // contains all of the result panels so that they can be adjusted
    private TopComponent _tcView;
        // used for the top component view
    private JFrame _frameView;
        // used for the frame view
    private boolean _isTC;
        // determines whether we are in the top component view or the frame view
}
