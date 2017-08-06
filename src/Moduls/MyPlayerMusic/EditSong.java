/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import Moduls.MyPlayerMusic.Player.Skladba;
import Moduls.MyPlayerMusic.Player.Songs;
import Moduls.MyPlayerMusic.songAdder.*;
import VControl.Settings.AppSettings;
import VControl.UI.components.AutoCompletion;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.TagOptionSingleton;
import org.farng.mp3.id3.AbstractID3v2Frame;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyTDAT;
import org.farng.mp3.id3.ID3v2_4Frame;

/**
 *
 * @author vojta3310
 */
public class EditSong {

  private final JPanel gui;
  private MusicAnalizer ma;
  private SongEditPanel sep;
  private final MyPlayerMusic MP;
  private final JComboBox<Skladba> combo;
  private Skladba song;

  public EditSong(MyPlayerMusic mod, Songs songs) throws IOException, UnsupportedAudioFileException {
    MP = mod;
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    ma = new MusicAnalizer();
    sep = new SongEditPanel(new String[]{"", "", "", "", "", "", "", "",}, ma);
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

    save.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          if (song != null) {
            save();
            ma = new MusicAnalizer();
            sep.load(new String[]{"", "", "", "", "", "", "", "",}, ma);
            song = null;
          }
        } catch (IOException | UnsupportedAudioFileException | TagException ex) {
          Logger.getLogger(EditSong.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    combo.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if ((combo.getSelectedIndex() >= 0) && (combo.getSelectedIndex()) < combo.getItemCount() - 1) {
          try {
            loads(combo.getItemAt(combo.getSelectedIndex()));
          } catch (IOException ex) {
            Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
          } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(EditSong.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    });

    gui.add(combo, BorderLayout.PAGE_START);

    gui.add(save, BorderLayout.PAGE_END);

    gui.add(sep);
  }

  public void loads(Skladba s) throws UnsupportedAudioFileException, IOException {
    song = s;
    ma.setFile(new File(s.getPath()));
    ma.initAudioStream();
    sep.load(new String[]{s.getTitle(),
      s.getAutor(),
      s.getAlbum(),
      s.getLangue(),
      s.getTags(),
      s.getSpecialTags(),
      Long.toString(s.getStart()),
      Long.toString(s.getStart() + s.getLenght()),}, ma);
  }

  private void save() throws UnsupportedAudioFileException, IOException, TagException {
    String[] set = sep.getSet();
    File directory = new File(MP.SgetString("MusicDir"));
    if (!directory.exists()) {
      directory.mkdir();
    }

    Path f = new File(directory.toString() + File.separator + set[1] + "-" + set[0] + ".mp3").toPath();

    Files.move(ma.getFile().toPath(), f, StandardCopyOption.REPLACE_EXISTING);

    MP3File mp3file = new MP3File(f.toString());
    TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);

    mp3file.getID3v2Tag().clearFrameMap();

    String s = "MyPlayer%@%";
    for (String seti : set) {
      s += seti + "%@%";
    }
    s += Float.toString(song.getOblibenost())+"%@%" + Float.toString(ma.getAverangeVolume());

    AbstractID3v2Frame frame;
    AbstractID3v2FrameBody frameBody;
    frameBody = new FrameBodyTDAT((byte) 0, s);
    frame = new ID3v2_4Frame(frameBody);
    mp3file.getID3v2Tag().setFrame(frame);
    mp3file.save();

    MP.reloadSongs();
  }

  public JPanel getGui() {
    return gui;
  }

}
