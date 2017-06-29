/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import Moduls.IModul;
import VControl.Settings.AppSettings;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class Sidebar extends JPanel {

  public Sidebar() {
//    this.setAutoscrolls(true);
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setPreferredSize(new Dimension(
      AppSettings.getInt("Icon_Size") + 20,
      AppSettings.getInt("Window_Height")));
    FlowLayout l = new FlowLayout(FlowLayout.LEFT);
    l.setHgap(10);
    l.setVgap(10);
    this.setLayout(l);
  }

  public void addModule(IModul modul) {
    SidebarModule b = new SidebarModule(modul);
    this.add(b);
  }

//  @Override
//  public void paintComponent(Graphics g) {
//    super.paintComponent(g);
//    g.setColor(AppSettings.getColour("FG_Color"));
//    g.fillRect(this.getWidth() - AppSettings.getInt("Border_Size"), 0, 
//      AppSettings.getInt("Border_Size"), this.getHeight());
//  }
}
