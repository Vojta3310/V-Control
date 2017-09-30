/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import VControl.Command;
import Moduls.Modul;
import Moduls.MyPlayerMusic.songAdder.addFromFile;
import VControl.UI.ToolButton;
import java.awt.Image;
import java.io.IOException;
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
  private int paused = 0;

  public MyPlayerMusic(VControl.Commander Commander) throws LineUnavailableException, IOException, TagException, UnsupportedAudioFileException {
    super(Commander);
    Player = new MusicOrganiser(this);
    addSong = new addFromFile(this);
    edit = new EditSong(this, Player.getSongs());
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
        paused--;
        if (paused <= 0) {
          Player.Play();
        }
        break;
      case "Pause":
        paused++;
        if (paused > 0) {
          Player.Pause();
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
    }
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_MusicDir", "/home/vojta3310/Hudba/MP/");
    p.setProperty("Modul_" + this.GetModulName() + "_Transfet_Lenght", "1000");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Step", "0.1");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_Volume", "0.5");
  }

  @Override
  public void StartModule() {
    try {
      final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/PlayFile.png")));
      final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/addFromFile.png")));
      final ToolButton d = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/EditSong.png")));
      b.Activate();
      b.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          c.Deactivate();
          d.Deactivate();
          b.Activate();
          setMyGrafics(Player.getGui());
        }
      });
      c.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          b.Deactivate();
          d.Deactivate();
          c.Activate();
          setMyGrafics(addSong.getGui());
        }
      });
      d.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          b.Deactivate();
          c.Deactivate();
          d.Activate();
          setMyGrafics(edit.getGui());
        }
      });

      super.getToolBar().addTool(b);
      super.getToolBar().addTool(c);
      super.getToolBar().addTool(d);
    } catch (IOException ex) {
      Logger.getLogger(MyPlayerMusic.class.getName()).log(Level.SEVERE, null, ex);
    }
    super.setMyGrafics(Player.getGui());
  }

}
