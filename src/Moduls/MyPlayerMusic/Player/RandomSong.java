/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class RandomSong implements ISkladba {

  private final Songs songs;
  private Skladba song;
  private ArrayList<Term> podm = new ArrayList<>();
  private ArrayList<Skladba> Pripustne;
  private int repead = 0;
  private int repeaded = 0;
  private boolean choosedOne = false;
  //!private InfoPanel iPanel;

  public RandomSong(Songs s) {
    this.songs = s;
  }

  public void chooseSong() {
    if (!choosedOne) {
      if (Pripustne == null) {
        Pripustne = songs.getSongsByTern(podm);
      }
      //v Pripustne máme všechny přijatelné skladby
      if (Pripustne.isEmpty()) {
        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Není co hrát: neexistuje skladba která by odpovídala všem podmínkám");
        Pripustne = songs.getSongs();
      } else if (Pripustne.size() == 1) {
        choosedOne = true;
        song = Pripustne.get(0);
        song.setRepead(0);
        //this.repeaded = repead - 1;
      } else {
        choosedOne = false;
        ArrayList<Integer> b = new ArrayList<>();
        int sum = 0;

        sum = Pripustne.stream().map((s) -> (int) (s.getOblibenost() * 100 / (s.getPlayed() * 3 + 1)) + 1).map((o) -> {
          b.add(o);
          return o;
        }).map((o) -> o).reduce(sum, Integer::sum);
        int n = (int) (Math.random() * (sum - 1));
        for (int i = 0; i < b.size(); i++) {
          n -= b.get(i);
          if (n <= 0) {
            this.song = Pripustne.get(i);
            this.song.setRepead(0);
            break;
          }
        }
      }
    }
  }

  @Override
  public String getLabel() {
    String l = "";
    if (repead > 0) {
      l = "(" + (repead + 1) + "x) ";
    }
    if (choosedOne) {
      l = l + song.getTitle() + "-" + song.getAutor();
    } else if (podm != null) {

      String s = null;
      int i = 0;
      while (s == null && i < podm.size()) {
        s = podm.get(i).getLabel();
        i++;
      }
      if (s != null) {
        l = l + s;
      } else {
        l = l + "Náhodná skladba";
      }
    } else {
      l = l + "Náhodná skladba";
    }
    return l;
  }

  public boolean isEmty() {
    if (podm == null) {
      return true;
    }
    return podm.isEmpty();
  }

//  @Override
  public Skladba getSkladba() {
    chooseSong();
    return song;
  }

  public void Repeaded() {
    this.repeaded += 1;
  }

  public int getRepeaded() {
    return repeaded;
  }

  public void ResetRepeaded() {
//    if (!choosedOne) {
//      this.repeaded = 0;
//    } else {
//      repeaded = repead - 1;
//    }
    this.repeaded = 0;
  }

  public int getRepead() {
    return repead;
  }

  public void setRepead(int repead) {
    this.repead = repead;
  }

  public void setPodm(ArrayList<Term> podm) {
    choosedOne = false;
    this.podm = new ArrayList<>(podm);
    Pripustne = songs.getSongsByTern(podm);
    chooseSong();
  }

  public void setSong(Skladba s) {
    choosedOne = false;
    this.podm = new ArrayList<>();

    Term star = new Term();
    star.setType("Stars");
    star.setVal(s.getOblibenost());
    podm.add(star);

    Term stag = new Term();
    stag.setType("STag");
    stag.setValue(s.getSpecialTags());
    podm.add(stag);

    Term name = new Term();
    name.setType("Název");
    name.setValue(s.getTitle());
    podm.add(name);

    Term aut = new Term();
    aut.setType("Autor");
    aut.setValue(s.getAutor());
    podm.add(aut);

    Term alb = new Term();
    alb.setType("Album");
    alb.setValue(s.getAlbum());
    podm.add(alb);

    Term lang = new Term();
    lang.setType("Jazyk");
    lang.setValue(s.getLangue());
    podm.add(lang);

    Term tag = new Term();
    tag.setType("Tagy");
    tag.setValue(s.getTags());
    podm.add(tag);

    Pripustne = songs.getSongsByTern(podm);
    chooseSong();
  }

  public ArrayList<Term> getPodm() {
    return podm;
  }

  @Override
  public String toString() {
    return getLabel();
  }

}
