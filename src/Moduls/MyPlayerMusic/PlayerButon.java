/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class PlayerButon extends JButton {

  private BufferedImage image;
  private ButonTipe tip;
  MusicOrganiser player;

  public PlayerButon(ButonTipe t, MusicOrganiser pl) {
    tip = t;
    player = pl;
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    switch (tip) {
      case play:
        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/play.png").getImage());
        break;
      case pause:
        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/pause.png").getImage());
        break;
      case next:
        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/next.png").getImage());
        break;

      case prew:
        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/prew.png").getImage());
        break;

    }
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
    }

    this.setPreferredSize(new Dimension(image.getWidth(),
      image.getHeight()));
    this.setMinimumSize(new Dimension(image.getWidth(),
      image.getHeight()));

    this.addActionListener(new java.awt.event.ActionListener() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        switch (tip) {
          case play:
            player.tooglePause();
            break;
          case pause:
            player.tooglePause();
            break;
          case next:
            player.Next();
            break;
          case prew:
            player.Prew();
            break;
        }
      }
    });
  }

//  public void setTip(ButonTipe tip) {
//    switch (tip) {
//      case play:
//        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/play.png").getImage());
//      case pause:
//        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/pause.png").getImage());
//      case next:
//        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/next.png").getImage());
//      case prew:
//        image = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/prew.png").getImage());
//    }
//    if (AppSettings.getBool("Icon_Chanhe_Color")) {
//      utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
//        AppSettings.getColour("FG_Color"));
//    }
//
//    this.setPreferredSize(new Dimension(image.getWidth(),
//      image.getHeight()));
//    this.setSize(new Dimension(image.getWidth(),
//      image.getHeight()));
//  }
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
