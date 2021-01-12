/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.RandomSong;
import Moduls.MyPlayerMusic.Player.Skladba;
import Moduls.MyPlayerMusic.Player.Songs;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class SlistTransferHandler extends TransferHandler {

  private final Songs songs;

  public SlistTransferHandler(Songs s) {
    super();
    songs = s;
  }

  @Override
  public int getSourceActions(JComponent comp) {
    return COPY;
  }

  private int index = 0;

  @Override
  public Transferable createTransferable(JComponent comp) {
    JList dragFrom = (JList) comp;
    index = dragFrom.getSelectedIndex();
    if (index < 0 || index >= dragFrom.getModel().getSize()) {
      return null;
    }
    RandomSong rs=new RandomSong(songs);
    rs.setSong((Skladba)dragFrom.getModel().getElementAt(index));
    return new TransferableRS(rs);
  }

  @Override
  public void exportDone(JComponent comp, Transferable trans, int action) {
  }
}
