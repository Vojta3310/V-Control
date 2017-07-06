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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  private byte afterTransform = 0; //0 nic; 1 další; 2 předchozí; 3 pause;
  private long transformEnd;
  private long transformStart;
  private float transferTo = 0.5f;
  private float transferFrom = 0.5f;
  private float volume = 0.5f;
  private final Modul modul;

  public MusicOrganiser(Modul mod) throws IOException, TagException {
    modul = mod;
    gui = new MPgui(this);
    player = new Player();
    Songs = new Songs();
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
    Path p = Paths.get(modul.SgetString("MusicDir"));
    if (Files.exists(p)) {
      DirectoryStream<Path> dirStream = Files.newDirectoryStream(p);
      if (dirStream.iterator().hasNext()) {
        start();
      }
    }
  }

  public final void start() throws IOException, TagException {
    Timer tim = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        doTransform();
        checkNextSong();
      }
    });
    tim.start();
    Songs.load(modul.SgetString("MusicDir"));
    playSong(NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex()).getSkladba());
  }

  private void UplayNext() {
    Playing.Played();
    //@TODO playing.oblibenost --
    Skladba ns = getNextSong();
    if (!ns.equals(Playing)) {
      PlayedSongs.addElement(Playing);
      gui.getSpanel().getRlist().setSelectedIndex(PlayedSongs.size() - 1);
      gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    gui.getIpanel().ShowSong(ns);
    playSong(ns);
    //@TODO kdyš ns dohraje ns.oblineost ++
  }

  private void UplayPrew() {
    Playing.Played();
    if (gui.getSpanel().getSlist().getSelectedIndex() > 0) {
      gui.getSpanel().getSlist().setSelectedIndex(gui.getSpanel().getSlist().getSelectedIndex() - 1);
    }
    Skladba ns = PlayedSongs.elementAt(gui.getSpanel().getSlist().getSelectedIndex());
    gui.getIpanel().ShowSong(ns);
    playSong(ns);
    //@TODO kdyš ns dohraje ns.oblineost ++
  }

  private Skladba getNextSong() {
    if (!PlayedSongs.isEmpty()) {
      if (gui.getSpanel().getSlist().getSelectedIndex() < PlayedSongs.getSize() - 1) {
        gui.getSpanel().getSlist().setSelectedIndex(gui.getSpanel().getSlist().getSelectedIndex() + 1);
        return PlayedSongs.elementAt(gui.getSpanel().getSlist().getSelectedIndex());
      }
    }
    RandomSong rs = NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex());
    if ((rs.getRepead() > 0) || (rs.getRepead() == -1)) {
      return rs.getSkladba();
    }
    int index = gui.getSpanel().getRlist().getSelectedIndex() + 1;
    if (index == NextSongs.size() - 1) {
      index = 0;
    }
    return NextSongs.elementAt(index).getSkladba();
  }

  private void playSong(Skladba ns) {
    Playing = ns;
    player.PrepareSong(Playing);
    gui.getSpanel().getSlabel().setText(Playing.getLabel());
    transferTo = volume;
    player.setVolume(volume);
    player.play();
  }

  private void playNextSong() {
    Playing.Played();
    Skladba ns = getNextSong();
    if ((Playing.getRepead() > 0) || (Playing.getRepead() == -1)) {
      ns = Playing;
    }
    if (!ns.equals(Playing)) {
      PlayedSongs.addElement(Playing);
      gui.getSpanel().getRlist().setSelectedIndex(PlayedSongs.size() - 1);
      gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    gui.getIpanel().ShowSong(ns);
    playSong(ns);
  }

  public void Vup() {
    setVolume(volume + Float.parseFloat(modul.SgetString("Volume_Step")));
  }

  public void Vdown() {
    setVolume(volume - Float.parseFloat(modul.SgetString("Volume_Step")));
  }

  public void setVolume(float v) {
    transferFrom = player.getVolume();
    transferTo = v;
    transformStart = player.getPos();
  }

  public void Next() {
    afterTransform = 1; //0 nic; 1 další; 2 předchozí; 3 pause;
    transferFrom = player.getVolume();
    transferTo = 0;
    transformStart = player.getPos();
  }

  public void Prew() {
    afterTransform = 2; //0 nic; 1 další; 2 předchozí; 3 pause;
    transferFrom = player.getVolume();
    transferTo = 0;
    transformStart = player.getPos();
  }

  public void Pause() {
    if (!player.getPaused()) {
      afterTransform = 3; //0 nic; 1 další; 2 předchozí; 3 pause;
      transferFrom = player.getVolume();
      transferTo = 0;
      transformStart = player.getPos();
    }
  }

  public void Play() {
    if (player.getPaused()) {
      player.play();
      transferFrom = player.getVolume();
      transferTo = volume;
      transformStart = player.getPos();
    }
  }

  public void tooglePause() {
    if (player.getPaused()) {
      Play();
    } else {
      Pause();
    }
  }

  private void doTransform() {
    if (transferTo != player.getVolume()) {
      float stat = (player.getPos() - transformStart) / modul.SgetInt("Transfet_Lenght");
      player.setVolume(transferFrom + (transferTo - transferFrom) * stat);
    } else {
      switch (afterTransform) {
        case 1:
          UplayNext();
        case 2:
          UplayPrew();
        case 3:
          tooglePause();
      }
      afterTransform = 0;
    }
  }

  private void checkNextSong() {
    if (player.getPos() >= (Playing.getLenght() + Playing.getStart())) {
      playNextSong();
    }
  }

  private void clearRlist() {
    if (lastSelected != gui.getSpanel().getRlist().getSelectedIndex()) {
      if (gui.getSpanel().getRlist().getSelectedValue().getClass().equals(AddSongSign.class
      )) {
        gui.getSpanel()
          .getRlist().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        NextSongs.add(NextSongs.getSize() - 1, new RandomSong(Songs));
        gui.getSpanel()
          .getRlist().setSelectedIndex(lastSelected);
        gui.getSpanel()
          .getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
