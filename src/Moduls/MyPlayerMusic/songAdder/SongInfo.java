/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
  private String Langue = "";
  private String Tags = "";
  private String Title = "";

  public void load(File f) {
    try {
      Document doc = Jsoup.connect("https://www.google.cz/search?q=" + URLEncoder.encode(f.getName().replace(".mp3", ""))).get();
      Elements a = doc.select("#uid_0");
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
        if (a.html().contains("Více informací o: ")) {
          Title = (a.select("div:containsOwn(Více informací o: )").html().replace("Více informací o: ", ""));
        }

      }
    } catch (Exception e) {
      System.out.println("Nepodařilo se vyGooglit informase o skladbě.");
    }

  }

  public String[] getSet() {
    return new String[]{getTitle(), getAutor(), getAlbum(), getLangue(), getTags(), "", "", "",};
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

}
