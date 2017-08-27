/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class Stars extends JPanel {

  private final BufferedImage emty;
  private final BufferedImage full;
  private int maxStars = 5;
  private float Stars = 2.5F;
  private boolean editabe = false;

  public Stars() {
    emty = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/EmtyStar.png").getImage());
    full = utiliti.toBufferedImage(new javax.swing.ImageIcon("res/icons/modules/MyPlayerMusic/FullStar.png").getImage());

    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      utiliti.changeColor(emty, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
      utiliti.changeColor(full, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
    }

    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (editabe) {
          Stars = (float) ((float) e.getX() / getWidth() * (float) maxStars);
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
//    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    g.setColor(AppSettings.getColour("BG_Color"));
    g.fillRect(0, 0, getWidth(), getHeight());

    int gap = (getWidth() - full.getWidth() * maxStars) / (maxStars + 1);
    for (int i = 0; i < maxStars; i++) {
      g.drawImage(
        full,
        gap + (full.getWidth() + gap) * i,
        (getHeight() - full.getHeight()) / 2,
        this
      );
    }

    g.fillRect(getWidth() - (int) (getWidth() * ((maxStars - Stars) / maxStars)), 0, getWidth(), getHeight());

//    gap = (getWidth() - emty.getWidth() * maxStars) / (maxStars + 1);
    for (int i = 0; i < maxStars; i++) {
      g.drawImage(
        emty,
        gap + (emty.getWidth() + gap) * i,
        (getHeight() - emty.getHeight()) / 2,
        this
      );
    }
  }

  public int getMaxStars() {
    return maxStars;
  }

  public void setMaxStars(int maxStars) {
    this.maxStars = maxStars;
  }

  public float getStars() {
    return Stars;
  }

  public void setStars(float Stars) {
    this.Stars = Stars;
  }

  public boolean isEditabe() {
    return editabe;
  }

  public void setEditabe(boolean editabe) {
    this.editabe = editabe;
  }

}
