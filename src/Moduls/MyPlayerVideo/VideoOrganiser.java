/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.Timer;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;

/**
 *
 * @author vojta3310
 */
public class VideoOrganiser implements IMediaOrganiser {

  private final EmbeddedMediaPlayerComponent PlayerComp;
  private final GUI gui;
  private final MyPlayerVideo modul;
  private boolean MediaLoaded = false;
  private boolean FullScreen = false;
  private boolean resume = false;
  private JFrame f;

  public VideoOrganiser(MyPlayerVideo m) {
    modul = m;
    new NativeDiscovery().discover();

    PlayerComp = new EmbeddedMediaPlayerComponent();
    PlayerComp.setBackground(AppSettings.getColour("BG_Color"));
    PlayerComp.setPreferredSize(new Dimension(200, 150));
    f = new JFrame();
    f.setPreferredSize(new Dimension(500, 500));
//    f.show();

    PlayerComp.getMediaPlayer().setFullScreenStrategy(new DefaultFullScreenStrategy(f));
    PlayerComp.getMediaPlayer().setEnableKeyInputHandling(true);
    PlayerComp.getMediaPlayer().setEnableMouseInputHandling(true);
    PlayerComp.getVideoSurface().setFocusable(true);
    gui = new GUI(this, m.getMyGrafics(), PlayerComp);
    try {
      loadFilms();
    } catch (IOException ex) {
      Logger.getLogger(VideoOrganiser.class.getName()).log(Level.SEVERE, null, ex);
    }

    gui.getList().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          play();
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

    gui.getPpanel().getSlider().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        resume = false;
        if (!getPaused()) {
          resume = true;
          PlayerComp.getMediaPlayer().pause();
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        PlayerComp.getMediaPlayer().play();
        PlayerComp.getMediaPlayer().setPosition(((float) ((float) gui.getPpanel().getSlider().getValue()
          / (float) gui.getPpanel().getSlider().getMaximum())));
        if (!resume) {
          PlayerComp.getMediaPlayer().pause();
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    PlayerComp.getVideoSurface().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        PlayerComp.getVideoSurface().requestFocusInWindow();
        if (e.getClickCount() == 2) {
          toogleFullScreen();
        }
        if (e.getClickCount() == 1) {
          tooglePause();
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

    PlayerComp.getVideoSurface().addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && FullScreen) {
          toogleFullScreen();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          tooglePause();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });

    Timer t = new Timer(100, (ActionEvent e) -> {
      updateVolume();
      updateGUI();
    });
    t.start();
  }

  private void updateGUI() {
    gui.getPpanel().getSizeLabel().setText(utiliti.MilToTime(PlayerComp.getMediaPlayer().getLength()));
    gui.getPpanel().getStateLabel().setText(utiliti.MilToTime((long) (PlayerComp.getMediaPlayer().getLength() * PlayerComp.getMediaPlayer().getPosition())));
    gui.getPpanel().getSlider().setMaximum(Integer.MAX_VALUE);
    if (!getPaused()) {
      gui.getPpanel().getSlider().setValue(
        (int) (((float) (PlayerComp.getMediaPlayer().getPosition())) * Integer.MAX_VALUE));
    }
    gui.repaint();
    gui.getPpanel().repaint();
    gui.getPpanel().getSizeLabel().repaint();
    gui.getPpanel().getStateLabel().repaint();
    gui.getPpanel().getSlider().repaint();
//    System.out.println(PlayerComp.getMediaPlayer().getPosition());
  }

  private void updateVolume() {
//    System.out.println(gui.getPpanel().getVcontrol().getVolume());
    PlayerComp.getMediaPlayer().setVolume((int) (200 * gui.getPpanel().getVcontrol().getVolume()));
//System.out.println(gui.getPpanel().getVcontrol().re
  }

  public EmbeddedMediaPlayerComponent getPlayerComp() {
    return PlayerComp;
  }

  @Override
  public void tooglePause() {
    if (MediaLoaded) {
      if (getPaused()) {
        PlayerComp.getMediaPlayer().play();
      } else {
        PlayerComp.getMediaPlayer().pause();
      }
    } else {
      play();
    }
  }

  public void reMakeVideo() {
    float p = PlayerComp.getMediaPlayer().getPosition();
    if (PlayerComp.getMediaPlayer().isPlayable()) {
      PlayerComp.getMediaPlayer().stop();
    }
    play();
    PlayerComp.getMediaPlayer().setPosition(p);
  }

  private void play() {
    if (gui.getList().getSelectedValue() != null) {
      PlayerComp.getMediaPlayer().playMedia(modul.SgetString("VideoDir")
        + File.separator + (String) gui.getList().getSelectedValue());
      MediaLoaded = true;
    }
  }

  @Override
  public void Next() {
    if (gui.getList().getModel().getSize() > 0) {
      if (gui.getList().getSelectedIndex() < gui.getList().getModel().getSize()-1) {
        gui.getList().setSelectedIndex(gui.getList().getSelectedIndex() + 1);
      }
      play();
    }
  }

  public void toogleFullScreen() {
    if (FullScreen) {
      f.remove(PlayerComp);
      gui.showVideo();
    } else {
      gui.hideVideo();
      f.add(PlayerComp);
    }
    FullScreen = !FullScreen;
    PlayerComp.getMediaPlayer().setFullScreen(FullScreen);
    reMakeVideo();
    gui.revalidate();
    gui.repaint();
  }

  @Override
  public void Prew() {
    if (gui.getList().getModel().getSize() > 0) {
      if (gui.getList().getSelectedIndex() > 0) {
        gui.getList().setSelectedIndex(gui.getList().getSelectedIndex() - 1);
      }
      play();
    }
  }

  @Override
  public void Repeat() {
    PlayerComp.getMediaPlayer().setRepeat(!PlayerComp.getMediaPlayer().getRepeat());
  }

  private boolean isVideo(File f) throws IOException {
    return Files.probeContentType(f.toPath()).startsWith("video");
  }

  private void loadFilms() throws IOException {
    String path = modul.SgetString("VideoDir");
    File fi = new File(path);
    String[] files = fi.list();
    DefaultListModel v = new DefaultListModel<>();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (!fil.isDirectory() && isVideo(fil)) {
        System.out.println(Files.probeContentType(fil.toPath()));
        v.addElement(file);
      }
    }
    gui.getList().setModel(v);
  }

  @Override
  public boolean getPaused() {
    return !PlayerComp.getMediaPlayer().isPlaying();
  }

}
