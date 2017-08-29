/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik;

import VControl.ICommand;
import Moduls.IModul;
import Moduls.Modul;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.io.SerialPort.RXTX;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.skladnik.Robot;
import Moduls.Skladnik.ui.ModulUI.MGUI;
import Moduls.Skladnik.utilities.Settings;
import VControl.UI.ToolButton;
import java.awt.Image;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author vojta3310
 */
public class SkladnikModule extends Modul implements IModul {

  private final RXTX rxtx;
  private final Robot rob;
  private ISklad sklad;
  private XML xml;

  public SkladnikModule(VControl.Commander Commander) throws IOException {
    super(Commander);
    xml = new XML(Settings.file);
    try {
      sklad = xml.read();
    } catch (ParserConfigurationException | SAXException ex) {
      Logger.getLogger(SkladnikModule.class.getName()).log(Level.SEVERE, null, ex);
    }

    
    rxtx = new RXTX();
    if (rxtx.connect() == -1) {
//      JOptionPane.showMessageDialog(super.getGrafics(), "Skladník: \nSériový port nebyl nalezen!");
      System.out.println("---------------");
      System.out.println("-Using FakeIO!-");
      System.out.println("---------------");
    }
    rob = new Robot(sklad, rxtx, xml);
    rob.setDaemon(true);
    final MGUI gui = new MGUI(sklad, rob, xml,getMyGrafics());

    final ToolButton b = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_podat_32px.png")));
    final ToolButton c = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_vlozit_32px.png")));
    final ToolButton d = new ToolButton(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Skladnik/dark/icon_pridat_32px.png")));
    b.Activate();
    b.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        c.Deactivate();
        d.Deactivate();
        b.Activate();
        gui.Podavani();
        repaint();
      }
    });
    c.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        b.Deactivate();
        d.Deactivate();
        c.Activate();
        gui.Vkladani();
        repaint();
      }
    });
    d.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        b.Deactivate();
        c.Deactivate();
        d.Activate();
        gui.Editovani();
        repaint();
      }
    });
    super.getToolBar().addTool(b);
    super.getToolBar().addTool(c);
    super.getToolBar().addTool(d);

  }

  public void StartRob() {
    rob.start();
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
  public void Execute(ICommand co) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_File", "home/vojta3310/Sklad.xml");
    p.setProperty("Modul_" + this.GetModulName() + "_Serial_Port", "/dev/ttyUSB0");
    p.setProperty("Modul_" + this.GetModulName() + "_Sleep_Dylay", "10");
    p.setProperty("Modul_" + this.GetModulName() + "_Favour_Moving_Item", "true");
  }

}
