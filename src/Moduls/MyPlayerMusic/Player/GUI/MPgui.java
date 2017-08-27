/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import Moduls.MyPlayerMusic.Player.RandomSong;
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
  private final NextSongFilter RSpanel;
  private final JPanel a;
  private boolean i = true;

  public MPgui(final MusicOrganiser o) {
    ipanel = new InfoPanel();
    spanel = new SongPanel();
    RSpanel = new NextSongFilter(this, o);
    ppanel = new PlayerPanel(o);
    a = new JPanel();
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

    spanel.getRlist().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && 
          spanel.getRlist().getSelectedIndex() < spanel.getRlist().getModel().getSize()) {
          showRS();
        } else {
          showInfo();
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

  public void showInfo() {
    if (!i) {
      i = true;
      a.remove(RSpanel);
      a.add(ipanel, BorderLayout.CENTER);
    }
  }

  public void showRS() {
    if (i) {
      i = false;
      a.remove(ipanel);
      a.add(RSpanel, BorderLayout.CENTER);
      RSpanel.setRS((RandomSong) spanel.getRlist().getSelectedValue());
      RSpanel.revalidate();
      RSpanel.repaint();
    }
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
