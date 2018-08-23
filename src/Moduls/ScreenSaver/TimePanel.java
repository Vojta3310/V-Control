/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.ScreenSaver;

import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author vojta3310
 */
public class TimePanel extends JPanel {

  private final JLabel Time;
  private final JLabel Second;
  private final JLabel Date;
  private Color color = Color.DARK_GRAY;

  public TimePanel() {
    Time = new JLabel("00:00");
    Second = new JLabel("00");
    Date = new JLabel("pondělí, 1. leden 0000");

    Time.setForeground(Color.white);
    Second.setForeground(Color.white);
    Date.setForeground(Color.white);
    Font f = new Font(AppSettings.getString("Font_Name"), 0, (int) 4 * AppSettings.getInt("Font_Size"));
    Time.setFont(f.deriveFont(1, (float) (10 * AppSettings.getInt("Font_Size"))));
    Second.setFont(f);
    Date.setFont(f);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(Time)
            .addGap(18, 18, 18)
            .addComponent(Second))
          .addComponent(Date))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Time)
          .addComponent(Second))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(Date)
        .addContainerGap(18, Short.MAX_VALUE))
    );
    Timer tim = new Timer(103, (ActionEvent ae) -> {
      updateTime();
    }
    );
    tim.start();
  }

  private void updateTime() {
    LocalDateTime currentTime = LocalDateTime.now();
    int hour = currentTime.getHour();
    String h = Integer.toString(hour);
    if (hour < 10) {
      h = "0" + h;
    }
    int min = currentTime.getMinute();
    String m = Integer.toString(min);
    if (min < 10) {
      m = "0" + m;
    }
    int sec = currentTime.getSecond();
    String s = Integer.toString(sec);
    if (sec < 10) {
      s = "0" + s;
    }
    Time.setText(h + ":" + m);
    Second.setText(s);
    
    color=new Color(1- 2*Math.abs((float)sec/60-0.5f),1- 2*Math.abs((float)min/60-0.5f),1-2*Math.abs((float)hour/24-0.5f));
    
    LocalDate date = currentTime.toLocalDate();

    Date.setText(date.format(DateTimeFormatter.ofPattern("cccc, dd. MMMM yyyy")));

    getParent().revalidate();
    getParent().repaint();

  }

  @Override
  protected void paintComponent(Graphics g) {
//    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    g.setColor(new Color(0, 0, 0, 127));
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(color);
    g.fillRect(0, getHeight()-AppSettings.getInt("Border_Size"), getWidth(), AppSettings.getInt("Border_Size"));

  }

}
