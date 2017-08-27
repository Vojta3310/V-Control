/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

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
  private float statMinVol = 1;
  private float statMaxVol = 0;
//  private model

  public void load(String path) throws IOException, TagException {
    loadDir(path);
  }

  private void loadDir(String path) throws IOException, TagException {
    File f = new File(path);
    String[] files = f.list();
    Songs = new ArrayList<>();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (fil.isDirectory()) {
        loadDir(fil.getPath());
      } else {
        Skladba a = new Skladba(fil.getPath());
        Songs.add(a);
        statMinVol = Math.min(a.getVolume(), statMinVol);
        statMaxVol = Math.max(a.getVolume(), statMaxVol);
      }
    }
  }

  public ArrayList<Skladba> getSongs() {
    return Songs;
  }

  public ArrayList<Skladba> getSongsByTern(ArrayList<Term> podm) {
    if (podm == null) {
      return Songs;
    }
    ArrayList<Skladba> a = new ArrayList<>();
    for (Skladba s : Songs) {
      boolean ok = true;
      for (Term p : podm) {
        ok = ok & p.ValidSong(s);
      }
      if (ok) {
        a.add(s);
      }
    }
    return a;
  }

  public ArrayList<String> getLabelsByTerms(String Type, ArrayList<Term> podm) {
    ArrayList<Skladba> s = getSongsByTern(podm);
    ArrayList<String> out = new ArrayList<>();
    for (Skladba sk : s) {
      String a = sk.getTitle();
      switch (Type) {
        case "Název":
          a = sk.getTitle();
          break;
        case "Autor":
          a = sk.getAutor();
          break;
        case "Album":
          a = sk.getAlbum();
          break;
        case "Jazyk":
          a = sk.getLangue();
          break;
        case "Tagy":
          a = sk.getTags();
          break;
      }
      if (!out.contains(a) && !a.equals("")) {
        out.add(a);
      }
    }
    return out;
  }

  public int getCount() {
    return Songs.size();
  }

  public float getStatMinVol() {
    return statMinVol;
  }

  public float getStatMaxVol() {
    return statMaxVol;
  }
}
