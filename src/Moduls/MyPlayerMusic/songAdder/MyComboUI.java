/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author vojta3310
 */
public class MyComboUI extends javax.swing.plaf.basic.BasicComboBoxUI {

  @Override
  public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
    super.paintCurrentValue(g, bounds, false);
  }

  @Override
  public void paint(Graphics g, JComponent c) {
    super.paint(g, c);
    g.setColor(AppSettings.getColour("FG_Color"));
    if (c.isFocusOwner()) {
      g.fillRect(0, c.getHeight() - AppSettings.getInt("Border_Size"), c.getWidth(), c.getHeight());
    } else {
      g.fillRect(0, c.getHeight() - AppSettings.getInt("Border_Size") / 2, c.getWidth(), c.getHeight());
    }
  }

  @Override
  protected ComboPopup createPopup() {
    return new MyComboPopup(comboBox);
  }

  @Override
  protected JButton createArrowButton() {
    JButton btn = new JButton() {

      @Override
      protected void paintComponent(Graphics g) {
        g.setColor(AppSettings.getColour("FG_Color"));
        int r = AppSettings.getInt("Border_Size");
        g.fillPolygon(new int[]{(int) (getWidth() / 2 - r * 1.5), getWidth() / 2, (int) (getWidth() / 2 + r * 1.5)}, new int[]{getHeight() / 2 - r, getHeight() / 2 + r, getHeight() / 2 - r}, 3);
      }
    };
//    btn.setFocusTraversalKeysEnabled(false);
    btn.setFocusable(false);
    btn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    return btn;
  }

}
