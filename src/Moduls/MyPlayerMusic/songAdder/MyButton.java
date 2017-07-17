/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.Settings.AppSettings;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author vojta3310
 */
public class MyButton extends JButton {

  private boolean enableBorder = true;

  public MyButton() {
    this.setBorder(BorderFactory.createEmptyBorder());
  }

  public MyButton(Icon icon) {
    super(icon);
    this.setBorder(BorderFactory.createEmptyBorder());
  }

  public MyButton(String text) {
    super(text);
    this.setBorder(BorderFactory.createEmptyBorder());
  }

  public MyButton(Action a) {
    super(a);
    this.setBorder(BorderFactory.createEmptyBorder());
  }

  public MyButton(String text, Icon icon) {
    super(text, icon);
    this.setBorder(BorderFactory.createEmptyBorder());
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    if (enableBorder) {
      g.setColor(getForeground());
      g.fillRect(AppSettings.getInt("Border_Size"), AppSettings.getInt("Border_Size"), getWidth() - AppSettings.getInt("Border_Size")*2, getHeight() - AppSettings.getInt("Border_Size")*2);
      g.setColor(getBackground());
      g.fillRect((int) (AppSettings.getInt("Border_Size")*1.5), (int)(AppSettings.getInt("Border_Size")*1.5), getWidth() - AppSettings.getInt("Border_Size")*3, getHeight() - AppSettings.getInt("Border_Size")*3);
    }

    g2d.setFont(getFont());
    final FontMetrics fm = g2d.getFontMetrics();
    String s =getText();
    if (fm.stringWidth(s) > getWidth() - AppSettings.getInt("Border_Size")) {
      while (fm.stringWidth(s) + fm.stringWidth("...") > getWidth() - AppSettings.getInt("Border_Size")) {
        s = s.substring(0, s.length() - 1);
      }
      s = s + "...";
    }
    g2d.setColor(getForeground());
    g2d.drawString(s, (getWidth()-fm.stringWidth(s))/2, (getHeight() + fm.getHeight())/2-5);

  }

  public boolean isEnableBorder() {
    return enableBorder;
  }

  public void setEnableBorder(boolean enableBorder) {
    this.enableBorder = enableBorder;
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension d= super.getPreferredSize();
    d.height+=AppSettings.getInt("Border_Size")*4;
    return d;
  }
  
  
  

}
