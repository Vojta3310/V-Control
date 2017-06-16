/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import Moduls.IModul;
import VControl.Settings.AppSettings;
import VControl.Settings.Settings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class ToolBox extends JPanel {

  public ToolBox() {
    this.setAutoscrolls(true);
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setPreferredSize(new Dimension(AppSettings.getInt("Window_Width")
      - (AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size")),
      AppSettings.getInt("Icon_Size") + 20));
    FlowLayout l = new FlowLayout(FlowLayout.LEFT);
    l.setHgap(10);
    l.setVgap(10);
    this.setLayout(l);

  }

  public void addTool(ToolButton b) {
    this.add(b);
    this.setPreferredSize(new Dimension(
      AppSettings.getInt("Icon_Size") + 20,
      (AppSettings.getInt("Icon_Size") + 10) * this.getComponentCount() + 10));
  }
}
