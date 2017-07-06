/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.Settings.AppSettings;
import java.awt.Font;
import javax.print.DocFlavor;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
class InfoPanel extends JPanel {

  private final JLabel Title;
  private final JLabel Autor;
  private final JLabel Album;
  private final JLabel Tags;
  private final JLabel Played;
  private final JLabel Langue;
  private final Font font;

  public InfoPanel() {
    this.setBackground(AppSettings.getColour("BG_Color"));
    font = new Font(AppSettings.getString("Font_Name"), 0, AppSettings.getInt("Font_Size"));
    Title = new JLabel();
    Autor = new JLabel();
    Album = new JLabel();
    Tags = new JLabel();
    Played = new JLabel();
    Langue = new JLabel();
    Title.setFont(font.deriveFont(1, 2));
    Autor.setFont(font);
    Album.setFont(font);
    Tags.setFont(font);
    Played.setFont(font);
    Langue.setFont(font);

    add(Title);
    add(Autor);
    add(Album);
    add(Played);
    add(Tags);
    add(Langue);
  }
  
  public void ShowSong(Skladba s){
    Title.setText(s.getTitle());
    Autor.setText(s.getAutor());
    Album.setText(s.getAlbum());
    Played.setText(Integer.toString(s.getPlayed()));
    Tags.setText(s.getTags());
    Langue.setText(s.getLangue());
  }
  
}
