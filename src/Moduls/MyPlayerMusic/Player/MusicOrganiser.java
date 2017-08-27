/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.Modul;
import Moduls.MyPlayerMusic.Player.GUI.MPgui;
import VControl.utiliti;
import ddf.minim.AudioPlayer;
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
  private byte afterTransform = 0; //0 nic; 1 další; 2 předchozí; 3 pause;
  private long transformStart;
  private float transferTo = 0.5f;
  private float transferFrom = 0.0f;
  private float volume;
  private final Modul modul;
  private boolean newSong;

  public MusicOrganiser(Modul mod) throws IOException, TagException {
    Songs = new Songs();
    modul = mod;
    gui = new MPgui(this);
    player = new Player();
    NextSongs = new DefaultListModel<>();
    PlayedSongs = new DefaultListModel<>();
    gui.getSpanel().getRlist().setModel(NextSongs);
    gui.getSpanel().getSlist().setModel(PlayedSongs);
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
    start();
  }

  public final void start() throws IOException, TagException {
    volume = Float.parseFloat(modul.SgetString("Default_Volume"));
    Songs.load(modul.SgetString("MusicDir"));
    player.setStatMaxVol(Songs.getStatMaxVol());
    player.setStatMinVol(Songs.getStatMinVol());
    playSong(NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex()).getSkladba());
    Timer tim = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        doTransform();
        checkNextSong();
        updateGUI();
        updateVolume();
      }
    });
    tim.start();
  }

  private void updateVolume() {
//    System.out.println(gui.getPpanel().getVcontrol().getVolume());
    if (gui.getPpanel().getVcontrol().getVolume() != volume) {
      IsetVolume(gui.getPpanel().getVcontrol().getVolume());
    }
  }

  public void reloadSongs() throws IOException, TagException {
    Songs.load(modul.SgetString("MusicDir"));
    player.setStatMaxVol(Songs.getStatMaxVol());
    player.setStatMinVol(Songs.getStatMinVol());
  }

  public Player getAplayer() {
    return player;
  }

  private void UplayNext() {
    transferFrom = 0;
    transferTo = 0;
    Playing.Played();
    //@TODO playing.oblibenost --
    Skladba ns = getNextSong();
//    if (!ns.equals(Playing)) {
//      PlayedSongs.addElement(Playing);
//      gui.getSpanel().getRlist().setSelectedIndex(PlayedSongs.size());
//      gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    }
    playSong(ns);
    //@TODO kdyš ns dohraje ns.oblineost ++
  }

  private void UplayPrew() {
    transferFrom = 0;
    transferTo = 0;
    Playing.Played();
    if (gui.getSpanel().getSlist().getModel().getSize() > 0) {
      if (gui.getSpanel().getSlist().getSelectedIndex() > 0) {
        if (newSong) {
          gui.getSpanel().getSlist().setSelectedIndex(PlayedSongs.getSize() - 1);
          PlayedSongs.addElement(Playing);
          gui.getSpanel().getSlist().scrollRectToVisible(gui.getSpanel().getSlist().getCellBounds(PlayedSongs.size() - 1, PlayedSongs.size() - 1));
        } else {
          gui.getSpanel().getSlist().setSelectedIndex(gui.getSpanel().getSlist().getSelectedIndex() - 1);
        }
        //@TODO kdyš ns dohraje ns.oblineost ++
      }
      Skladba ns = PlayedSongs.elementAt(gui.getSpanel().getSlist().getSelectedIndex());
      newSong = false;
      playSong(ns);
    } else {
      newSong = false;
      playSong(Playing);
    }
  }

  private void UplaySel() {
    transferFrom = 0;
    transferTo = 0;
    Playing.Played();
    if (gui.getSpanel().getSlist().getModel().getSize() > 0) {
      if (gui.getSpanel().getSlist().getSelectedIndex() >= 0) {
        if (newSong) {
          PlayedSongs.addElement(Playing);
        }
        //@TODO kdyš ns dohraje ns.oblineost ++
      }
      Skladba ns = PlayedSongs.elementAt(gui.getSpanel().getSlist().getSelectedIndex());
      newSong = false;
      playSong(ns);
    }
  }

  private Skladba getNextSong() {
    if (!PlayedSongs.isEmpty()) {
      if (gui.getSpanel().getSlist().getSelectedIndex() < PlayedSongs.getSize() - 1) {
        gui.getSpanel().getSlist().setSelectedIndex(gui.getSpanel().getSlist().getSelectedIndex() + 1);
        newSong = false;
        return PlayedSongs.elementAt(gui.getSpanel().getSlist().getSelectedIndex());
      }
    }
    newSong = true;
    RandomSong rs = NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex());
    if (rs.getRepead() > rs.getRepeaded()) {
      Skladba ns = rs.getSkladba();
      rs.Repeaded();
      if (PlayedSongs.isEmpty() || !Playing.equals(PlayedSongs.elementAt(PlayedSongs.getSize() - 1))) {
        PlayedSongs.addElement(Playing);
        gui.getSpanel().getSlist().scrollRectToVisible(gui.getSpanel().getSlist().getCellBounds(PlayedSongs.size() - 1, PlayedSongs.size() - 1));
        gui.getSpanel().getSlist().setSelectedIndex(PlayedSongs.size() - 1);
        gui.getSpanel().getSlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      }
      return ns;
    }
    if (rs.getRepead() == rs.getRepeaded()) {
      rs.ResetRepeaded();
    }
    int index = gui.getSpanel().getRlist().getSelectedIndex() + 1;
    if (index >= NextSongs.size() - 1) {
      index = 0;
    }
    gui.getSpanel().getRlist().setSelectedIndex(index);

    Skladba ns = NextSongs.elementAt(index).getSkladba();
    if (!ns.equals(Playing)) {
      PlayedSongs.addElement(Playing);
      gui.getSpanel().getSlist().scrollRectToVisible(gui.getSpanel().getSlist().getCellBounds(PlayedSongs.size() - 1, PlayedSongs.size() - 1));
      gui.getSpanel().getSlist().setSelectedIndex(PlayedSongs.size() - 1);
      gui.getSpanel().getSlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    return ns;
  }

  private void playSong(Skladba ns) {
    Playing = ns;
    AudioPlayer ap = player.PrepareSong(Playing);
    gui.getIpanel().setupEqualizer(ap);
    gui.getPpanel().getVcontrol().setAp(ap);
    gui.getSpanel().getSlabel().setText(Playing.getLabel());
    transferTo = volume;
    player.setVolume(volume);
    player.play();
    gui.getIpanel().ShowSong(ns);
    modul.repaint();
  }

  private void playNextSong() {
    Playing.Played();
    if ((Playing.getRepead() > 0) || (Playing.getRepead() == -1)) {
      Playing.Repeaded();
      playSong(Playing);
    } else {
      playSong(getNextSong());
    }
    //    if (!ns.equals(Playing)) {
    //      PlayedSongs.addElement(Playing);
    //      gui.getSpanel().getRlist().setSelectedIndex(PlayedSongs.size());
    //      gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //    }

  }

  public void Vup() {
    setVolume(volume + Float.parseFloat(modul.SgetString("Volume_Step")));
  }

  public void Vdown() {
    setVolume(volume - Float.parseFloat(modul.SgetString("Volume_Step")));
  }

  public void setVolume(float v) {
    gui.getPpanel().getVcontrol().setVolume(v);
  }

  private void IsetVolume(float v) {
    transferFrom = volume;
    if (v <= 1 && v >= 0) {
      this.volume = (v);
    } else if (v > 1) {
      this.volume = 1;
    } else if (v < 0) {
      this.volume = 0;
    }
    transferTo = volume;
    transformStart = player.getPos();
  }

  public void Repeat() {
    System.out.println(Playing.getRepead());
    if (Playing.getRepead() > 0 && Playing.getRepead() < 5) {
      Playing.setRepead(Playing.getRepead() + 1);
    } else if (Playing.getRepead() == 0) {
      Playing.setRepead(-1);
    } else if (Playing.getRepead() == -1) {
      Playing.setRepead(1);
    } else if (Playing.getRepead() == 5) {
      Playing.setRepead(0);
    }
  }

  public void Next() {
    afterTransform = 1; //0 nic; 1 další; 2 předchozí; 3 pause;
    transferFrom = volume;
    transferTo = 0;
    transformStart = player.getPos();
  }

  public void Prew() {
    afterTransform = 2; //0 nic; 1 další; 2 předchozí; 3 pause;
    transferFrom = volume;
    transferTo = 0;
    transformStart = player.getPos();
  }

  public void PlaySel() {
    afterTransform = 4; //0 nic; 1 další; 2 předchozí; 3 pause; 4 playsel;
    transferFrom = volume;
    transferTo = 0;
    transformStart = player.getPos();
  }

  public void Pause() {
    if (!player.getPaused()) {
      afterTransform = 3; //0 nic; 1 další; 2 předchozí; 3 pause;
      transferFrom = volume;
      transferTo = 0;
      transformStart = player.getPos();
    }
  }

  public void Play() {
    if (player.getPaused()) {
      player.play();
      transferFrom = 0;
      transferTo = volume;
      transformStart = player.getPos();
      afterTransform = 0;
    }
  }

  public void tooglePause() {
    if (player.getPaused()) {
      Play();
    } else {
      Pause();
    }
  }

  private void pltooglePause() {
    if (player.getPaused()) {
      player.play();
    } else {
      player.pause();
    }
  }

  private void doTransform() {
    if (!(((transferFrom > transferTo) && (transferTo >= player.getVolume()))
      || ((transferFrom < transferTo) && (transferTo <= player.getVolume())))) {
      float stat = (float) (player.getPos() - transformStart) / modul.SgetInt("Transfet_Lenght");
      player.setVolume(transferFrom + (transferTo - transferFrom) * stat);
    } else {
      switch (afterTransform) {
        case 1:
          UplayNext();
          break;
        case 2:
          UplayPrew();
          break;
        case 3:
          pltooglePause();
          break;
        case 4:
          UplaySel();
          break;
      }
      afterTransform = 0;
    }
  }

  private void checkNextSong() {
    if (player.finished()) {
      playNextSong();
    }
  }

  private void updateGUI() {
    gui.getPpanel().getSizeLabel().setText(utiliti.MilToTime(Playing.getLenght()));
    gui.getPpanel().getStateLabel().setText(utiliti.MilToTime(player.getPos() - Playing.getStart()));
    gui.getPpanel().getSlider().setMaximum(Integer.MAX_VALUE);
    if (!player.getPaused()) {
      gui.getPpanel().getSlider().setValue(
        (int) (((float) (player.getPos() - Playing.getStart()) / Playing.getLenght()) * Integer.MAX_VALUE));
    }
    gui.repaint();
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

  public Skladba getPlaying() {
    return Playing;
  }

  public Modul getModul() {
    return modul;
  }

  public MPgui getGui() {
    return gui;
  }

  public Songs getSongs() {
    return Songs;
  }
}
