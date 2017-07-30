/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.MyPlayerMusic.Player.Skladba;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class Songs {

  private ArrayList<Skladba> Songs;
//  private model

  public void load(String path) throws IOException, TagException {
    loadDir(path);
  }

  private void loadDir(String path) throws IOException, TagException {
    File f = new File(path);
    String[] files = f.list();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (fil.isDirectory()) {
        loadDir(fil.getPath());
      } else {
        Songs.add(new Skladba(fil.getPath()));
      }
    }
  }

  public ArrayList<Skladba> getSongs() {
    return Songs;
  }

  public int getCount() {
    return Songs.size();
  }
}