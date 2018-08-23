/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.UI.components.MyButton;
import VControl.UI.components.MyComboUI;
import Moduls.MyPlayerMusic.Player.Skladba;
import Moduls.MyPlayerMusic.Player.Songs;
import Moduls.MyPlayerMusic.songAdder.*;
import VControl.Settings.AppSettings;
import VControl.UI.components.AutoCompletion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class EditSong {

  private final JPanel gui;
  private SongEditPanel sep;
  private final MyPlayerMusic MP;
  private final JComboBox<Skladba> combo;
  private Skladba song;
  private final Songs songs;

  public EditSong(MyPlayerMusic mod, Songs songs) throws IOException, UnsupportedAudioFileException {
    this.songs = songs;
    MP = mod;
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    sep = new SongEditPanel();
    MyButton save = new MyButton("Uložit ...");
    combo = new JComboBox(songs.getSongs().toArray());
    combo.setUI(new MyComboUI());
    AutoCompletion.enable(combo);

    save.setForeground(AppSettings.getColour("FG_Color"));
    combo.getEditor().getEditorComponent().setForeground(AppSettings.getColour("FG_Color"));
    save.setBackground(AppSettings.getColour("BG_Color"));
    combo.getEditor().getEditorComponent().setBackground(AppSettings.getColour("BG_Color"));
    combo.setBackground(AppSettings.getColour("BG_Color"));
    save.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    combo.setFont(new Font(AppSettings.getString("Font_Name"), 1, (int) (AppSettings.getInt("Font_Size") * 1.5)));

    save.addActionListener((ActionEvent ae) -> {
      try {
        if (song != null) {
          save();
          sep.clear();
          song = null;
        }
      } catch (IOException | UnsupportedAudioFileException | TagException ex) {
        Logger.getLogger(EditSong.class.getName()).log(Level.SEVERE, null, ex);
      }
    });

    combo.addActionListener((ActionEvent e) -> {
      Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Reloading songs.");
      if ((combo.getSelectedIndex() >= 0) && (combo.getSelectedIndex()) <= combo.getItemCount() - 1) {

        try {
          loads(combo.getItemAt(combo.getSelectedIndex()));
        } catch (IOException ex) {
          Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
          Logger.getLogger(EditSong.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    gui.add(combo, BorderLayout.PAGE_START);

    gui.add(save, BorderLayout.PAGE_END);

    gui.add(sep);
  }

  public final void loads(Skladba s) throws UnsupportedAudioFileException, IOException {
    song = s;
    s.initMusicAnalizer();
    sep.load(s);
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
    ArrayList<Skladba> list = songs.getSongs();
    combo.setModel(new DefaultComboBoxModel<>(list.toArray(new Skladba[list.size()])));
  }

  public JPanel getGui() {
    return gui;
  }

}
