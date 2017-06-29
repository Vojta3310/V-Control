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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class SidebarModule extends JButton {

  private final IModul modul;
  private boolean active = false;
  private final BufferedImage image;

  public SidebarModule(final IModul modul) {
    this.modul = modul;
    this.setBackground(AppSettings.getColour("BG_Color"));
    ImageIcon ico = modul.GetIcon();
    
    image = utiliti.toBufferedImage(ico.getImage());
    
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      
      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
    }
    
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
    active = true;
  }

  public void Deactivate() {
    active = false;
  }
  
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setPaint(AppSettings.getColour("BG_Color"));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setPaint(AppSettings.getColour("FG_Color"));
    if (this.active) {
      for (int i = 0; i < AppSettings.getInt("Border_Size"); i++) {
        g2d.drawRect(i, i, getWidth() - 2 * i, getHeight() - 2 * i);
      }
    }

    if (image != null) {
      g2d.drawImage(
        image,
        (getWidth() - image.getWidth()) / 2,
        (getHeight() - image.getHeight()) / 2,
        this
      );
    }
  }

}
