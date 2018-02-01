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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagConstant;
import org.farng.mp3.TagException;
import org.farng.mp3.TagOptionSingleton;
import org.farng.mp3.id3.AbstractID3v2FrameBody;
import org.farng.mp3.id3.FrameBodyAENC;
import org.farng.mp3.id3.FrameBodyTALB;
import org.farng.mp3.id3.FrameBodyTCOM;
import org.farng.mp3.id3.FrameBodyTCOP;
import org.farng.mp3.id3.FrameBodyTENC;
import org.farng.mp3.id3.FrameBodyTEXT;
import org.farng.mp3.id3.FrameBodyTIT2;
import org.farng.mp3.id3.FrameBodyTMED;
import org.farng.mp3.id3.FrameBodyTOPE;
import org.farng.mp3.id3.FrameBodyTPE1;
import org.farng.mp3.id3.FrameBodyTPUB;
import org.farng.mp3.id3.FrameBodyTRCK;
import org.farng.mp3.id3.FrameBodyTSRC;
import org.farng.mp3.id3.FrameBodyTSSE;
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
  private final Songs songs;

  public EditSong(MyPlayerMusic mod, Songs songs) throws IOException, UnsupportedAudioFileException {
    this.songs = songs;
    MP = mod;
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    ma = new MusicAnalizer();
    sep = new SongEditPanel(new String[]{"", "", "", "", "", "", "", "", ""}, ma);
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
          ma = new MusicAnalizer();
          sep.load(new String[]{"", "", "", "", "", "", "", "", ""}, ma);
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
    ma.setFile(new File(s.getPath()));
    ma.initAudioStream();
    sep.load(new String[]{s.getTitle(),
      s.getAutor(),
      s.getAlbum(),
      s.getLangue(),
      s.getTags(),
      s.getSpecialTags(),
      Long.toString(s.getStart()),
      Long.toString(s.getStart() + s.getLenght()),
      s.getLyric()
    }, ma);
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
//
//    String s = "MyPlayer%@%";
//    for (String seti : set) {
//      s += seti + "%@%";
//    }
//    s += Float.toString(song.getOblibenost())+"%@%" + Float.toString(ma.getAverangeVolume());
//
//    AbstractID3v2Frame frame;
//    AbstractID3v2FrameBody frameBody;
//    frameBody = new FrameBodyTDAT((byte) 0, s);
//    frame = new ID3v2_4Frame(frameBody);
//    mp3file.getID3v2Tag().setFrame(frame);

    AbstractID3v2FrameBody frameBody = new FrameBodyTMED((byte) 0, "MyPlayer");
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTIT2((byte) 0, set[0]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTPE1((byte) 0, set[1]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTALB((byte) 0, set[2]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTOPE((byte) 0, set[3]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTPUB((byte) 0, set[4]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTCOP((byte) 0, set[5]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTSRC((byte) 0, set[6]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTRCK((byte) 0, set[7]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTEXT((byte) 0, set[8]);
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTCOM((byte) 0, Float.toString(ma.getAverangeVolume()));
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));
    frameBody = new FrameBodyTENC((byte) 0, Float.toString(song.getOblibenost()));
    mp3file.getID3v2Tag().setFrame(new ID3v2_4Frame(frameBody));

    mp3file.save();

    MP.reloadSongs();
    ArrayList<Skladba> list = songs.getSongs();

    combo.setModel(new DefaultComboBoxModel<>(list.toArray(new Skladba[list.size()])));
  }

  public JPanel getGui() {
    return gui;
  }

}
