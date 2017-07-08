/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Commander;
import VControl.ICommand;
import VControl.Settings.AppSettings;
import VControl.Settings.Settings;
import VControl.UI.SidebarModule;
import VControl.UI.ToolBox;
import VControl.UI.components.MyScrollPanelLayout;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author vojta3310
 */
public abstract class Modul implements IModul {

  private final Commander Commander;
  private final JPanel FullGrafics;
  private JPanel MyGrafics;
  private final ToolBox ToolBar;
  private SidebarModule sidebarButton;

  public Modul(Commander Commander) {
    this.Commander = Commander;
    this.FullGrafics = new JPanel();
    this.MyGrafics = new JPanel();
    this.ToolBar = new ToolBox();
    JScrollPane a = new JScrollPane(this.ToolBar);
    a.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    a.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    a.setViewportBorder(null);
    a.setBorder(createEmptyBorder());
    a.setSize(new Dimension(AppSettings.getInt("Window_Width")
      - (AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size")),
      AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size")));
//    a.setLayout(null);            //nejdriv si nastav layout na null (pak na to ale nezapomen)
//        a.getHorizontalScrollBar().setSize(100, 15); //potom nastav i velikost (jak nastavis rozlozeni na null, vsechno delas rucne
//        a.getHorizontalScrollBar().setLocation(0, 0);

    a.setLayout(new MyScrollPanelLayout());
    JScrollBar sb = a.getHorizontalScrollBar();
    sb.setPreferredSize(new Dimension(Integer.MAX_VALUE, AppSettings.getInt("Border_Size")));
    sb.setUI(new MyScrollbarUI());
    this.FullGrafics.setLayout(new BorderLayout());
    this.FullGrafics.add(MyGrafics, BorderLayout.CENTER);
    this.FullGrafics.add(a, BorderLayout.PAGE_END);
    this.MyGrafics.setBackground(AppSettings.getColour("BG_Color"));
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
  public ImageIcon GetIcon() {
    return new javax.swing.ImageIcon("res/icons/modules/video/console.png");
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
    return Settings.p.getProperty("Modul_" + this.GetModulName() + "_" + key);
  }

  public int SgetInt(String key) {
    return Integer.parseInt(Settings.p.getProperty("Modul_" + this.GetModulName() + "_" + key));
  }

  public boolean SgetBool(String key) {
    return Boolean.parseBoolean(Settings.p.getProperty("Modul_" + this.GetModulName() + "_" + key));
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
  public boolean doCommand(ICommand co) {
    if (co.GetFor().equals(this.GetModulName())) {
      this.Execute(co);
    }
    return true;
  }

}
