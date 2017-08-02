/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

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
  private float oblibenost = 0f;
  private int repead = 0;
  private boolean choosedOne = false;
  //!private InfoPanel iPanel;

  public RandomSong(Songs s) {
    this.songs = s;
  }

  public void chooseSong() {
    if (!choosedOne) {
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
        ok &= (s.getOblibenost() >= oblibenost);
        if (ok) {
          a.add(s);
        }
      }

      //v a máme všechny přijatelné skladby
      if (a.isEmpty()) {
        System.out.println("Není co hrát: neexistuje skladba která by odpovídala všem podmínkám");
      } else if (a.size() == 1) {
        choosedOne = true;
        song = a.get(0);
        song.setRepead(repead);
        this.repead = 0;
      } else {
        choosedOne = false;
        ArrayList<Integer> b = new ArrayList<>();
        int sum = 0;

        for (Skladba s : a) {
          int o = (int)( s.getOblibenost() * songs.getCount() / (s.getPlayed()*3 + 1))+1;
          b.add(o);
          sum += o;
        }
        int n = (int) (Math.random() * sum);
        for (int i = 0; i < b.size(); i++) {
          n -= b.get(i);
          if (n <= 0) {
            this.song = a.get(i);
            this.song.setRepead(0);
            break;
          }
        }
      }
    }
  }

  public void setSong(Skladba song) {
    this.song = song;
    this.Album = song.getAlbum();
    this.Autor = song.getAutor();
    this.Langue = song.getLangue();
    this.SpecialTags = song.getSpecialTags();
    this.Tags = song.getTags();
    this.Title = song.getTitle();
    this.oblibenost = song.getOblibenost();
  }

  @Override
  public String getLabel() {
    String l = "";
    if (repead > 0) {
      l = "(" + repead + "x) ";
    }
    if ((Title != null) && (Autor != null)) {
      l = l + Title + "-" + Autor;
    } else if (Title != null) {
      l = l + "Tit.=" + Title;
    } else if (Autor != null) {
      l = l + "Aut.=" + Autor;
    } else if (Album != null) {
      l = l + "Alb.=" + Album;
    } else if (Langue != null) {
      l = l + "Jaz.=" + Langue;
    } else if (Tags != null) {
      l = l + "Tag.=" + Tags;
    } else if (SpecialTags != null) {
      l = l + "STag.=" + SpecialTags;
    } else {
      l = l + "Náhodná skladba";
    }
    return l;
  }

  public boolean isEmty() {
    return (Title == null)
      && (Autor == null)
      && (Album == null)
      && (Langue == null)
      && (Tags == null)
      && (oblibenost == 0);
  }

//  @Override
  public Skladba getSkladba() {
    chooseSong();
    return song;
  }

  public String getTitle() {
    return Title;
  }

  public void setTitle(String Title) {
    this.Title = Title;
    choosedOne = false;
  }

  public String getAutor() {
    return Autor;
  }

  public void setAutor(String Autor) {
    this.Autor = Autor;
    choosedOne = false;
  }

  public String getTags() {
    return Tags;
  }

  public void setTags(String Tags) {
    this.Tags = Tags;
    choosedOne = false;
  }

  public String getAlbum() {
    return Album;
  }

  public void setAlbum(String Album) {
    this.Album = Album;
    choosedOne = false;
  }

  public String getLangue() {
    return Langue;
  }

  public void setLangue(String Langue) {
    this.Langue = Langue;
    choosedOne = false;
  }

  public String getSpecialTags() {
    return SpecialTags;
  }

  public void setSpecialTags(String SpecialTags) {
    this.SpecialTags = SpecialTags;
    choosedOne = false;
  }

  public float getOblibenost() {
    return oblibenost;
  }

  public void setOblibenost(float oblibenost) {
    this.oblibenost = oblibenost;
    choosedOne = false;
  }

  public void Repeaded() {
    this.repead -= 1;
  }

  public int getRepead() {
    return repead;
  }

  public void setRepead(int repead) {
    this.repead = repead;
  }

}
