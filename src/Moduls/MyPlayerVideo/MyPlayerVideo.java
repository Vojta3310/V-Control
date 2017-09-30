/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.Modul;
import VControl.Command;
import VControl.UI.ToolButton;
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
public class MyPlayerVideo extends Modul {

  private final VideoOrganiser VO;
  private int paused;

  public MyPlayerVideo(VControl.Commander Com) throws IOException {
    super(Com);
    VO = new VideoOrganiser(this);
  }

  @Override
  public void Activate() {
    super.Activate(); //To change body of generated methods, choose Tools | Templates.
    VO.reMakeVideo();
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
  public void Execute(Command co) {
    switch (co.GetCommand()) {
      case "Play":
        paused--;
        if (paused <= 0) {
          VO.Play();
        }
        break;
      case "Pause":
        paused++;
        if (paused > 0) {
          VO.Pause();
        }
        break;
      case "Next":
        VO.Next();
        break;
      case "Prew":
        VO.Prew();
        break;
      case "Repeat":
        VO.Repeat();
        break;
      case "ToglePause":
        VO.tooglePause();
        break;
      case "VUp":
        VO.Vup();
        break;
      case "VDown":
        VO.Vdown();
        break;
      case "VSet":
        if (co.GetParms() != null) {
          VO.setVolume((float) co.GetParms());
        }
        break;
    }
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_VideoDir", "/home/vojta3310/Videa/");
    p.setProperty("Modul_" + this.GetModulName() + "_SetingsFile", "/home/vojta3310/Videa/MPSet.cfg");
    p.setProperty("Modul_" + this.GetModulName() + "_Volume_Step", "0.1");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_Volume", "0.5");
  }
  
  @Override
  public Image GetIcon() {
    try {
      return ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerVideo/film.png"));
    } catch (IOException ex) {
      Logger.getLogger(Modul.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }  
  
  @Override
  public void StartModule() {
    try {
      final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerVideo/film.png")));
      final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerVideo/serial.png")));
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
    } catch (Exception e) {
    }
  }

}
