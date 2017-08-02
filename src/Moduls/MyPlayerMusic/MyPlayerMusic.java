/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import VControl.ICommand;
import Moduls.IModul;
import Moduls.Modul;
import Moduls.MyPlayerMusic.songAdder.addFromFile;
import VControl.UI.ToolButton;
import java.io.IOException;
import java.util.Properties;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class MyPlayerMusic extends Modul implements IModul {
  private final MusicOrganiser Player;
  private final addFromFile addSong;
  
  public MyPlayerMusic(VControl.Commander Commander) throws LineUnavailableException, IOException, TagException, UnsupportedAudioFileException {
    super(Commander);
    Player=new MusicOrganiser(this);
    addSong=new addFromFile(this);
    final ToolButton b = new ToolButton(this.GetIcon());
    final ToolButton c = new ToolButton(this.GetIcon());
    b.Activate();
    b.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        c.Deactivate();
        b.Activate();
        setMyGrafics(Player.getGui());
      }
    });
    c.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        b.Deactivate();
        c.Activate();
        setMyGrafics(addSong.getGui());
      }
    });
    super.getToolBar().addTool(b);
    super.getToolBar().addTool(c);
    super.setMyGrafics(Player.getGui());
  }
  
  public void reloadSongs() throws IOException, TagException{
    Player.reloadSongs();
  }

  @Override
  public final ImageIcon GetIcon() {
    return new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/modul.png");
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
  public void Execute(ICommand co) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_MusicDir", "/home/vojta3310/Hudba/MP/");
    p.setProperty("Modul_" + this.GetModulName() + "_Transfet_Lenght", "1000");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Step", "0.1");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_Volume", "0.5");
  }

}
