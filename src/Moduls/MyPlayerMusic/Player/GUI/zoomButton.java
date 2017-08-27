/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class zoomButton extends JButton {

  private final BufferedImage image;

  public zoomButton(String i) throws IOException {

    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/" + i + ".png")));

    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
    }

    this.setPreferredSize(new Dimension(image.getWidth(),
      image.getHeight()));
    this.setMinimumSize(new Dimension(image.getWidth(),
      image.getHeight()));

  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setPaint(AppSettings.getColour("BG_Color"));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setPaint(AppSettings.getColour("FG_Color"));

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
