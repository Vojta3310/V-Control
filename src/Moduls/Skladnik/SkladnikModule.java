/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik;

import VControl.Command;
import Moduls.Modul;
import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.Enums.typOperace;
import Moduls.Skladnik.io.SerialPort.RXTX;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.skladnik.Robot;
import Moduls.Skladnik.ui.ModulUI.MGUI;
import Moduls.Skladnik.ui.graphics.QRCframe;
import VControl.Settings.AppSettings;
import VControl.UI.ToolButton;
import java.awt.Image;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vojta3310
 */
public class SkladnikModule extends Modul {

  private RXTX rxtx;
  private Robot rob;
  private ISklad sklad;
  private XML xml;

  public SkladnikModule(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public final Image GetIcon() {
    try {
      return ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/modul.png"));
    } catch (IOException ex) {
      Logger.getLogger(SkladnikModule.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public String GetModulName() {
    return "Skladnik";
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void Execute(Command co) {
    switch (co.GetCommand()) {
      case "Bring":
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Bring id: {0}", co.GetParms().toString());
        if (co.GetParms() != null) {
          Box b = sklad.getBoxByID((int) co.GetParms());
          if (b != null && rob != null) {
            Logger.getLogger(this.getClass().getName()).log(Level.FINE,"ID finded bringing...");
            b.setStav(typOperace.PODEJ);
            rob.addToBuffer(b);
          }
        }
        break;
      case "PutIn":  //contains error
        if (co.GetParms() != null && rob != null) {
          Box b = sklad.getBoxByID((int) co.GetParms());
          if (b != null) {
            b.setStav(typOperace.VLOZ);
            rob.addToBuffer(b);
          }
        }
        break;
      case "List":
        co.setResults(sklad.getList());
        break;
    }

  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_File", "home/vojta3310/Sklad.xml");
    p.setProperty("Modul_" + this.GetModulName() + "_Serial_Port", "/dev/ttyUSB0");
    p.setProperty("Modul_" + this.GetModulName() + "_Sleep_Dylay", "10");
    p.setProperty("Modul_" + this.GetModulName() + "_Favour_Moving_Item", "true");
  }

  @Override
  public void StartModule() {
    try {
      getMyGrafics().setBackground(AppSettings.getColour("BG_Color"));
      JLabel nc= new JLabel("Loading XML ...");
      nc.setForeground(AppSettings.getColour("FG_Color"));
      getMyGrafics().add(nc);
      xml = new XML(SgetString("File"));
      try {
        sklad = xml.read();
      } catch (ParserConfigurationException | SAXException ex) {
        Logger.getLogger(SkladnikModule.class.getName()).log(Level.SEVERE, null, ex);
      }
      nc.setText("Connecting ...");
      rxtx = new RXTX(SgetString("Serial_Port"));
      while (rxtx.connect() == -1) {
        nc.setText("!STORAGE NOT CONECTED!");
        Thread.sleep(3333);
      }
      nc.setText("Connected.");
      rob = new Robot(sklad, rxtx, xml, SgetBool("Favour_Moving_Item"), SgetInt("Sleep_Dylay"), this);
      rob.setDaemon(true);
      getMyGrafics().remove(nc);
      final MGUI gui = new MGUI(sklad, rob, xml, getMyGrafics());
      final QRCframe qrf=new QRCframe();
      
      final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_podat_32px.png")));
      final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_vlozit_32px.png")));
      final ToolButton d = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_pridat_32px.png")));
      final ToolButton a = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_QR.png")));
      b.Activate();
      a.addActionListener((java.awt.event.ActionEvent evt) -> {
        qrf.UpdateModel(sklad);
        qrf.revalidate();
        qrf.repaint();
        qrf.setVisible(true);
      });
      b.addActionListener((java.awt.event.ActionEvent evt) -> {
        c.Deactivate();
        d.Deactivate();
        b.Activate();
        gui.Podavani();
        repaint();
      });
      c.addActionListener((java.awt.event.ActionEvent evt) -> {
        b.Deactivate();
        d.Deactivate();
        c.Activate();
        gui.Vkladani();
        repaint();
      });
      d.addActionListener((java.awt.event.ActionEvent evt) -> {
        b.Deactivate();
        c.Deactivate();
        d.Activate();
        gui.Editovani();
        repaint();
      });
      super.getToolBar().addTool(b);
      super.getToolBar().addTool(c);
      super.getToolBar().addTool(d);
      super.getToolBar().addTool(a);

      rob.start();
      this.getMyGrafics().revalidate();
      this.getMyGrafics().repaint();
    } catch (IOException | InterruptedException e) {
    }
  }

}
