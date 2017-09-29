package Moduls.MyPlayerVideo;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import utilities.TextUtilities;

/**
 *
 * @author Ondřej Bleha
 */
public class SearchFieldListener implements DocumentListener {

  private final JList list;
  private DefaultListModel listModel;

  private String text;

  public SearchFieldListener(JList ls) {
    this.list = ls;
    this.listModel = new DefaultListModel();
    text = "";

    this.listModel.addListDataListener(new ListDataListener() {
      @Override
      public void intervalAdded(ListDataEvent e) {
        updateList();
      }

      @Override
      public void intervalRemoved(ListDataEvent e) {
        updateList();
      }

      @Override
      public void contentsChanged(ListDataEvent e) {
        updateList();
      }
    });

  }

  protected void updateFilter(Document doc) throws BadLocationException {
    this.text = doc.getText(0, doc.getLength());
    updateList();
  }

  public synchronized void updateList() {
    DefaultListModel newModel = new DefaultListModel();
    for (int i = 0; i < listModel.getSize(); i++) {
      String v = (String) listModel.getElementAt(i);
      if (text != null) {
        if (TextUtilities.normalize(v).contains(TextUtilities.normalize(this.text))) {
          newModel.addElement(listModel.elementAt(i));
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

  public void setModel(DefaultListModel listModel) {
    this.listModel = listModel;
  }

}
