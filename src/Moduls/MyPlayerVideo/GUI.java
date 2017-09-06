/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import Moduls.MyPlayerMusic.Player.GUI.PlayerPanel;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyCellRenderer;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author vojta3310
 */
public class GUI extends JPanel {

  private final Component video;
  private final JList List;
  private final PlayerPanel ppanel;
  private final JPanel a = new JPanel();

  public GUI(IMediaOrganiser o, JPanel out, Component v) {
    video = v;
    List = new JList();
    ppanel = new PlayerPanel(o);

    List.setBackground(AppSettings.getColour("BG_Color"));
    List.setCellRenderer(new MyCellRenderer(List));
    List.setMinimumSize(new Dimension(200, 300));
    JScrollPane b = new JScrollPane(List);
    b.setViewportBorder(null);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setBorder(createEmptyBorder());
    JScrollBar sb = b.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());
    JScrollBar sb2 = b.getHorizontalScrollBar();
    sb2.setPreferredSize(new Dimension(Integer.MAX_VALUE, AppSettings.getInt("Border_Size")));
    sb2.setUI(new MyScrollbarUI());

    a.setBorder(BorderFactory.createEmptyBorder());
    a.setLayout(new BorderLayout());
    a.add(ppanel, BorderLayout.PAGE_END);
    a.add(video, BorderLayout.CENTER);
    out.setLayout(new BorderLayout());
    out.add(a, BorderLayout.CENTER);
    out.add(b, BorderLayout.LINE_START);
    out.setBackground(AppSettings.getColour("BG_Color"));

    List.setCellRenderer(new MyCellRenderer(List));

  }
  
  public void hideVideo(){
    a.remove(video);
  }
  
  public void showVideo(){
    a.add(video, BorderLayout.CENTER);
    a.revalidate();
  }
  

  public JList getList() {
    return List;
  }

  public PlayerPanel getPpanel() {
    return ppanel;
  }
}
