/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.AddSongSign;
import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import Moduls.MyPlayerMusic.Player.RandomSong;
import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

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
  private final Karaoke karaoke;
  private int lastSelected = 0;
  private final DefaultListModel<RandomSong> NextSongs;
  private final MusicOrganiser Mo;
  private final SongSearchGUI ssg;

  private boolean karaokeb = false;

  public MPgui(final MusicOrganiser o) {
    this.Mo = o;
    ipanel = new InfoPanel();
    spanel = new SongPanel();
    RSpanel = new NextSongFilter(this, o);
    ppanel = new PlayerPanel(o);
    a = new JPanel();
    karaoke = new Karaoke();
    NextSongs = new DefaultListModel<>();
    a.setLayout(new BorderLayout());
    a.add(ipanel, BorderLayout.CENTER);
    a.add(ppanel, BorderLayout.PAGE_END);
    this.setLayout(new BorderLayout());
    this.add(a, BorderLayout.CENTER);
    this.add(spanel, BorderLayout.LINE_START);
    this.setBackground(AppSettings.getColour("BG_Color"));

    NextSongs.addElement(new RandomSong(Mo.getSongs()));
    NextSongs.addElement(new AddSongSign(Mo.getSongs()));
    spanel.getRlist().setModel(NextSongs);
    spanel.getRlist().setSelectedIndex(0);
    spanel.getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    spanel.getRlist().addListSelectionListener((javax.swing.event.ListSelectionEvent evt) -> {
      clearRlist();
    });

    ssg = new SongSearchGUI(Mo.getSongs(), spanel.getRlist());
    spanel.getSlist().setTransferHandler(new SlistTransferHandler(Mo.getSongs()));
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
        if (e.getButton() == 1) {
          if (e.getClickCount() == 2
            && spanel.getRlist().getSelectedIndex() < spanel.getRlist().getModel().getSize()) {
            showRS();
          } else {
            showInfo();
          }
        } else {
          ssg.setVisible(true);
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
//    System.out.println("i" +sh);
//    if (sh != 0) {
//      sh = 0;
//      if (sh == 1) {
//        a.remove(RSpanel);
//      }
//      if (sh == 2) {
//        a.remove(karaoke);
//      }
//      a.add(ipanel, BorderLayout.CENTER);
//    }
    a.remove(RSpanel);
    a.remove(ipanel);
    a.remove(karaoke);
    if (karaokeb) {
      a.add(karaoke, BorderLayout.CENTER);
    } else {
      a.add(ipanel, BorderLayout.CENTER);
    }
  }

  public void showRS() {
    a.remove(RSpanel);
    a.remove(ipanel);
    a.remove(karaoke);

    a.add(RSpanel, BorderLayout.CENTER);

//    System.out.println("r" +sh);
//    if (sh != 1) {
//      sh = 1;
//      if (sh == 0) {
//        a.remove(ipanel);
//      }
//      if (sh == 2) {
//        a.remove(karaoke);
//      }
//      a.add(RSpanel, BorderLayout.CENTER);
    RSpanel.setRS((RandomSong) spanel.getRlist().getSelectedValue());
    RSpanel.revalidate();
    RSpanel.repaint();
//    }
  }

  public void clearRlist() {
    if (getSpanel().getRlist().getSelectedValue() == null) {
      getSpanel()
        .getRlist().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      getSpanel()
        .getRlist().setSelectedIndex(0);
      getSpanel()
        .getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      lastSelected = 0;
    }
    if (lastSelected != getSpanel().getRlist().getSelectedIndex()) {
      if (getSpanel().getRlist().getSelectedValue() != null
        && getSpanel().getRlist().getSelectedValue().getClass().equals(AddSongSign.class
        )) {
        getSpanel()
          .getRlist().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        NextSongs.add(NextSongs.getSize() - 1, new RandomSong(Mo.getSongs()));
        getSpanel()
          .getRlist().setSelectedIndex(lastSelected);
        getSpanel()
          .getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      } else {
        boolean l = false;
        for (int i = 0; i < NextSongs.size(); i++) {
          RandomSong s = NextSongs.elementAt(i);
          if (s.isEmty() && l) {
            NextSongs.elementAt(i - 1).setRepead(NextSongs.elementAt(i - 1).getRepead() + 1);
            if (getSpanel().getRlist().getSelectedIndex() == i) {
              getSpanel().getRlist().setSelectedIndex(i - 1);
              lastSelected = i - 1;
            }
            if (i < NextSongs.getSize() - 1) {
              NextSongs.removeElementAt(i);
            }
            i--;
          } else {
            l = s.isEmty();
          }
        }
      }
      lastSelected = getSpanel().getRlist().getSelectedIndex();
    }
  }

  public void updateGUI() {
    getPpanel().getSizeLabel().setText(utiliti.MilToTime(Mo.getPlaying().getLenght()));
    getPpanel().getStateLabel().setText(utiliti.MilToTime(Mo.getAplayer().getPos() - Mo.getPlaying().getStart()));
    getPpanel().getSlider().setMaximum(Integer.MAX_VALUE);
    if (!Mo.getAplayer().getPaused()) {
      getPpanel().getSlider().setValue(
        (int) (((float) (Mo.getAplayer().getPos() - Mo.getPlaying().getStart()) / Mo.getPlaying().getLenght()) * Integer.MAX_VALUE));
    }
    repaint();
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

  public Karaoke getKaraoke() {
    return karaoke;
  }

  public void setKaraoke(boolean b) {
    karaokeb = b;
    showInfo();
  }

  public DefaultListModel<RandomSong> getNextSongs() {
    return NextSongs;
  }

}
