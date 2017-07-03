/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import Moduls.Modul;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class MusicOrganiser {

  private final MPgui gui;
  private final Songs Songs;
  private final DefaultListModel<Skladba> PlayedSongs;
  private final DefaultListModel<RandomSong> NextSongs;
  private Skladba Playing;
  private int lastSelected;
  private final Player player;

  public MusicOrganiser(Modul modul) throws IOException, TagException {
    gui = new MPgui(this);
    player = new Player();
    Songs = new Songs();
    Songs.load(modul.SgetString("MusicDir"));
    NextSongs = new DefaultListModel<>();
    PlayedSongs = new DefaultListModel<>();
    gui.getSpanel().getRlist().setModel(NextSongs);
    NextSongs.addElement(new RandomSong(Songs));
    NextSongs.addElement(new AddSongSign(Songs));
    gui.getSpanel().getRlist().setSelectedIndex(0);
    gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    gui.getSpanel().getRlist().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      @Override
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        clearRlist();
      }
    });
    Timer tim = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        checkNextSong();
      }
    });
    tim.start();
    playSong(NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex()).getSkladba());
  }

  private Skladba getNextSong() {
    if ((Playing.getRepead() > 0) || (Playing.getRepead() == -1)) {
      return Playing;
    } else {
      RandomSong rs = NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex());
      if ((rs.getRepead() > 0) || (rs.getRepead() == -1)) {
        return rs.getSkladba();
      } else {
        int index = gui.getSpanel().getRlist().getSelectedIndex() + 1;
        if (index == NextSongs.size() - 1) {
          index = 0;
        }
        return NextSongs.elementAt(index).getSkladba();
      }
    }
  }

  private void playSong(Skladba ns) {
    Playing = ns;
    player.PrepareSong(Playing);
    gui.getSpanel().getSlabel().setText(Playing.getLabel());
    player.play();
  }

  private void playNextSong() {
    Playing.Played();
    Skladba ns = getNextSong();
    if (!ns.equals(Playing)) {
      PlayedSongs.addElement(Playing);
    }
    playSong(ns);
  }

  private void checkNextSong() {
    if (player.getPos() >= (Playing.getLenght() + Playing.getStart())) {
      playNextSong();
    }
  }

  private void clearRlist() {
    if (lastSelected != gui.getSpanel().getRlist().getSelectedIndex()) {
      if (gui.getSpanel().getRlist().getSelectedValue().getClass().equals(AddSongSign.class)) {
        gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        NextSongs.add(NextSongs.getSize() - 1, new RandomSong(Songs));
        gui.getSpanel().getRlist().setSelectedIndex(lastSelected);
        gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      } else {
        boolean l = false;
        for (int i = 0; i < NextSongs.size(); i++) {
          RandomSong s = NextSongs.elementAt(i);
          if (s.isEmty() && l) {
            NextSongs.elementAt(i - 1).setRepead(NextSongs.elementAt(i - 1).getRepead() + 1);
            if (gui.getSpanel().getRlist().getSelectedIndex() == i) {
              gui.getSpanel().getRlist().setSelectedIndex(i - 1);
              lastSelected = i - 1;
            }
            NextSongs.removeElementAt(i);
            i--;
          } else {
            l = s.isEmty();
          }
        }
      }
      lastSelected = gui.getSpanel().getRlist().getSelectedIndex();
    }
  }

  public MPgui getGui() {
    return gui;
  }
}
