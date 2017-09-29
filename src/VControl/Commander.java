/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import VControl.Settings.Settings;
import Moduls.IModul;
import VControl.Settings.AppSettings;
import VControl.UI.GUI;
import VControl.UI.Sidebar;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Properties;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author vojta3310
 */
public class Commander {

  private final ArrayList<IModul> moduls;
  private final Sidebar sidebar;
  private IModul active;
  private final GUI gui;
  private final Settings setings;
  private final JScrollPane scSidebar;

  public Commander() {
    this.moduls = new ArrayList<>();
    this.setings = new Settings(this);
    this.setings.load();
    this.gui = new GUI();
    this.sidebar = new Sidebar();
    this.scSidebar = new JScrollPane(this.sidebar);
    this.scSidebar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.scSidebar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.scSidebar.setViewportBorder(null);
    this.scSidebar.setBorder(createEmptyBorder());
    JScrollBar sb = scSidebar.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());
    gui.add(scSidebar, BorderLayout.LINE_START);
    gui.repaint();
  }

  public boolean Execute(Command co) {
    moduls.stream().forEach((a) -> {
      a.doCommand(co);
    });
    this.setings.doCommand(co);
    return true;
  }

  public void Activate(IModul c) {
    if (this.active != null) {
      active.Deactivate();
      gui.remove(active.getGrafics());
    }
    active = c;
    gui.add(c.getGrafics(), BorderLayout.CENTER);
    this.scSidebar.getVerticalScrollBar().setValue(c.getSidebarButton().getY()
      - AppSettings.getInt("Window_Height") / 2 + AppSettings.getInt("Icon_Size"));
    c.getGrafics().repaint();
    gui.repaint();
    gui.revalidate();
  }

  public void getDefaultSettings(Properties p) {
    moduls.stream().forEach((a) -> {
      a.DefaultSettings(p);
    });
    AppSettings.DefaultSettings(p);
  }

  public IModul getActive() {
    return active;
  }

  public ArrayList getIModuls() {
    return moduls;
  }

    public void StartModules() {
    for (IModul a : moduls) {
      a.start();
    }
  }  
  
  public void RegisterGUI() {
    for (IModul a : moduls) {
      if (a.HaveGUI()) {
        this.sidebar.addModule(a);
        this.sidebar.setPreferredSize(new Dimension(
          AppSettings.getInt("Icon_Size") + 20,
          (AppSettings.getInt("Icon_Size") + 10) * this.moduls.size() + 10));
      }
    }
  }

  public void addIModule(IModul d) {
    String e = Settings.p.getProperty("Modul_" + d.GetModulName() + "_enabled");
    if (e == null) {
      d.DefaultSettings(Settings.p);
      this.setings.save();
      this.moduls.add(d);
    } else if (e.equals("true")) {
      this.moduls.add(d);
    }
  }
}
