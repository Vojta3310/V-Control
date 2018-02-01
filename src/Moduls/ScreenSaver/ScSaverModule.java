/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.ScreenSaver;

import Moduls.IModul;
import Moduls.Modul;
import VControl.Command;
import VControl.Settings.AppSettings;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Properties;
import javax.swing.Timer;

/**
 *
 * @author vojta3310
 */
public class ScSaverModule extends Modul {

  private int booringTime = 0;
  private IModul last = this;
  private boolean Shown = false;
  private boolean Enabled = true;
  
  public ScSaverModule(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "ScreenSaver";
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_ImageDir", "/home/vojta3310/Obrázky/Walpapapers/");
    p.setProperty("Modul_" + this.GetModulName() + "_Transfet_Lenght", "5");
    p.setProperty("Modul_" + this.GetModulName() + "_Start_Delay(s)", "30");
    p.setProperty("Modul_" + this.GetModulName() + "_Image_Delay(s)", "60");
  }

  @Override
  public void Execute(Command co) {
    if (co.GetCommand().equals("Enable")) Enabled=true;
    if (co.GetCommand().equals("Disable")) Enabled=false;
    if (Shown) {
      getCommander().Execute(new Command("ShowSidebar", "app", GetModulName()));
      if (last != this) {
        last.Activate();
      }
      Shown = false;
    }
    booringTime = 0;
  }

  @Override
  public void StartModule() {
    super.getGrafics().removeAll();
    super.getGrafics().add(new SaverPlane(this));
    Timer tim = new Timer(1000, (ActionEvent ae) -> {
      booringTime++;
      if (booringTime > SgetInt("Start_Delay(s)") && Enabled) {
        this.Activate();
      }
    });
    tim.start();
  }

  @Override
  public synchronized void Activate() {
    if (!Shown&&Enabled) {
      last = getCommander().getActive();
      Shown = true;
      getCommander().Execute(new Command("HideSidebar", "app", GetModulName()));
      super.Activate();
      super.getGrafics().setSize(new Dimension(AppSettings.getInt("Window_Width"),
        AppSettings.getInt("Window_Height")));

    }

  }

}
