/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.IModul;
import Moduls.Modul;
import VControl.ICommand;
import VControl.UI.ToolButton;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author vojta3310
 */
public class MyPlayerVideo extends Modul implements IModul {

  private final VideoOrganiser VO;

  public MyPlayerVideo(VControl.Commander Commander) throws IOException {
    super(Commander);

    VO = new VideoOrganiser(this);

    final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/PlayFile.png")));
    final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/addFromFile.png")));
    b.Activate();
    b.addActionListener((java.awt.event.ActionEvent evt) -> {
      c.Deactivate();
      b.Activate();
      try {
        VO.showFilm();
      } catch (IOException ex) {
        Logger.getLogger(MyPlayerVideo.class.getName()).log(Level.SEVERE, null, ex);
      }
      super.getGrafics().revalidate();
      super.getGrafics().repaint();
    });
    c.addActionListener((java.awt.event.ActionEvent evt) -> {
      b.Deactivate();
      c.Activate();
      try {
        VO.showSerial();
      } catch (IOException ex) {
        Logger.getLogger(MyPlayerVideo.class.getName()).log(Level.SEVERE, null, ex);
      }
      super.getGrafics().revalidate();
      super.getGrafics().repaint();
    });

    super.getToolBar().addTool(b);
    super.getToolBar().addTool(c);

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
