/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import ddf.minim.AudioPlayer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class VolumeControl extends JPanel {

  private int lines = 7;
  private AudioPlayer ap;
  private float volume = 0.5F;

  public VolumeControl() {
    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        volume = (float) e.getX() / (float) getWidth();
        repaint();
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

    g2d.setColor(AppSettings.getColour("BG_Color"));
    g2d.fillRect(0, 0, getWidth(), getHeight());

    g2d.setColor(AppSettings.getColour("FG_Color"));
//    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    int width = getWidth() / (lines * 2);
    int height = (getHeight() - 8 - width) / (lines + 1);
    for (int i = 0; i < lines; i++) {
      g2d.fillRect(i * 2 * width + width / 2, lines * height, width, width);
      if (i < lines * volume) {
        g2d.fillRect(i * 2 * width + width / 2, (lines - i) * height, width, (i * height));
      }
    }
    if (ap != null) {
      g2d.setColor(Color.GRAY);
      g2d.fillRect(width / 2, getHeight() - 6, (getWidth() - width), 6);
      g2d.setColor(AppSettings.getColour("FG_Color"));
      g2d.fillRect(width / 2, getHeight() - 6, (int) (ap.mix.level() * volume * 3 * (getWidth() - width)), 6);
    }
  }

  public int getLines() {
    return lines;
  }

  public void setLines(int lines) {
    this.lines = lines;
  }

  public AudioPlayer getAp() {
    return ap;
  }

  public void setAp(AudioPlayer ap) {
    this.ap = ap;
  }

  public float getVolume() {
    return volume;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

}
