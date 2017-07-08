/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author vojta3310
 */
public class addFromFile {

  private final JPanel gui;
  private MusicAnalizer ma;
  private SongEditPanel sep;

  public addFromFile() throws IOException, UnsupportedAudioFileException {
    gui = new JPanel();
    gui.setLayout(new BorderLayout());
    ma = new MusicAnalizer();
    sep = new SongEditPanel(new String[]{"", "", "", "", "", "", "", "",}, ma);
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

    final JButton select = new JButton("Vybrat soubor ...");
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

    gui.add(select, BorderLayout.PAGE_START);

    gui.add(sep);
  }

  private void load(File f) throws UnsupportedAudioFileException, IOException {
    ma.setFile(f);
    sep.load(new String[]{"", "", "", "", "", "", "", "",}, ma);
  }

  private void load(File f, String s) throws UnsupportedAudioFileException, IOException {
    ma.setFile(f);
    sep.load(s.split("|@|"), ma);
  }

  public JPanel getGui() {
    return gui;
  }

}
