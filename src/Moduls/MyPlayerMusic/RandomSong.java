/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import java.util.ArrayList;

/**
 *
 * @author vojta3310
 */
public class RandomSong implements ISkladba {

  private final Songs songs;
  private Skladba song;
  private String Title;
  private String Autor;
  private String Tags;
  private String Album;
  private String Langue;
  private String SpecialTags;
  private float oblibenost;
  //!private InfoPanel iPanel;

  public RandomSong(Songs s) {
    this.songs = s;
  }

  public void chooseSong() {
    ArrayList<Skladba> a = new ArrayList<>();
    for (int i = 0; i < songs.getSongs().size(); i++) {
      Skladba s = songs.getSongs().get(i);
      boolean ok = true;
      if (Title != null) {
        ok &= Title.equals(s.getTitle());
      }
      if (Autor != null) {
        ok &= Autor.equals(s.getAutor());
      }
      if (Album != null) {
        ok &= Album.equals(s.getAlbum());
      }
      if (Langue != null) {
        ok &= Langue.equals(s.getLangue());
      }
      if (Tags != null) {
        String[] t = Tags.split("|");
        for (String t1 : t) {
          ok &= s.getTags().contains(t1);
        }
      }
      if (SpecialTags != null) {
//        String[] t = SpecialTags.split("|");
        for (String aSpecialTag : s.getASpecialTags()) {
          ok &= SpecialTags.contains(aSpecialTag);
        }
      }
      if (ok) {
        a.add(s);
      }
    }

    //v a máme všechny přijatelné skladby
    if (a.isEmpty()) {
      System.out.println("Není co hrát: neexistuje skladba která by odpovídala všem podmínkám");
    }

    ArrayList<Integer> b = new ArrayList<>();
    int sum = 0;

    for (Skladba s : a) {
      int o = (int) s.getOblibenost() * songs.getCount() / (s.getPlayed() + 1);
      b.add(o);
      sum += o;
    }
    int n = (int) (Math.random() * sum);
    for (int i = 0; i < b.size(); i++) {
      n -= b.get(i);
      if (n <= 0) {
        this.song = a.get(i);
        break;
      }
    }
  }

  @Override
  public Skladba getSkladba() {
    chooseSong();
    return song;
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String Title) {
    this.Title = Title;
  }

  public String getAutor() {
    return Autor;
  }

  public void setAutor(String Autor) {
    this.Autor = Autor;
  }

  public String getTags() {
    return Tags;
  }

  public void setTags(String Tags) {
    this.Tags = Tags;
  }

  public String getAlbum() {
    return Album;
  }

  public void setAlbum(String Album) {
    this.Album = Album;
  }

  public String getLangue() {
    return Langue;
  }

  public void setLangue(String Langue) {
    this.Langue = Langue;
  }

  public String getSpecialTags() {
    return SpecialTags;
  }

  public void setSpecialTags(String SpecialTags) {
    this.SpecialTags = SpecialTags;
  }

  public float getOblibenost() {
    return oblibenost;
  }

  public void setOblibenost(float oblibenost) {
    this.oblibenost = oblibenost;
  }

}
