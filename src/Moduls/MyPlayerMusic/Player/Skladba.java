/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import java.io.IOException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.TagOptionSingleton;
import org.farng.mp3.id3.AbstractID3v2Frame;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyTDAT;
import org.farng.mp3.id3.ID3v2_4Frame;

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
  private int repead = 0;

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
        this.lenght = Long.parseLong(s[8]) - start;
      } else {
        System.err.println("Not my song!!!2");
      }

    } else {
      System.err.println("Not my song!!!" + pathtofile);
    }
  }

  public void load() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

    this.audioInputStream = AudioSystem.getAudioInputStream(new File(Path));

  }

  public void editFavority(int body) {
    try {
      oblibenost += body;

      String[] s = mp3file.getID3v2Tag().getFrame("TDAT").getBody().getBriefDescription().split("%@%", 20);

      TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);

      mp3file.getID3v2Tag().clearFrameMap();

      s[9] = Float.toString(oblibenost);

      String str = "";
      for (String seti : s) {
        str += seti + "%@%";
      }
      str = str.substring(0, str.length() - 3);


      AbstractID3v2Frame frame;
      AbstractID3v2FrameBody frameBody;
      frameBody = new FrameBodyTDAT((byte) 0, str);
      frame = new ID3v2_4Frame(frameBody);
      mp3file.getID3v2Tag().setFrame(frame);
      mp3file.save();
    } catch (IOException | TagException ex) {
      Logger.getLogger(Skladba.class.getName()).log(Level.SEVERE, null, ex);
    }

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
    return (float) (1 / (1 + Math.exp(-oblibenost / 5)));
  }

  public int getPlayed() {
    return played;
  }

  public void Played() {
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

  @Override
  public String toString() {
    return Title + " - " + Autor;
  }

}
