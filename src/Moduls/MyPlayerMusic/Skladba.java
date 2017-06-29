/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import java.awt.Component;
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
  // private InfoPanel iPanel;

  public Skladba(String pathtofile) throws IOException, TagException {
    this.Path = pathtofile;
    this.mp3file = new MP3File(pathtofile);
    if (mp3file.getID3v2Tag().hasFrame("MyPlayer")) {
      this.Autor = mp3file.getID3v2Tag().getFrame("TPE1").toString();
      this.Title = mp3file.getID3v2Tag().getFrame("TIT2").toString();
      this.Album = mp3file.getID3v2Tag().getFrame("TALB").toString();
      this.Tags = mp3file.getID3v2Tag().getFrame("Tags").toString();
      this.SpecialTags = mp3file.getID3v2Tag().getFrame("STags").toString();
      this.Langue = mp3file.getID3v2Tag().getFrame("Lang").toString();
      this.oblibenost = Float.parseFloat(mp3file.getID3v2Tag().getFrame("Fav").toString());
      this.start = Long.parseLong(mp3file.getID3v2Tag().getFrame("Start").toString());
      this.lenght = Long.parseLong(mp3file.getID3v2Tag().getFrame("len").toString());
      //  this.iPanel = new InfoPanel(this);
    } else {
      System.err.println("Not my song!!!");
    }
  }

  public void load() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

    this.audioInputStream = AudioSystem.getAudioInputStream(
      new File("some_file.wav"));
    byte[] bytes = new byte[(int) (audioInputStream.getFrameLength())
      * (audioInputStream.getFormat().getFrameSize())];
    audioInputStream.read(bytes);
    long sum = 0;
    for (int i = 0; i < bytes.length; i++) {
      sum += bytes[i];
    }
    this.volume = (float) sum / bytes.length;
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

  @Override
  public Skladba getSkladba() {
    return this;
  }

//  @Override
//  public Component getInfoPanel() {
//    return iPanel;
//  }
  public int getPlayed() {
    return played;
  }

  public void Played() {
    this.played += 1;
  }
}
