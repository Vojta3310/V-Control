/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.ICommand;
import Moduls.IModul;
import Moduls.Modul;
import VControl.UI.ToolButton;
import java.awt.Image;
import java.io.FileInputStream;
import java.util.Properties;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;

/**
 *
 * @author vojta3310
 */
public class MyPlayerMusic extends Modul implements IModul {
  private final MusicOrganiser Player;
  
  public MyPlayerMusic(VControl.Commander Commander) throws LineUnavailableException {
    super(Commander);
    ToolButton b = new ToolButton(this.GetIcon());
    b.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        onClick();
      }
    });
    super.getToolBar().addTool(b);
    Player=new MusicOrganiser();
    super.setMyGrafics(Player.getGui());
  }

  @Override
  public final ImageIcon GetIcon() {
    return new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/modul.png");
  }

  public void onClick() {
    System.out.println("Clicked me");
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
  }

}
