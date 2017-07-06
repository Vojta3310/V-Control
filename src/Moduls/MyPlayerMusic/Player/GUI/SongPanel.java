/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.text.StyleConstants;

/**
 *
 * @author vojta3310
 */
public class SongPanel extends JPanel {

  private final JList slist;
  private final JList rlist;
  private final JLabel slabel;

  public SongPanel() {
    this.setPreferredSize(new Dimension((int) (2 * AppSettings.getInt("Icon_Size")), Integer.MAX_VALUE));
    this.setBackground(AppSettings.getColour("FG_Color"));
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    slist = new JList<>();
    rlist = new JList<>();
    slist.setBackground(AppSettings.getColour("BG_Color"));
    rlist.setBackground(AppSettings.getColour("BG_Color"));
    slist.setCellRenderer(new MyCellRenderer(slist));
    rlist.setCellRenderer(new MyCellRenderer(rlist));
//    rlist.se
    JScrollPane b = new JScrollPane(slist);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setViewportBorder(null);
    b.setBorder(createEmptyBorder());
    JScrollBar sb = b.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());
    JScrollPane c = new JScrollPane(rlist);
    c.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    c.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    c.setViewportBorder(null);
    c.setBorder(createEmptyBorder());
    c.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int) (0.3 * AppSettings.getInt("Icon_Size"))));
    JScrollBar sbt = c.getVerticalScrollBar();
    sbt.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sbt.setUI(new MyScrollbarUI());
    slabel = new JLabel("Tonight we go to war-TryhardNinja");
    slabel.setForeground(AppSettings.getColour("BG_Color"));
    slabel.setIconTextGap(AppSettings.getInt("Border_Size"));
    slabel.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    slabel.setPreferredSize(new Dimension((int) (1.6 * AppSettings.getInt("Icon_Size")), 2 * AppSettings.getInt("Font_Size")));
    this.add(b);
    this.add(slabel);
    this.add(c);
  }

  public JList getSlist() {
    return slist;
  }

  public JList getRlist() {
    return rlist;
  }

  public JLabel getSlabel() {
    return slabel;
  }

}
