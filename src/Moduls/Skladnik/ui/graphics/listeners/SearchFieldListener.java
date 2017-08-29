package Moduls.Skladnik.ui.graphics.listeners;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.Kategorie;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import utilities.TextUtilities;

/**
 *
 * @author Ondřej Bleha
 */
public class SearchFieldListener implements DocumentListener {
    private final JList list;
    private DefaultListModel<Box> listModel;
    
    private final JTree tree;
    private final Kategorie kategorie;
    
    private String text;

    public SearchFieldListener(JList list, JTree tree, Kategorie kategorie) {
        this.list = list;
        this.listModel = (DefaultListModel) list.getModel();
        this.tree = tree;
        this.kategorie = kategorie;
        
        text = "";
        
        this.listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {updateList();}

            @Override
            public void intervalRemoved(ListDataEvent e) {updateList();}

            @Override
            public void contentsChanged(ListDataEvent e) {updateList();}
        });
        
        this.tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {updateList();}
        });
    }
    
    protected void updateFilter(Document doc) throws BadLocationException {
        this.text = doc.getText(0, doc.getLength());
        updateList();
    }
    
    public void updateList(){
        DefaultListModel newModel = new DefaultListModel();
        for (int i = 0; i < listModel.getSize(); i++) {            
            Box box = listModel.getElementAt(i);
            if(text != null){
                if(TextUtilities.normalize(box.getInfo()).contains(TextUtilities.normalize(this.text))){
                    if(kategorie.isPodkategorie(box.getKategorie(), (DefaultMutableTreeNode) (tree.getSelectionPath().getLastPathComponent()))){
                        newModel.addElement(listModel.elementAt(i));
                    }
                }
            }
        }
        list.setModel(newModel);
    }
    
    @Override
    public void insertUpdate(DocumentEvent evt) {
        try {
            updateFilter(evt.getDocument());
        } catch (BadLocationException ex) {
            Logger.getLogger(SearchFieldListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent evt) {
        try {
            updateFilter(evt.getDocument());
        } catch (BadLocationException ex) {
            Logger.getLogger(SearchFieldListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //do nothing
    }

  public void setModel(DefaultListModel<Box> listModel) {
    this.listModel = listModel;
  }

}
