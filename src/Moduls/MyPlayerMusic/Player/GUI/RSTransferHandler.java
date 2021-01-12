/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.RandomSong;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 *
 * @author vojta3310
 */
public class RSTransferHandler extends TransferHandler {

  private int indexF = 0;
  private int indexT = 0;
  private RandomSong data;

  @Override
  public boolean canImport(TransferHandler.TransferSupport support) {
    // for the demo, we will only support drops (not clipboard paste)
    if (!support.isDrop()) {
      return false;
    }

    support.setDropAction(support.getSourceDropActions());
    return support.isDataFlavorSupported(TransferableRS.RSFlavor);

  }

  @Override
  public boolean importData(TransferHandler.TransferSupport support) {
    // if we cannot handle the import, say so
    if (!canImport(support)) {
      return false;
    }

    // fetch the drop location
    JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

    indexT = dl.getIndex();

    // fetch the data and bail if this fails
    try {
      data = (RandomSong) support.getTransferable().getTransferData(TransferableRS.RSFlavor);
    } catch (UnsupportedFlavorException | java.io.IOException e) {
      return false;
    }

    JList list = (JList) support.getComponent();
    DefaultListModel model = (DefaultListModel) list.getModel();
    if (indexT >= model.getSize()) {
      return false;
    }
    model.insertElementAt(data, indexT);

    Rectangle rect = list.getCellBounds(indexT, indexT);
    list.scrollRectToVisible(rect);
    //list.setSelectedIndex(indx);
    list.requestFocusInWindow();

    return true;
  }

  @Override
  public int getSourceActions(JComponent comp) {
    return MOVE;
  }

  @Override
  public Transferable createTransferable(JComponent comp) {
    JList dragFrom = (JList) comp;
    indexF = dragFrom.getSelectedIndex();
    if (indexF < 0 || indexF >= dragFrom.getModel().getSize() - 1) {
      return null;
    }

    return new TransferableRS((RandomSong) ((DefaultListModel) dragFrom.getModel()).elementAt(dragFrom.getSelectedIndex()));
  }

  @Override
  public void exportDone(JComponent comp, Transferable trans, int action) {
    if (action != MOVE) {
      return;
    }
    JList list = (JList) comp;
    DefaultListModel model = (DefaultListModel) list.getModel();
    int index;
    if (indexF > indexT) {
      index = (indexF + 1);
    } else {
      index = (indexF);
    }
    if (index == model.getSize() - 1) {
      index--;
    }

    if (model.getElementAt(index) == data) {
      model.removeElementAt(index);
    }

  }

}
