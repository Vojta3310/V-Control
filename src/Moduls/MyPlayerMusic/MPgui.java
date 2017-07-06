/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class MPgui extends JPanel {

  private final InfoPanel ipanel;
  private final SongPanel spanel;
  private final PlayerPanel ppanel;

  public MPgui(MusicOrganiser o) {
    ipanel = new InfoPanel();
    spanel = new SongPanel();
    ppanel = new PlayerPanel(o);
    JPanel a = new JPanel();
    a.setLayout(new BorderLayout());
    a.add(ipanel, BorderLayout.CENTER);
    a.add(ppanel, BorderLayout.PAGE_END);
    this.setLayout(new BorderLayout());
    this.add(a, BorderLayout.CENTER);
    this.add(spanel, BorderLayout.LINE_START);
    this.setBackground(AppSettings.getColour("BG_Color"));
  }

  public InfoPanel getIpanel() {
    return ipanel;
  }

  public SongPanel getSpanel() {
    return spanel;
  }

  public PlayerPanel getPpanel() {
    return ppanel;
  }

}
