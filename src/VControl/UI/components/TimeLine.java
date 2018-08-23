/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.LocalDateTime;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class TimeLine extends JPanel {

  private LocalDateTime currentTime = LocalDateTime.now();
  private LocalDateTime Time = LocalDateTime.now();
  private LocalDateTime min = null;
  private LocalDateTime max = null;
  private LocalDateTime animation = null;
  private LocalDateTime interval = LocalDateTime.of(0, 1, 1, 1, 0);

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    drawDay(0, currentTime, g);
    drawDay(1, currentTime, g);

  }

  private void drawDay(int index, LocalDateTime date, Graphics gf) {
    Graphics2D g = (Graphics2D) gf;
    int height = 48;
    int y = 0;
    int start = index * 6 * 24;
    Color b = Color.darkGray;
    Color e = Color.blue.brighter().brighter().brighter();
    int str = (b.getRed() - e.getRed()) / (3 * 24);
    int stg = (b.getGreen() - e.getGreen()) / (3 * 24);
    int stb = (b.getBlue() - e.getBlue()) / (3 * 24);
    Color actual = b;

    for (int i = 0; i < 24 * 6; i++) {
      g.setColor(actual);
      g.drawLine(start + i, y, start + i, height);
      if (i < 24 * 3) {
        actual = new Color(actual.getRed() - str,
          actual.getGreen() - stg,
          actual.getBlue() - stb);
      } else {
        actual = new Color(actual.getRed()+ str,
          actual.getGreen() + stg,
          actual.getBlue() + stb);
      }
      g.setColor(Color.BLACK);
      if (i % 6 == 0) {
        g.drawLine(start + i, y, start + i, ((i / 6) % 12 == 0) ? 10 : 5);
        g.drawLine(start + i, height - (((i / 6) % 12 == 0) ? 10 : 5), start + i, height);
      }
    }

  }

}
