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
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyTENC;
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
  private String Lyric;
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
    if (mp3file.hasID3v2Tag() && mp3file.getID3v2Tag().hasFrame("TMED")) {
      if (mp3file.getID3v2Tag().getFrame("TMED").getBody().getBriefDescription().equals("MyPlayer")) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINER, "Loading song from: "+Path);
        
//        System.out.println(mp3file.getID3v2Tag().getFrame("TDAT").getBody().getBriefDescription());
        
        
//        String[] s = mp3file.getID3v2Tag().getFrame("TPE1").getBody().getBriefDescription().split("%@%", 20);
        
        this.Autor = mp3file.getID3v2Tag().getFrame("TPE1").getBody().getBriefDescription();
        this.Title = mp3file.getID3v2Tag().getFrame("TIT2").getBody().getBriefDescription();
        this.Album = mp3file.getID3v2Tag().getFrame("TALB").getBody().getBriefDescription();
        this.Langue = mp3file.getID3v2Tag().getFrame("TOPE").getBody().getBriefDescription();
        this.Tags = mp3file.getID3v2Tag().getFrame("TPUB").getBody().getBriefDescription();
        this.SpecialTags = mp3file.getID3v2Tag().getFrame("TCOP").getBody().getBriefDescription();
        
        this.oblibenost = Float.parseFloat(mp3file.getID3v2Tag().getFrame("TENC").getBody().getBriefDescription());
        this.volume = Float.parseFloat(mp3file.getID3v2Tag().getFrame("TCOM").getBody().getBriefDescription());
        
        this.start = Long.parseLong(mp3file.getID3v2Tag().getFrame("TSRC").getBody().getBriefDescription());
        this.lenght = Long.parseLong(mp3file.getID3v2Tag().getFrame("TRCK").getBody().getBriefDescription()) - start;
        this.Lyric = mp3file.getID3v2Tag().getFrame("TEXT").getBody().getBriefDescription();
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
//
//      String[] s = mp3file.getID3v2Tag().getFrame("TDAT").getBody().getBriefDescription().split("%@%", 20);
//
//      TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);
//
//      mp3file.getID3v2Tag().clearFrameMap();
//
//      s[9] = Float.toString(oblibenost);
//
//      String str = "";
//      for (String seti : s) {
//        str += seti + "%@%";
////      }
//      str = str.substring(0, str.length() - 3);
//
//      System.out.println(str);
       
      
      mp3file.getID3v2Tag().removeFrame("TENC");
      AbstractID3v2FrameBody frameBody = new FrameBodyTENC((byte) 0, Float.toString(oblibenost));
      mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
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

  public String getLyric() {
    return Lyric;
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
