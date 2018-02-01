/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import VControl.Command;
import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
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
  private boolean serial = false;
  private final SearchFieldListener sfl;

  public VideoOrganiser(MyPlayerVideo m) {
    modul = m;
    new NativeDiscovery().discover();

    PlayerComp = new EmbeddedMediaPlayerComponent();
    PlayerComp.setBackground(AppSettings.getColour("BG_Color"));
    PlayerComp.setPreferredSize(new Dimension(200, 150));
    f = new JFrame();
    f.setPreferredSize(new Dimension(500, 500));
//    f.show();
    f.setUndecorated(true);

    PlayerComp.getMediaPlayer().setFullScreenStrategy(new DefaultFullScreenStrategy(f));
    PlayerComp.getMediaPlayer().setEnableKeyInputHandling(false);
    PlayerComp.getMediaPlayer().setEnableMouseInputHandling(false);
    PlayerComp.getVideoSurface().setFocusable(true);
    gui = new GUI(this, m.getMyGrafics(), PlayerComp);

    sfl = new SearchFieldListener(gui.getList());
    gui.getSearchField().getDocument().addDocumentListener(sfl);

    try {
      loadFilms();
    } catch (IOException ex) {
      Logger.getLogger(VideoOrganiser.class.getName()).log(Level.SEVERE, null, ex);
    }
    gui.getList().addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (serial) {
          try {
            loadEpizodes();
          } catch (IOException ex) {
            Logger.getLogger(VideoOrganiser.class.getName()).log(Level.SEVERE, null, ex);
          }
        } else {
          if (e.getClickCount() == 2) {
            play();
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Food box id: " + modul.SgetInt("FoodBoxID"));
            modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
            modul.getCommander().Execute(new Command("Disable", "ScreenSaver", modul.GetModulName()));
            modul.getCommander().Execute(new Command("Bring", (Object) modul.SgetInt("FoodBoxID"), "Skladnik", modul.GetModulName()));
            toogleFullScreen();
          }
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

    gui.getPlayButton().addActionListener((ActionEvent e) -> {
      play();
      modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
      modul.getCommander().Execute(new Command("Disable", "ScreenSaver", modul.GetModulName()));
      modul.getCommander().Execute(new Command("Bring", (Object) modul.SgetInt("FoodBoxID"), "Skladnik", modul.GetModulName()));
      toogleFullScreen();
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

    PlayerComp.addMouseListener(new MouseListener() {

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
      if (!getPaused()) {
        updateVolume();
        updateGUI();
      }

      if (PlayerComp.getMediaPlayer().getPosition() >= 1) {
        if (serial) {
          Next();
        } else {
          modul.getCommander().Execute(new Command("Play", "MyPlayerMusic", modul.GetModulName()));
//          modul.getCommander().Execute(new Command("Enable", "ScreenSaver", modul.GetModulName()));
        }
      }
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
    String command = modul.SgetString("Volume_Command");
    switch (command) {
      case "V":
        PlayerComp.getMediaPlayer().setVolume((int) (200 * gui.getPpanel().getVcontrol().getVolume()));
        break;
      default:
        String vol = Integer.toString(((int)((float)gui.getPpanel().getVcontrol().getVolume() * 100)));
        try {
          Runtime.getRuntime().exec(command.replace("%v%", vol));
        } catch (IOException ex) {
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        break;
    }
  }

  public EmbeddedMediaPlayerComponent getPlayerComp() {
    return PlayerComp;
  }

  @Override
  public void tooglePause() {
    if (MediaLoaded) {
      if (getPaused()) {
        PlayerComp.getMediaPlayer().play();
        modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
      } else {
        PlayerComp.getMediaPlayer().pause();
        modul.getCommander().Execute(new Command("Play", "MyPlayerMusic", modul.GetModulName()));
//        modul.getCommander().Execute(new Command("Enaable", "ScreenSaver", modul.GetModulName()));
      }
    } else {
      play();
      modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
    }
  }

  public void Play() {
    if (MediaLoaded) {
      PlayerComp.getMediaPlayer().play();
      modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
    } else {
      play();
      modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
    }
  }

  public void Pause() {
    if (MediaLoaded) {
      PlayerComp.getMediaPlayer().pause();
      modul.getCommander().Execute(new Command("Play", "MyPlayerMusic", modul.GetModulName()));
//      modul.getCommander().Execute(new Command("Enaable", "ScreenSaver", modul.GetModulName()));
    }
  }

  public void reMakeVideo() {
    float p = PlayerComp.getMediaPlayer().getPosition();
    if (PlayerComp.getMediaPlayer().isPlayable()) {
      PlayerComp.getMediaPlayer().stop();
    }
    play();
    PlayerComp.getMediaPlayer().setPosition(p);
    modul.getCommander().Execute(new Command("Pause", "MyPlayerMusic", modul.GetModulName()));
  }

  private void play() {
    if (gui.getList().getSelectedValue() != null) {
      String path;
      if (serial) {
        try {
          path = FindEpizode(modul.SgetString("VideoDir") + File.separator
            + (String) gui.getList().getSelectedValue());
//           + gui.getEpizode().getSelectedItem();
        } catch (IOException ex) {
          Logger.getLogger(VideoOrganiser.class.getName()).log(Level.SEVERE, null, ex);
          path = modul.SgetString("VideoDir") + File.separator
            + (String) gui.getList().getSelectedValue()
            + File.separator + gui.getEpizode().getSelectedItem();
        }

        try {
          File fout = new File(modul.SgetString("VideoDir") + File.separator
            + (String) gui.getList().getSelectedValue() + File.separator + "LastWatched");
          FileOutputStream fos;

          fos = new FileOutputStream(fout);
          BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
          bw.write((String) gui.getEpizode().getSelectedItem());
          bw.newLine();

          bw.close();
        } catch (IOException ex) {
          Logger.getLogger(VideoOrganiser.class.getName()).log(Level.SEVERE, null, ex);
        }

      } else {
        path = modul.SgetString("VideoDir")
          + File.separator + (String) gui.getList().getSelectedValue();
      }

      PlayerComp.getMediaPlayer().playMedia(path);
      MediaLoaded = true;
    }
  }

  @Override
  public final void Next() {
    if (serial) {
      if (gui.getEpizode().getModel().getSize() > 0) {
        if (gui.getEpizode().getSelectedIndex() < gui.getEpizode().getModel().getSize() - 1) {
          gui.getEpizode().setSelectedIndex(gui.getEpizode().getSelectedIndex() + 1);
        }
        play();
      }
    } else {
      if (gui.getList().getModel().getSize() > 0) {
        if (gui.getList().getSelectedIndex() < gui.getList().getModel().getSize() - 1) {
          gui.getList().setSelectedIndex(gui.getList().getSelectedIndex() + 1);
        }
        play();
      }
    }
  }

  @Override
  public void Prew() {
    if (serial) {
      if (gui.getEpizode().getModel().getSize() > 0) {
        if (gui.getEpizode().getSelectedIndex() > 0) {
          gui.getEpizode().setSelectedIndex(gui.getEpizode().getSelectedIndex() - 1);
        }
        play();
      }
    } else {
      if (gui.getList().getModel().getSize() > 0) {
        if (gui.getList().getSelectedIndex() > 0) {
          gui.getList().setSelectedIndex(gui.getList().getSelectedIndex() - 1);
        }
        play();
      }
    }
  }

  public void toogleFullScreen() {
    if (FullScreen) {
      f.remove(PlayerComp);
      gui.showVideo();
      f.setVisible(false);
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
  public void Repeat() {
    PlayerComp.getMediaPlayer().setRepeat(!PlayerComp.getMediaPlayer().getRepeat());
  }

  private boolean isVideo(File f) throws IOException {
    if (Files.probeContentType(f.toPath()) == null) {
      return false;
    }
    return Files.probeContentType(f.toPath()).startsWith("video");
  }

  private String FindEpizode(String path) throws IOException {

    File fi = new File(path);
    String[] files = fi.list();
    if (files == null) {
      return null;
    }
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (!fil.isDirectory() && isVideo(fil)) {
        if (fil.getName().equals(gui.getEpizode().getSelectedItem())) {
          return fil.getPath();
        }
      }
      if (fil.isDirectory()) {
        String a = FindEpizode(path + File.separator + file);
        if (a != null) {
          return a;
        }
      }
    }
    return null;
  }

  private void loadFilms() throws IOException {
    String path = modul.SgetString("VideoDir");
    File fi = new File(path);
    String[] files = fi.list();
    DefaultListModel v = new DefaultListModel<>();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (!fil.isDirectory() && isVideo(fil)) {
        v.addElement(file);
      }
    }
    gui.getList().setModel(v);
    sfl.setModel(v);
  }

  private void loadSerials() throws IOException {
    String path = modul.SgetString("VideoDir");
    File fi = new File(path);
    String[] files = fi.list();
    DefaultListModel v = new DefaultListModel<>();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (fil.isDirectory()) {
        v.addElement(file);
      }
    }
    gui.getList().setModel(v);
    sfl.setModel(v);
  }

  private ArrayList loadDirs(String path) throws IOException {
    ArrayList ar = new ArrayList();
    File fi = new File(path);
    String[] files = fi.list();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (!fil.isDirectory() && isVideo(fil)) {
        ar.add(file);
      } else if (fil.isDirectory()) {
        ar.addAll(loadDirs(path + File.separator + file));
      }
    }
    return ar;
  }

  private void loadEpizodes() throws IOException {
    if (gui.getList().getSelectedIndex() >= 0 && gui.getList().getSelectedIndex() <= gui.getList().getMaxSelectionIndex()) {
      String path = modul.SgetString("VideoDir") + File.separator + gui.getList().getSelectedValue();
      ArrayList ar = loadDirs(path);

      ar.sort(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
          return s1.compareToIgnoreCase(s2);
        }
      });

      DefaultComboBoxModel model = new DefaultComboBoxModel(ar.toArray());
      gui.getEpizode().setModel(model);

      String line;
      try (BufferedReader br = new BufferedReader(new FileReader(modul.SgetString("VideoDir") + File.separator
        + (String) gui.getList().getSelectedValue() + File.separator + "LastWatched"))) {
        line = br.readLine();
      }

      if (line != null) {
        if (gui.getEpizode().getModel().getSize() > 0) {
          gui.getEpizode().setSelectedItem(line);
        }
      }
    }
  }

  @Override
  public boolean getPaused() {
    return !PlayerComp.getMediaPlayer().isPlaying();
  }

  public void showSerial() throws IOException {
    serial = true;
    gui.showSerial();
    loadSerials();
  }

  public void showFilm() throws IOException {
    serial = false;
    gui.hideSerial();
    loadFilms();
  }

  public void setVolume(float volume) {
    gui.getPpanel().getVcontrol().setVolume(volume);
  }

  public void Vup() {
    gui.getPpanel().getVcontrol().setVolume(gui.getPpanel().getVcontrol().getVolume() + 0.1F);
  }

  public void Vdown() {
    gui.getPpanel().getVcontrol().setVolume(gui.getPpanel().getVcontrol().getVolume() - 0.1F);
  }
}
