/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import Moduls.MyPlayerMusic.Player.GUI.InfoPanel;
import Moduls.MyPlayerMusic.Player.GUI.PlayerPanel;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyCellRenderer;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class GUI extends JPanel {

  private final InfoPanel ipanel;
  private final JList List;
  private final PlayerPanel ppanel;

  public GUI(IMediaOrganiser o) {
    ipanel = new InfoPanel();
    List = new JList();
    ppanel = new PlayerPanel(o);

    JPanel a = new JPanel();
    a.setLayout(new BorderLayout());
    a.add(ipanel, BorderLayout.CENTER);
    a.add(ppanel, BorderLayout.PAGE_END);
    this.setLayout(new BorderLayout());
    this.add(a, BorderLayout.CENTER);
    this.add(List, BorderLayout.LINE_START);
    this.setBackground(AppSettings.getColour("BG_Color"));
    
    List.setCellRenderer(new MyCellRenderer(List));

  }

  public InfoPanel getIpanel() {
    return ipanel;
  }

  public JList getSpanel() {
    return List;
  }

  public PlayerPanel getPpanel() {
    return ppanel;
  }
}