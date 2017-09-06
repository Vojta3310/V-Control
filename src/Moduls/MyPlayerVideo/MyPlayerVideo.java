/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.IModul;
import Moduls.Modul;
import VControl.ICommand;
import java.util.Properties;

/**
 *
 * @author vojta3310
 */
public class MyPlayerVideo extends Modul implements IModul {

  private final VideoOrganiser VO;

  public MyPlayerVideo(VControl.Commander Commander) {
    super(Commander);

    VO = new VideoOrganiser(this);

  }

  @Override
  public void Activate() {
    super.Activate(); //To change body of generated methods, choose Tools | Templates.
    VO.reMakeVideo();
  }
  
  @Override
  public boolean doCommand(ICommand co) {
    return false;
  }

  @Override
  public String GetModulName() {
    return "MyPlayerVideo";
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
    p.setProperty("Modul_" + this.GetModulName() + "_VideoDir", "/home/vojta3310/Videa/");
    p.setProperty("Modul_" + this.GetModulName() + "_SetingsFile", "/home/vojta3310/Videa/MPSet.cfg");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Step", "0.1");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_Volume", "0.5");
  }

}
