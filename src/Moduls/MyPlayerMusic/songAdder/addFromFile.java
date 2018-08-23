/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.UI.components.MyButton;
import Moduls.MyPlayerMusic.MyPlayerMusic;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class addFromFile {

  private final JPanel gui;
  private SongEditPanel sep;
  private MyPlayerMusic MP;
  private Skladba song;
  private final MyButton select;

  public addFromFile(MyPlayerMusic mod) throws IOException, UnsupportedAudioFileException {
    MP = mod;
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    sep = new SongEditPanel();
    MyButton add = new MyButton("Přidat ...");
    final JFileChooser fc = new JFileChooser();
    FileFilter ff = new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file.getName().endsWith(".mp3") || file.isDirectory();
      }

      @Override
      public String getDescription() {
        return "Mp3 Files (.mp3)";
      }
    };
    fc.addChoosableFileFilter(ff);
    fc.setFileFilter(ff);
    fc.setFileSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    select = new MyButton("Vybrat soubor ...");
    select.addActionListener((ActionEvent e) -> {
      int returnVal = fc.showOpenDialog(gui);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try {
          load(file);
        } catch (UnsupportedAudioFileException | IOException ex) {
          Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    select.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    select.setForeground(AppSettings.getColour("FG_Color"));
    select.setBackground(AppSettings.getColour("BG_Color"));
    add.setForeground(AppSettings.getColour("FG_Color"));
    add.setBackground(AppSettings.getColour("BG_Color"));
    add.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    add.addActionListener((ActionEvent ae) -> {
      try {
        if (song != null) {
          save();
          fc.setSelectedFile(null);
          select.setText("Vybrat soubor ...");
          select.requestFocus();
          sep.clear();
        }
      } catch (IOException | UnsupportedAudioFileException | TagException ex) {
        Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
      }
    });

    gui.add(select, BorderLayout.PAGE_START);
    gui.add(add, BorderLayout.PAGE_END);

    gui.add(sep);
  }

//  public final void add(File f, String[] set) throws UnsupportedAudioFileException, IOException, TagException {
//    save();
//  }
  public final void load(File f) throws UnsupportedAudioFileException, IOException {
    select.setText(f.getName());
    song = new Skladba(f.getPath());
    song.initMusicAnalizer();
    SongInfo i = new SongInfo();
    i.setInfo(song);
    sep.load(song);
  }

  public final void loadSond(Skladba s) throws UnsupportedAudioFileException, IOException {
    select.setText(new File(s.getPath()).getName());
    song = s;
    song.initMusicAnalizer();
    sep.load(song);
  }

  private void save() throws UnsupportedAudioFileException, IOException, TagException {
    File directory = new File(MP.SgetString("MusicDir"));
    if (!directory.exists()) {
      directory.mkdir();
    }
    sep.apply(song);
    if (song.getLangue().equals("none")) {
      return;
    }
    song.save(directory.getPath());
    MP.reloadSongs();
  }

  public JPanel getGui() {
    return gui;
  }

}
