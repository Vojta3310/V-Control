/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

/**
 *
 * @author vojta3310
 */
public class MyScrollbarUI extends BasicScrollBarUI {

  private JButton b = new JButton() {
    @Override
    public Dimension getPreferredSize() {
      return new Dimension(0, 0);
    }
  };

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return b;
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return b;
  }

  @Override
  protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRect(r.x, r.y, r.width, r.height);
  }

  @Override
  protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    Color a = AppSettings.getColour("FG_Color");
    g.setColor(a.darker());
    if (r.width < r.height) {
      g.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, (r.width - 2), (r.width - 2));
    }
    if (r.width > r.height) {
      g.fillRoundRect(r.x, r.y + 1, r.width, r.height - 2, (r.height - 2), (r.height - 2));
    }
    if (r.width == r.height) {
      g.fillRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2);
    }
  }
}
