/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.Modul;
import Moduls.MyPlayerMusic.Player.GUI.MPgui;
import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import VControl.utiliti;
import ddf.minim.AudioPlayer;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class MusicOrganiser implements IMediaOrganiser {

  private final MPgui gui;
  private final Songs Songs;
  private final DefaultListModel<Skladba> PlayedSongs;
  private final DefaultListModel<RandomSong> NextSongs;
  private Skladba Playing;
  private final Player player;
  private byte afterTransform = 0; //0 nic; 1 další; 2 předchozí; 3 pause;
  private long transformStart;
  private float transferTo = 0.5f;
  private float transferFrom = 0.0f;
  private float volume;
  private final Modul modul;
  private boolean newSong;
  private boolean ASAddFavoriti = false;
  private boolean resume = false;
  private AudioPlayer ap;

  public MusicOrganiser(Modul mod) throws IOException, TagException {
    Songs = new Songs();
    modul = mod;
    gui = new MPgui(this);
    player = new Player(mod);
    NextSongs = gui.getNextSongs();
    PlayedSongs = new DefaultListModel<>();
    gui.getSpanel().getSlist().setModel(PlayedSongs);

    gui.getPpanel().getSlider().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        resume = false;
        if (!getAplayer().getPaused()) {
          resume = true;
          getAplayer().pause();
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        getAplayer().play();
        getAplayer().setPos(getPlaying().getStart()
          + (long) ((float) ((float) gui.getPpanel().getSlider().getValue()
          / (float) gui.getPpanel().getSlider().getMaximum()) * getPlaying().getLenght()));
        if (!resume) {
          getAplayer().pause();
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    start();
  }

  public final void start() throws IOException, TagException {
    gui.clearRlist();
    volume = Float.parseFloat(modul.SgetString("Default_Volume"));
    Songs.load(modul.SgetString("MusicDir"));
    player.setStatMaxVol(Songs.getStatMaxVol());
    player.setStatMinVol(Songs.getStatMinVol());
    if (Songs.getCount() > 0) {
      playSong(NextSongs.elementAt(gui.getSpanel().getRlist().getSelectedIndex()).getSkladba());
      Timer tim = new Timer(10, (ActionEvent ae) -> {
        doTransform();
        checkNextSong();
        gui.updateGUI();
        updateVolume();
      });
      tim.start();
      Pause();
    }
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
    Playing.editFavority(-1);
    Skladba ns = getNextSong();
//    if (!ns.equals(Playing)) {
//      PlayedSongs.addElement(Playing);
//      gui.getSpanel().getRlist().setSelectedIndex(PlayedSongs.size());
//      gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    }
    playSong(ns);
    ASAddFavoriti = true;
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
        ASAddFavoriti = true;
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
        ASAddFavoriti = true;
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
    System.out.println(rs.getRepead());
    System.out.println(rs.getRepeaded());
    
    if (rs.getRepead() > rs.getRepeaded()) {
      Skladba ns = rs.getSkladba();
      rs.Repeaded();
      if (PlayedSongs.isEmpty() || !Playing.equals(ns)) {
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
    ap = player.PrepareSong(Playing);
    gui.getIpanel().setupEqualizer(ap);
    gui.getKaraoke().setupEqualizer(ap);
    gui.getPpanel().getVcontrol().setAp(ap);
    gui.getSpanel().getSlabel().setText(Playing.getLabel());
    transferTo = volume;
    player.setVolume(volume);
    player.play();
    gui.getIpanel().ShowSong(ns);
    gui.getKaraoke().ShowSong(ns);
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

  @Override
  public void Repeat() {
//    System.out.println(Playing.getRepead());
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

  @Override
  public void Next() {
    afterTransform = 1; //0 nic; 1 další; 2 předchozí; 3 pause;
    transferFrom = volume;
    transferTo = 0;
    transformStart = player.getPos();
  }

  @Override
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
      String command = modul.SgetString("Volume_Command");
      if (!command.equals("V") && !command.equals("G")) {
        try {
          Runtime.getRuntime().exec(command.replace("%v%", "0"));
        } catch (IOException ex) {
          Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      player.play();
      transferFrom = 0;
      transferTo = volume;
      transformStart = player.getPos();
      afterTransform = 0;
    }
  }

  @Override
  public void tooglePause() {
    if (player.getPaused()) {
      Play();
    } else {
      Pause();
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
//          pltooglePause();
          player.pause();
          String command = modul.SgetString("Volume_Command");
          if (!command.equals("V") && !command.equals("G")) {
            String vol = Integer.toString((int) ((float) volume * (float) 100));
            try {
              Runtime.getRuntime().exec(command.replace("%v%", vol));
            } catch (IOException ex) {
              Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
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
      if (ASAddFavoriti) {
        ASAddFavoriti = false;
        Playing.editFavority(+1);
      }
      playNextSong();
    }
  }


  public void playSongByTerm(ArrayList<Term> terms) {
    RandomSong rs = new RandomSong(Songs);
    rs.setPodm(terms);
    playSong(rs.getSkladba());
  }

  public void setDefault(String Tag, String STag) {
    for (int i = 0; i < NextSongs.size(); i++) {
      RandomSong rs = NextSongs.get(i);
      for (int j = 0; j < rs.getPodm().size(); j++) {
        Term t = rs.getPodm().get(j);
        if (t.getType().equals("Tagy") && t.getValue().equals("")) {
          t.setValue(Tag);
        }
        if (t.getType().equals("STag") && t.getValue().equals("")) {
          t.setValue(STag);
        }
      }
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

  public AudioPlayer getAp() {
    return ap;
  }

  @Override
  public boolean getPaused() {
    return player.getPaused();
  }
}
