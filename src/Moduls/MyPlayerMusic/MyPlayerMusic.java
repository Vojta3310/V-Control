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
import com.sun.org.apache.xpath.internal.functions.Function;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author vojta3310
 */
public class MyPlayerMusic extends Modul implements IModul {

  public MyPlayerMusic(VControl.Commander Commander) {
    super(Commander);
    ToolButton b = new ToolButton(this.GetIcon());
    b.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        onClick();
      }
    });
    super.getToolBar().addTool(b);
  }

  @Override
  public ImageIcon GetIcon() {
    return new javax.swing.ImageIcon(getClass().getResource("/Moduls/MyPlayerMusic/Icons/modul.png"));
  }

  public void onClick() {
    System.out.println("Clicked me");
  }

  @Override
  public boolean doCommand(ICommand co) {
    return false;
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
    ;
  }

}
