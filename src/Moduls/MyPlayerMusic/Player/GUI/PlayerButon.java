/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
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
public class PlayerButon extends JButton {

  private BufferedImage image;
  private BufferedImage image2;
  private ButonTipe tip;
  MusicOrganiser player;

  public PlayerButon(ButonTipe t, MusicOrganiser pl) {
    tip = t;
    player = pl;
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    
    try {
      switch (tip) {
        case play_pause:
          image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/play.png")));
          image2 = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/pause.png")));
          break;
        case next:
          image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/next.png")));
          break;
        case prew:
          image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/prew.png")));
          break;
        case repeat:
          image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/repeat.png")));
          break;
      }
    } catch (IOException iOException) {
    }
    
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
      if (tip == ButonTipe.play_pause) {
        utiliti.changeColor(image2, AppSettings.getColour("BG_Color"),
          AppSettings.getColour("FG_Color"));
      }
    }

    this.setPreferredSize(new Dimension(image.getWidth(),
      image.getHeight()));
    this.setMinimumSize(new Dimension(image.getWidth(),
      image.getHeight()));

    this.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        switch (tip) {
          case play_pause:
            player.tooglePause();
            break;
          case next:
            player.Next();
            break;
          case prew:
            player.Prew();
            break;
          case repeat:
            player.Repeat();
            break;
        }
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setPaint(AppSettings.getColour("BG_Color"));
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.setPaint(AppSettings.getColour("FG_Color"));

    if (image != null) {
      if ((tip == ButonTipe.play_pause) && !player.getAplayer().getPaused()) {
        g2d.drawImage(
          image2,
          (getWidth() - image2.getWidth()) / 2,
          (getHeight() - image2.getHeight()) / 2,
          this
        );
      } else {
        g2d.drawImage(
          image,
          (getWidth() - image.getWidth()) / 2,
          (getHeight() - image.getHeight()) / 2,
          this
        );
      }
    }
  }
}
