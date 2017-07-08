/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.Settings.AppSettings;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author vojta3310
 */
public class MyField extends JTextField {

  public MyField() {
    this.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent fe) {
        repaint();
        revalidate();
      }

      @Override
      public void focusLost(FocusEvent fe) {
        repaint();
        revalidate();
      }
    });

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(AppSettings.getColour("FG_Color"));
    if (this.isFocusOwner()) {
      g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"), getWidth(), getHeight());
    } else {
      g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size") / 2, getWidth(), getHeight());
    }
  }

}
