/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import Moduls.IModul;
import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class SidebarModule extends JButton {

  private final IModul modul;

  public SidebarModule(final IModul modul) {
    this.modul = modul;
    this.setBackground(AppSettings.getColour("BG_Color"));
    ImageIcon ico = modul.GetIcon();
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      BufferedImage a = utiliti.toBufferedImage(ico.getImage());
      utiliti.changeColor(a, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
      ico.setImage(a);
    }
    this.setIcon(ico);
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), 0));
    this.setPreferredSize(new Dimension(AppSettings.getInt("Icon_Size"),
      AppSettings.getInt("Icon_Size")));
    this.modul.setButton(this);

    this.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        modul.Activate();
      }
    });
  }

  public void Activate() {
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), AppSettings.getInt("Border_Size")));
  }

  public void Deactivate() {
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), 0));
  }

}
