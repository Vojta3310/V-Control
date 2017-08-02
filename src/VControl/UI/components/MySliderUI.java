/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 *
 * @author vojta3310
 */
public class MySliderUI extends BasicSliderUI {

  public MySliderUI(JSlider b) {
    super(b);
  }

  @Override
  protected Dimension getThumbSize() {
    return new Dimension(16, 16);
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    //super.paint(g, c);
    paintTrack(g);
    paintThumb(g);
  }

  @Override
  public void paintTrack(Graphics g) {
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRoundRect(trackRect.x, 3, trackRect.width, 10, 8, 8);
    g.setColor(Color.GRAY);
    g.fillRoundRect(trackRect.x + 3, 6, trackRect.width - 6, 4, 5, 5);
  }

  @Override
  public void paintThumb(Graphics g) {
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillOval(thumbRect.x, 0, 16, 16);
    g.setColor(Color.GRAY);
    g.fillOval(thumbRect.x + 2, 2, 16 - 4, 16 - 4);
  }

}
