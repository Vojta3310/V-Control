/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import java.awt.Color;
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
    setBackground(AppSettings.getColour("BG_Color"));
    setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    setForeground(AppSettings.getColour("FG_Color"));
  }

  public MyButton(Icon icon) {
    super(icon);
    this.setBorder(BorderFactory.createEmptyBorder());
    setBackground(AppSettings.getColour("BG_Color"));
    setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    setForeground(AppSettings.getColour("FG_Color"));
  }

  public MyButton(String text) {
    super(text);
    this.setBorder(BorderFactory.createEmptyBorder());
    setBackground(AppSettings.getColour("BG_Color"));
    setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    setForeground(AppSettings.getColour("FG_Color"));
  }

  public MyButton(Action a) {
    super(a);
    this.setBorder(BorderFactory.createEmptyBorder());
    setBackground(AppSettings.getColour("BG_Color"));
    setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    setForeground(AppSettings.getColour("FG_Color"));
  }

  public MyButton(String text, Icon icon) {
    super(text, icon);
    this.setBorder(BorderFactory.createEmptyBorder());
    setBackground(AppSettings.getColour("BG_Color"));
    setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    setForeground(AppSettings.getColour("FG_Color"));
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g.setColor(getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    if (enableBorder) {
      g.setColor(getForeground());
      g.fillRect(AppSettings.getInt("Border_Size"), AppSettings.getInt("Border_Size"), getWidth() - AppSettings.getInt("Border_Size") * 2, getHeight() - AppSettings.getInt("Border_Size") * 2);
      g.setColor(getBackground());
      g.fillRect((int) (AppSettings.getInt("Border_Size") * 1.5), (int) (AppSettings.getInt("Border_Size") * 1.5), getWidth() - AppSettings.getInt("Border_Size") * 3, getHeight() - AppSettings.getInt("Border_Size") * 3);
    }

    g2d.setFont(getFont());
    final FontMetrics fm = g2d.getFontMetrics();
    String s = getText();
    if (fm.stringWidth(s) > getWidth() - AppSettings.getInt("Border_Size")) {
      while (fm.stringWidth(s) + fm.stringWidth("...") > getWidth() - AppSettings.getInt("Border_Size")) {
        s = s.substring(0, s.length() - 1);
      }
      s = s + "...";
    }
    g2d.setColor(getForeground());
    g2d.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, (getHeight() + fm.getHeight()) / 2 - 5);

  }

  public boolean isEnableBorder() {
    return enableBorder;
  }

  public void setEnableBorder(boolean enableBorder) {
    this.enableBorder = enableBorder;
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension d = super.getPreferredSize();
    d.height += AppSettings.getInt("Border_Size") * 4;
    return d;
  }

  @Override
  public void setEnabled(boolean b) {
    super.setEnabled(b); //To change body of generated methods, choose Tools | Templates.
    if (b) {
      setForeground(AppSettings.getColour("FG_Color"));
    } else {
      setForeground(Color.GRAY);
    }

  }

}
