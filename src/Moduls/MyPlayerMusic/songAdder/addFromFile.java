/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import Moduls.MyPlayerMusic.MyPlayerMusic;
import VControl.Settings.AppSettings;
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
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
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
public class addFromFile {

  private final JPanel gui;
  private MusicAnalizer ma;
  private SongEditPanel sep;
  private MyPlayerMusic MP;

  public addFromFile(MyPlayerMusic mod) throws IOException, UnsupportedAudioFileException {
    MP = mod;
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    ma = new MusicAnalizer();
    sep = new SongEditPanel(new String[]{"", "", "", "", "", "", "", "",}, ma);
    MyButton add = new MyButton("Přidat ...");
    final JFileChooser fc = new JFileChooser();
    FileFilter ff = new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file.getName().endsWith(".mp3") || file.getName().endsWith(".wav") || file.isDirectory();
      }

      @Override
      public String getDescription() {
        return "Known Audio Files (mp3, wav)";
      }
    };
    fc.addChoosableFileFilter(ff);
    fc.setFileFilter(ff);
    fc.setFileSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    final MyButton select = new MyButton("Vybrat soubor ...");
    select.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fc.showOpenDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          select.setText(file.getName());
          try {
            load(file);
          } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    });
    select.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    select.setForeground(AppSettings.getColour("FG_Color"));
    select.setBackground(AppSettings.getColour("BG_Color"));
    add.setForeground(AppSettings.getColour("FG_Color"));
    add.setBackground(AppSettings.getColour("BG_Color"));
    add.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") + 4));
    add.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          if (fc.getSelectedFile() != null) {
            save();
            fc.setSelectedFile(null);
            select.setText("Vybrat soubor ...");
            select.requestFocus();
            ma = new MusicAnalizer();
            sep.load(new String[]{"", "", "", "", "", "", "", "",}, ma);
          }
        } catch (IOException | UnsupportedAudioFileException | TagException ex) {
          Logger.getLogger(addFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    gui.add(select, BorderLayout.PAGE_START);
    gui.add(add, BorderLayout.PAGE_END);

    gui.add(sep);
  }

//  public final void add(File f, String[] set) throws UnsupportedAudioFileException, IOException, TagException {
//    save();
//  }

  private void load(File f) throws UnsupportedAudioFileException, IOException {
    ma.setFile(f);
    ma.initAudioStream();
    sep.load(new String[]{"", "", "", "", "", "", "", "",}, ma);
  }

  private void load(File f, String s) throws UnsupportedAudioFileException, IOException {
    ma.setFile(f);
    sep.load(s.split("%@%"), ma);
  }

  private void save() throws UnsupportedAudioFileException, IOException, TagException {
    String[] set = sep.getSet();
    File directory = new File(MP.SgetString("MusicDir"));
    if (!directory.exists()) {
      directory.mkdir();
    }

    Path f = new File(directory.toString()+File.separator+set[1] + "-" + set[0] + ".mp3").toPath();

    Files.copy(ma.getFile().toPath(), f, StandardCopyOption.REPLACE_EXISTING);

    MP3File mp3file = new MP3File(f.toString());
    TagOptionSingleton.getInstance().setDefaultSaveMode(TagConstant.MP3_FILE_SAVE_OVERWRITE);

    mp3file.getID3v2Tag().clearFrameMap();

    String s = "MyPlayer%@%";
    for (String seti : set) {
      s += seti + "%@%";
    }
    s += "0.5%@%" + Float.toString(ma.getAverangeVolume());

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
