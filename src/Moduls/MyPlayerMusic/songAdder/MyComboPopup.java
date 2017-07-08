/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author vojta3310
 */
public class MyComboPopup extends BasicComboPopup {

  public MyComboPopup(JComboBox combo) {
    super(combo);
    this.setBorder(new LineBorder(AppSettings.getColour("FG_Color"), 2));
    this.setFocusable(false);
  }

  @Override
  protected JList createList() {
    return super.createList(); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void configureList() {
    super.configureList(); //To change body of generated methods, choose Tools | Templates.
    this.getList().setBackground(AppSettings.getColour("BG_Color"));
  }

  @Override
  protected JScrollPane createScroller() {
    JScrollPane sc = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sc.getVerticalScrollBar().setUI(new MyScrollbarUI());
    sc.getVerticalScrollBar().setPreferredSize(new Dimension(AppSettings.getInt("Border_Size") + 4, Integer.MAX_VALUE));
    return sc;
  }

}
