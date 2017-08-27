/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class ToolButton extends JButton {

  private boolean active = false;
  private final BufferedImage image;

  public ToolButton(Image ico) {

    image = utiliti.toBufferedImage(ico);
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
    }
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    this.setPreferredSize(new Dimension(AppSettings.getInt("Icon_Size"),
      AppSettings.getInt("Icon_Size")));
    this.setBorderPainted(false);

  }

  public void Activate() {
    this.active = true;
//    this.setBorder(javax.swing.BorderFactory.createLineBorder(
//      AppSettings.getColour("FG_Color"), AppSettings.getInt("Border_Size")));
  }

  public void Deactivate() {
    this.active = false;
//    this.setBorder(javax.swing.BorderFactory.createLineBorder(
//      AppSettings.getColour("FG_Color"), 0));
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setPaint(AppSettings.getColour("BG_Color"));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setPaint(AppSettings.getColour("FG_Color"));
    if (this.active) {
      for (int i = 0; i < 3; i++) {
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
