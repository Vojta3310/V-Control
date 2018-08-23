/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Commander;
import VControl.Command;
import VControl.Settings.AppSettings;
import VControl.Settings.Settings;
import VControl.UI.SidebarModule;
import VControl.UI.ToolBox;
import VControl.UI.components.MyScrollPanelLayout;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author vojta3310
 */
public abstract class Modul extends Thread implements IModul {

  private final Commander Commander;
  private final JPanel FullGrafics;
  private JPanel MyGrafics;
  private final ToolBox ToolBar;
  private SidebarModule sidebarButton;
  private final ArrayList<Command> Commands = new ArrayList<>();

  public Modul(Commander Commander) {
    this.Commander = Commander;
    this.FullGrafics = new JPanel();
    this.MyGrafics = new JPanel();
    this.ToolBar = new ToolBox();
  }

  @Override
  public void run() {
    JScrollPane a = new JScrollPane(this.ToolBar);
    a.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    a.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    a.setViewportBorder(null);
    a.setBorder(createEmptyBorder());
    a.setSize(new Dimension(AppSettings.getInt("Window_Width")
      - (AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size")),
      AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size")));
    a.setLayout(new MyScrollPanelLayout());
    JScrollBar sb = a.getHorizontalScrollBar();
    sb.setPreferredSize(new Dimension(Integer.MAX_VALUE, AppSettings.getInt("Border_Size")));
    sb.setUI(new MyScrollbarUI());
    this.FullGrafics.setLayout(new BorderLayout());
    this.FullGrafics.add(MyGrafics, BorderLayout.CENTER);
    this.FullGrafics.add(a, BorderLayout.PAGE_END);
    this.MyGrafics.setBackground(AppSettings.getColour("BG_Color"));
    new ModulTimer(this).start();
//    Timer tim = new Timer(10, (ActionEvent ae) -> {
//      if (!Commands.isEmpty()) {
//        Commands.get(0).setStats(CommandStats.InProgress);
//        this.Execute(Commands.get(0));
//        Commands.get(0).setStats(CommandStats.Done);
//        Commands.remove(0);
//      }
//    });
//    tim.start();
    this.StartModule();
  }

  @Override
  public void Activate() {
    this.Commander.Activate(this);
    this.sidebarButton.Activate();
  }

  @Override
  public void Deactivate() {
    this.sidebarButton.Deactivate();
  }

  @Override
  public Image GetIcon() {
    try {
      return ImageIO.read(getClass().getResourceAsStream("/icons/modules/none.png"));
    } catch (IOException ex) {
      Logger.getLogger(Modul.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  @Override
  public JPanel getGrafics() {
    return FullGrafics;
  }

  @Override
  public void DefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_enabled", "true");
    this.getDefaultSettings(p);
  }

  public String SgetString(String key) {
    if (Settings.p.containsKey("Modul_" + this.GetModulName() + "_" + key)) {
      return Settings.p.getProperty("Modul_" + this.GetModulName() + "_" + key);
    }
    Properties p = new Properties();
    DefaultSettings(p);
    return p.getProperty("Modul_" + this.GetModulName() + "_" + key);
  }

  public int SgetInt(String key) {
    return Integer.parseInt(SgetString(key));
  }

  public boolean SgetBool(String key) {
    return Boolean.parseBoolean(SgetString(key));
  }

  public void Sset(String key, String value) {
    Settings.p.setProperty("Modul_" + this.GetModulName() + "_" + key, value);
  }

  @Override
  public void setButton(SidebarModule sidebatButton) {
    this.sidebarButton = sidebatButton;
  }

  @Override
  public SidebarModule getSidebarButton() {
    return sidebarButton;
  }

  public Commander getCommander() {
    return Commander;
  }

  public JPanel getMyGrafics() {
    return MyGrafics;
  }

  public ToolBox getToolBar() {
    return ToolBar;
  }

  public ArrayList<Command> getCommands() {
    return Commands;
  }

  public void setMyGrafics(JPanel MyGrafic) {
    this.FullGrafics.remove(MyGrafics);
    this.MyGrafics = MyGrafic;
    this.FullGrafics.add(MyGrafics, BorderLayout.CENTER);
    this.FullGrafics.repaint();
    this.FullGrafics.revalidate();
  }

  public void repaint() {
    this.FullGrafics.repaint();
    this.FullGrafics.revalidate();
  }

  @Override
  public synchronized void doCommand(Command co) {
    if (co.GetFor().equals(this.GetModulName()) || co.GetFor().equals("all")) {
      Commands.add(co);
    }
  }

}
