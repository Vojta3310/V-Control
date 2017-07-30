/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import java.io.IOException;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class Skladba implements ISkladba {

  private String Title;
  private String Autor;
  private final String Path;
  private String Tags;
  private String Album;
  private String Langue;
  private String SpecialTags;
  private float oblibenost;
  private long start;
  private long lenght;
  private float volume;
  private final MP3File mp3file;
  private AudioInputStream audioInputStream;
  private int played = 0;
  private int repead = 1;

  public Skladba(String pathtofile) throws IOException, TagException {
    this.Path = pathtofile;
    this.mp3file = new MP3File(pathtofile);
    if (mp3file.getID3v2Tag().hasFrame("TDAT")) {
      if (mp3file.getID3v2Tag().getFrame("TDAT").getBody().getBriefDescription().startsWith("MyPlayer%@%")) {
        String[] s = mp3file.getID3v2Tag().getFrame("TDAT").getBody().getBriefDescription().split("%@%", 20);
        this.Autor = s[2];
        this.Title = s[1];
        this.Album = s[3];
        this.Tags = s[5];
        this.SpecialTags = s[6];
        this.Langue = s[4];
        this.oblibenost = Float.parseFloat(s[9]);
        this.volume = Float.parseFloat(s[10]);
        this.start = Long.parseLong(s[7]);
        this.lenght = Long.parseLong(s[8]);
      } else {
        System.err.println("Not my song!!!");
      }

    } else {
      System.err.println("Not my song!!!");
    }
  }

  public void load() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

    this.audioInputStream = AudioSystem.getAudioInputStream(new File(Path));

  }

  @Override
  public String getLabel() {
    return Title + "-" + Autor;
  }

  public String getTitle() {
    return Title;
  }

  public String getAutor() {
    return Autor;
  }

  public String getPath() {
    return Path;
  }

  public String getAlbum() {
    return Album;
  }

  public String getLangue() {
    return Langue;
  }

  public long getStart() {
    return start;
  }

  public long getLenght() {
    return lenght;
  }

  public float getVolume() {
    return volume;
  }

  public AudioInputStream getAudioInputStream() {
    return audioInputStream;
  }

  public String getTags() {
    return Tags;
  }

  public String[] getATags() {
    if (Tags != null) {
      return Tags.split("|");
    }
    return null;
  }

  public String getSpecialTags() {
    return SpecialTags;
  }

  public String[] getASpecialTags() {
    if (SpecialTags != null) {
      return SpecialTags.split("|");
    }
    return null;
  }

  public float getOblibenost() {
    return oblibenost;
  }

  public int getPlayed() {
    return played;
  }

  public void Played() {
    Repeaded();
    this.played += 1;
  }

  public int getRepead() {
    return repead;
  }

  public void setRepead(int repead) {
    this.repead = repead;
  }

  public void Repeaded() {
    this.repead -= 1;
  }

}
