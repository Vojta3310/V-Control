/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import Moduls.MyPlayerMusic.Player.Skladba;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vojta3310
 */
public class SongInfo {

  private String Autor = "";
  private String Album = "";
  private final String Langue = "";
  private String Tags = "";
  private String Title = "";
  private String Lyric = "";

  public void load(File f) {
    googleIt(f.getName());
    String s = "";
    if (Autor.equals("") && Album.equals("") && Title.equals("") && Lyric.equals("")) {
      try {
        MP3File mp3file = new MP3File(f.getPath());
        if (mp3file.hasID3v2Tag()) {
          if (mp3file.getID3v2Tag().hasFrame("TPE1")) {
            s = mp3file.getID3v2Tag().getFrame("TPE1").getBody().getBriefDescription();
          }
          if (mp3file.getID3v2Tag().hasFrame("TIT2")) {
            s += " - " + mp3file.getID3v2Tag().getFrame("TIT2").getBody().getBriefDescription();
          }
        }
        if (!s.equals("")) {
          googleIt(s);
        }
      } catch (IOException | TagException ex) {
        Logger.getLogger(SongInfo.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    if (Autor.equals("") || Album.equals("") || Title.equals("") || Lyric.equals("")) {
//      if (Autor.equals("")) {
//        s += f.getName();
//      } else if (!Autor.equals("") && !Title.equals("")) {
//        s = Autor + " - " + Title;
//        s += f.getName();
//      }
      karaokeIt(f.getName());
    }
  }

  private void karaokeIt(String s) {
    System.out.println(s);
    try {
      Document doc = Jsoup.connect("http://www.karaoketexty.cz/search?q=" + URLEncoder.encode(s.replace(".mp3", ""))).get();
      Element a = doc.select("ul.title").first().select("a").first();
      System.out.println(a.absUrl("href"));
      String[] t = a.text().split(" - ");
      if (Autor.equals("")) {
        Autor = t[0];
        Title = t[1];
        System.out.println(Autor);
      }
      if (!Title.equals(t[1]) && Autor.equals(t[0])) {
        Title = t[1];
      }
      doc = Jsoup.connect(a.absUrl("href")).get();
      if (Album.equals("")) {
        Element n = doc.select("div.navigation_lyrics").first().select("a").last();
        if (!n.text().equals(Autor)) {
          Album = n.text();
        }
      }
      Element tx = doc.select("div[class*=_lyrics]:has(p.text)").first();

      System.out.println(tx.text());
      if (Lyric.equals("")) {
        Lyric = " " + tx.html().replace("<br>", "\n").replace("<p>", "").replace("</p>", "");
        Lyric = (String) Lyric.subSequence(Lyric.indexOf(">") + 1, Lyric.lastIndexOf("<"));
        Lyric = (String) Lyric.subSequence(0, Lyric.indexOf("<"));
      }
    } catch (IOException ex) {
      Logger.getLogger(SongInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void findLiric(Skladba s) {
    try {
      Document doc = Jsoup.connect("http://www.karaoketexty.cz/search?q="
        + URLEncoder.encode(s.getAutor() + " - " + s.getTitle())).get();
      Element a = doc.select("ul.title").first().select("a").first();
      doc = Jsoup.connect(a.absUrl("href")).get();
      if (s.getAlbum().equals("")) {
        Element n = doc.select("div.navigation_lyrics").first().select("a").last();
        if (!n.text().equals(s.getAutor())) {
          s.setAlbum(n.text());
        }
      }
      Element tx = doc.select("div[class*=_lyrics]:has(p.text)").first();

      System.out.println(tx.text());
      String Lyric;
      Lyric = " " + tx.html().replace("<br>", "\n").replace("<p>", "").replace("</p>", "");
      Lyric = (String) Lyric.subSequence(Lyric.indexOf(">") + 1, Lyric.lastIndexOf("<"));
      Lyric = (String) Lyric.subSequence(0, Lyric.indexOf("<"));
      s.setLyric(Lyric);
      
    } catch (IOException ex) {
      Logger.getLogger(SongInfo.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void googleIt(String s) {
    try {

      Document doc = Jsoup.connect("https://www.google.cz/search?q=" + URLEncoder.encode(s.replace(".mp3", ""))).get();
      Elements a = doc.select("#res");
      if (a != null) {
        if (a.html().contains("Uměl")) {
          Autor = (a.select("span:contains(Uměl)").parents().first().children().last().text());
        }
        if (a.html().contains("Žánr")) {
          Tags = (a.select("span:contains(Žánr)").parents().first().children().last().text());
        }
        if (a.html().contains("Album")) {
          Album = (a.select("span:contains(Album)").parents().first().children().last().text());
        }
        if (a.html().contains("text skladby")) {

          String href = a.select("a:containsOwn(text skladby)").attr("href");
          Document docl = Jsoup.connect(href).get();
          try {
            Element b = docl.select(".fade-out").first();
            Title = b.text();
          } catch (Exception e) {

          }

          try {
            Elements c = docl.select(".lyrics");
            Lyric = c.html().replace("<br>", "\n").replace("<p>", "").replace("</p>", "");
            Lyric = (String) Lyric.subSequence(Lyric.indexOf(">") + 1, Lyric.lastIndexOf("<"));
            Lyric = (String) Lyric.subSequence(Lyric.indexOf(">") + 1, Lyric.lastIndexOf("<"));
          } catch (Exception e) {

          }
        } else if (a.html().contains("_ARr")) {
          Element d = a.select("._ARr").first();
          d.select("._kEr").remove();
          d.select("._Rtn").remove();
          d.select("g-text-expander").prev().remove();
          d.select("g-text-expander").remove();
          Lyric = String.join("\n", d.select("span").eachText());
        }
        if (Title.equals("") && a.html().contains("h3 class=\"r\"")) {
          String[] sp = a.select(".r").text().split(" - ");
          if (sp.length > 1) {
            Title = sp[1];
          }

        }

      }
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Nepodařilo se vyGooglit informace o skladbě.");
    }

  }

  public void setInfo(Skladba s) {
    File f = new File(s.getPath());
    load(f);
    s.setAlbum(Album);
    s.setAutor(Autor);
    s.setLangue(Langue);
    s.setLyric(Lyric);
    s.setTags(Tags);
    s.setTitle(Title);
  }

  public String getAutor() {
    return Autor;
  }

  public String getAlbum() {
    return Album;
  }

  public String getLangue() {
    return Langue;
  }

  public String getTags() {
    return Tags;
  }

  public String getTitle() {
    return Title;
  }

  public String getLyric() {
    return Lyric;
  }

}
