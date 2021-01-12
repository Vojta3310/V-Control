/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import VControl.Command;
import Moduls.Modul;
import Moduls.MyPlayerMusic.Player.Skladba;
import Moduls.MyPlayerMusic.songAdder.YT_DL;
import Moduls.MyPlayerMusic.songAdder.addFromFile;
import VControl.UI.ToolButton;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class MyPlayerMusic extends Modul {

  private final MusicOrganiser Player;
  private final addFromFile addSong;
  private final EditSong edit;
  private final YT_DL yt;
  private final ArrayList<String> paused = new ArrayList<>();
  private boolean lasPaused;

  private final ToolButton a = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/YouTube-dl.png")));
  private final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/PlayFile.png")));
  private final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/addFromFile.png")));
  private final ToolButton d = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/EditSong.png")));
  private final ToolButton e = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/karaoke.png")));

  public MyPlayerMusic(VControl.Commander Commander) throws LineUnavailableException, IOException, TagException, UnsupportedAudioFileException {
    super(Commander);
    Player = new MusicOrganiser(this);
    addSong = new addFromFile(this);
    edit = new EditSong(this, Player.getSongs());
    yt = new YT_DL(this);
  }

  public void reloadSongs() throws IOException, TagException {
    Player.reloadSongs();
  }

  @Override
  public final Image GetIcon() {
    try {
      return ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/modul.png"));
    } catch (IOException ex) {
      Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public String GetModulName() {
    return "MyPlayerMusic";
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void Execute(Command co) {
    switch (co.GetCommand()) {
      case "Play":
        paused.remove(co.GetFrom());
        if (paused.isEmpty() && !lasPaused) {
          Player.Play();
          while (Player.getPaused()) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException ex) {
              Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
        break;
      case "Pause":
        if (paused.isEmpty()) {
          lasPaused = Player.getPaused();
        }
        paused.add(co.GetFrom());
        if (!paused.isEmpty()) {
          Player.Pause();
          while (!Player.getPaused()) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException ex) {
              Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
        break;
      case "Next":
        Player.Next();
        break;
      case "Prew":
        Player.Prew();
        break;
      case "Repeat":
        Player.Repeat();
        break;
      case "ToglePause":
        Player.tooglePause();
        break;
      case "VUp":
        Player.Vup();
        break;
      case "VDown":
        Player.Vdown();
        break;
      case "VSet":
        if (co.GetParms() != null) {
          Player.setVolume((float) co.GetParms());
        }
        break;
      case "Pget":
        co.setResults(!Player.getPaused());
        break;
      case "Sget":
        co.setResults(Player.getPlaying());
        break;
      case "Posget":
        co.setResults((float) (Player.getAplayer().getPos() - Player.getPlaying().getStart()) / Player.getPlaying().getLenght());
        break;
      case "APget":
        co.setResults(Player.getAp());
        break;
      case "SSong":
        String s = "%csPrávě hraje:";
        s += "%" + Player.getPlaying().getLangue().substring(0, 2);
        s += "" + Player.getPlaying().getTitle();
        s += "%cs od:";
        s += "%" + Player.getPlaying().getLangue().substring(0, 2);
        s += " " + Player.getPlaying().getAutor();
        getCommander().Execute(new Command("Say", s, "Speaker", GetModulName()));
        break;

    }
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_MusicDir", "/home/vojta3310/Hudba/MP/");
    p.setProperty("Modul_" + this.GetModulName() + "_Transfet_Lenght", "1000");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Step", "0.1");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_Volume", "0.5");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Command", "G");
    p.setProperty("Modul_" + this.GetModulName() + "_Mic_Volume_Command", "amixer set Capture %v%%");
    p.setProperty("Modul_" + this.GetModulName() + "_MaxVolume", "150");
    p.setProperty("Modul_" + this.GetModulName() + "_Loudness_Controll_Strenght", "0.35");
    p.setProperty("Modul_" + this.GetModulName() + "_Mic_Default_Volume", "0.25");
  }

  @Override
  public void StartModule() {
    b.Activate();
    a.addActionListener((java.awt.event.ActionEvent evt) -> {
      c.Deactivate();
      d.Deactivate();
      b.Deactivate();
      e.Deactivate();
      a.Activate();
      setMyGrafics(yt.getGui());
      getCommander().Execute(new Command("Enable", "ScreenSaver", GetModulName()));
    });
    b.addActionListener((java.awt.event.ActionEvent evt) -> {
      c.Deactivate();
      d.Deactivate();
      a.Deactivate();
      e.Deactivate();
      b.Activate();
      setMyGrafics(Player.getGui());
      Player.getGui().setKaraoke(false);
      getCommander().Execute(new Command("Enable", "ScreenSaver", GetModulName()));
    });
    c.addActionListener((java.awt.event.ActionEvent evt) -> {
      b.Deactivate();
      d.Deactivate();
      a.Deactivate();
      e.Deactivate();
      c.Activate();
      setMyGrafics(addSong.getGui());
      getCommander().Execute(new Command("Enable", "ScreenSaver", GetModulName()));
    });
    d.addActionListener((java.awt.event.ActionEvent evt) -> {
      b.Deactivate();
      c.Deactivate();
      a.Deactivate();
      e.Deactivate();
      d.Activate();
      setMyGrafics(edit.getGui());
      getCommander().Execute(new Command("Enable", "ScreenSaver", GetModulName()));
    });
    e.addActionListener((java.awt.event.ActionEvent evt) -> {
      b.Deactivate();
      c.Deactivate();
      a.Deactivate();
      d.Deactivate();
      e.Activate();
      setMyGrafics(Player.getGui());
      Player.getGui().setKaraoke(true);
      getCommander().Execute(new Command("Disable", "ScreenSaver", GetModulName()));
    });
    super.getToolBar().addTool(b);
    super.getToolBar().addTool(e);
    super.getToolBar().addTool(c);
    super.getToolBar().addTool(d);
    super.getToolBar().addTool(a);
    super.setMyGrafics(Player.getGui());
    Player.getGui().getKaraoke().setVolumeCmd(SgetString("Mic_Volume_Command"));
    Player.getGui().getKaraoke().setMaxVolume(SgetInt("MaxVolume"));
    Player.getGui().getKaraoke().setMicVolume(Float.parseFloat(SgetString("Mic_Default_Volume")));
    Player.getGui().setKaraoke(false);
  }

  public void addFile(File f) {
    try {
      setMyGrafics(addSong.getGui());
      b.Deactivate();
      d.Deactivate();
      a.Deactivate();
      e.Deactivate();
      c.Activate();
      addSong.load(f);
    } catch (UnsupportedAudioFileException | IOException ex) {
      Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void addSong(Skladba f) {
    try {
      setMyGrafics(addSong.getGui());
      b.Deactivate();
      d.Deactivate();
      a.Deactivate();
      e.Deactivate();
      c.Activate();
      addSong.loadSond(f);
    } catch (UnsupportedAudioFileException | IOException ex) {
      Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
