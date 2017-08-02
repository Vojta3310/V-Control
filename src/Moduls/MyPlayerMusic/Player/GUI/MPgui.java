/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class MPgui extends JPanel {

  private final InfoPanel ipanel;
  private final SongPanel spanel;
  private final PlayerPanel ppanel;

  public MPgui(final MusicOrganiser o) {
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

    spanel.getSlist().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        System.out.println(e.getClickCount());
        if (e.getClickCount() == 2) {
          o.PlaySel();
        }

      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

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
