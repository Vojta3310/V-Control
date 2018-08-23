/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Weader;

import Moduls.Modul;
import Moduls.Skladnik.SkladnikModule;
import VControl.Command;
import VControl.UI.components.TimeLine;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author vojta3310
 */
public class WeaderModule extends Modul {

  public WeaderModule(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "Weader";
  }

  @Override
  public Image GetIcon() {
    try {
      return ImageIO.read(getClass().getResourceAsStream("/icons/modules/Weader/Weader.png"));
    } catch (IOException ex) {
      Logger.getLogger(SkladnikModule.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void getDefaultSettings(Properties p) {

  }

  @Override
  public void Execute(Command co) {

  }

  @Override
  public void StartModule() {
    Radar r =new Radar();
    r.setPreferredSize(new Dimension(640, 400));
    getMyGrafics().add(r);
    TimeLine t = new TimeLine();
//    getMyGrafics().add(t);
    t.setPreferredSize(new Dimension(640, 48));
  }

}
