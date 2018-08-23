/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import VControl.Settings.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class ResolutionUpdater extends Modul {

  public ResolutionUpdater(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "ResolutionUpdater";
  }

  @Override
  public boolean HaveGUI() {
    return false;
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_Command", "xrandr -s %w%x%h%");
  }

  @Override
  public void Execute(Command co) {
    //do nothing
  }

  @Override
  public void StartModule() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    while (width < AppSettings.getInt("Window_Width") || height < AppSettings.getInt("Window_Height")) {
      try {
        String c = SgetString("Command");
        c = c.replace("%w%", AppSettings.getString("Window_Width"));
        c = c.replace("%h%", AppSettings.getString("Window_Height"));
        //System.out.println(c);
        Process p = Runtime.getRuntime().exec(c);
        p.waitFor();
        p.destroy();
      } catch (IOException | InterruptedException ex) {
        Logger.getLogger(Speaker.class.getName()).log(Level.SEVERE, null, ex);
      }
      screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      width = screenSize.getWidth();
      height = screenSize.getHeight();
      getCommander().Execute(null);
    }
  }
}
