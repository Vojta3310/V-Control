/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.RandomSong;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author vojta3310
 */
public class TransferableRS implements Transferable {

  protected static final DataFlavor RSFlavor
    = new DataFlavor(RandomSong.class, "A RS Object");

  protected static final DataFlavor[] supportedFlavors = {
    RSFlavor};

  private final RandomSong rs;

  public TransferableRS(RandomSong rs) {

    this.rs = rs;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {

    return supportedFlavors;
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {

    return flavor.equals(RSFlavor);
  }

  @Override
  public Object getTransferData(DataFlavor flavor)
    throws UnsupportedFlavorException {

    if (flavor.equals(RSFlavor)) {
      return rs;
    } else {
      throw new UnsupportedFlavorException(flavor);
    }
  }

}
