/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.MyPlayerMusic.songAdder.MusicAnalizer;
import VControl.utiliti;
import static VControl.utiliti.stripAccents;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
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
import org.farng.mp3.lyrics3.Lyrics3v2;

/**
 *
 * @author vojta3310
 */
public class Skladba implements ISkladba {

  private String Title;
  private String Autor;
  private File Path;
  private String Tags;
  private String Album;
  private String Langue;
  private String SpecialTags;
  private String Lyric;
  private float oblibenost;
  private long start;
  private long lenght;
  private float volume;
//  private final MP3File mp3file;
  private AudioInputStream audioInputStream;
  private int played = 0;
  private int repead = 0;
  private MusicAnalizer ma;

  public Skladba(String pathtofile) {
    TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_WRITE);
    loadFile(new File(pathtofile));
  }

  public Skladba() {
  }

  public final void loadFile(File f) {
    System.out.println("Loading song:" + f.getName() + " from: " + f.getParent());
    this.Path = f;
    try {
      MP3File mp3file = new MP3File(f);
      if (mp3file.hasLyrics3Tag() && mp3file.getLyrics3Tag().getSongComment() != null) {
        if (mp3file.getLyrics3Tag().getSongComment().startsWith("MyPlayer")) {

          this.Autor = mp3file.getLyrics3Tag().getAuthorComposer();
          this.Title = mp3file.getLyrics3Tag().getSongTitle();
          this.Album = mp3file.getLyrics3Tag().getAlbumTitle();
          this.Lyric = mp3file.getLyrics3Tag().getSongLyric();

          String[] s = mp3file.getLyrics3Tag().getSongComment().split("%@%", 20);
          System.out.println(Arrays.toString(s));

          this.Langue = s[4];
          this.Tags = s[3];
          this.SpecialTags = s[5];

          this.volume = Float.parseFloat(s[7]);

          this.start = Long.parseLong(s[1]);
          this.lenght = Long.parseLong(s[2]);

          this.oblibenost = Float.parseFloat(s[6].substring(1));
          if (s[6].startsWith("n")) {
            this.oblibenost *= -1;
          }

//          System.out.println("Tags:");
//          System.out.println("  autor: " + Autor);
//          System.out.println("  title: " + Title);
//          System.out.println("  Album: " + Album);
//          System.out.println("  Langue: " + Langue);
//          System.out.println("  Tags: " + Tags);
//          System.out.println("  SpecialTags: " + SpecialTags);
//          System.out.println("  oblibenost: " + oblibenost);
//          System.out.println("  volume: " + volume);
//          System.out.println("  start: " + start);
//          System.out.println("  lenght: " + lenght);
//          System.out.println("  lirics: " + Lyric.length() + "b");
          if (f.getName().contains("-")) {
            String a[] = f.getName().split("-");
            if (a.length == 2) {
              this.Autor = a[0];
              this.Title = a[1].substring(0, a[1].lastIndexOf("."));
            }
          }
          this.Title = utiliti.titleize(this.Title);
          this.Title = this.Title.trim();
          this.Autor = this.Autor.trim();
          this.Album = this.Album.trim();

        } else {
          System.err.println("Not my song!!!2");
        }
      } else {
        System.err.println("Not my song!!!" + f.getPath());
      }
    } catch (IOException | TagException ex) {
      Logger.getLogger(Skladba.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void load() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

    this.audioInputStream = AudioSystem.getAudioInputStream(Path);

  }

  public void editFavority(int body) {
    oblibenost += body;
    save(Path.getParent());
  }

  public final void save(String dirPath) {
    try {
      File directory = new File(dirPath);
      if (!directory.exists()) {
        directory.mkdir();
      }

      Path f = new File(directory.toString() + File.separator + Autor + "-" + Title + ".mp3").toPath();
      Files.move(Path.toPath(), f, StandardCopyOption.REPLACE_EXISTING);

      TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_WRITE);
      MP3File mp3file = new MP3File(f.toString());

      String ob;
      if (oblibenost < 0) {
        ob = "n";
      } else {
        ob = "p";
      }
      ob += Float.toString(Math.abs(oblibenost));
      String s = "MyPlayer%@%";
      s += Long.toString(start) + "%@%" + Long.toString(lenght) + "%@%";
      s += stripAccents(Tags).replaceAll("[^a-zA-Z0-9\\s]+", "") + "%@%";
      s += Langue + "%@%" + stripAccents(SpecialTags).replaceAll("[^a-zA-Z0-9\\s]+", "") + "%@%";
      s += ob + "%@%" + Float.toString(volume) + "%@%";

      Lyrics3v2 LT = new Lyrics3v2();
      LT.setSongTitle(stripAccents(Title).replaceAll("[^a-zA-Z0-9\\s]+", ""));
      LT.setAuthorComposer(stripAccents(Autor).replaceAll("[^a-zA-Z0-9\\s]+", ""));
      LT.setAlbumTitle(stripAccents(Album).replaceAll("[^a-zA-Z0-9\\s]+", ""));
      LT.setSongLyric(stripAccents(Lyric));
      LT.setSongComment(s);

      if (mp3file.hasID3v2Tag()) {
        mp3file.getID3v2Tag().clearFrameMap();
      }

      mp3file.setLyrics3Tag(LT);
      mp3file.save();

      File fl = new File(directory.toString() + File.separator + Autor + "-" + Title + ".original.mp3");
      if (fl.exists()) {
        fl.delete();
      }
      Path = f.toFile();
    } catch (IOException | TagException ex) {
      Logger.getLogger(Skladba.class
        .getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void initMusicAnalizer() {
    try {
      ma = new MusicAnalizer();
      ma.setFile(Path);
      ma.initAudioStream();
    } catch (UnsupportedAudioFileException | IOException ex) {
      Logger.getLogger(Skladba.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static Comparator<Skladba> SkladbaComparator = (Skladba s1, Skladba s2) -> {
    String StudentName1 = s1.toString().toUpperCase();
    String StudentName2 = s2.toString().toUpperCase();
    return StudentName1.compareTo(StudentName2);
  };

  public MusicAnalizer getMusicAnalizer() {
    return ma;
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
    return Path.getPath();
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
    if (repead != -1) {
      this.repead -= 1;
    }
  }

  @Override
  public String toString() {
    return Title + " - " + Autor;
  }

  public void setTitle(String Title) {
    this.Title = Title;
  }

  public void setAutor(String Autor) {
    this.Autor = Autor;
  }

  public void setTags(String Tags) {
    this.Tags = Tags;
  }

  public void setAlbum(String Album) {
    this.Album = Album;
  }

  public void setLangue(String Langue) {
    this.Langue = Langue;
  }

  public void setSpecialTags(String SpecialTags) {
    this.SpecialTags = SpecialTags;
  }

  public void setLyric(String Lyric) {
    this.Lyric = Lyric;
  }

  public void setOblibenost(float oblibenost) {
    this.oblibenost = oblibenost;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public void setLenght(long lenght) {
    this.lenght = lenght;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

}
