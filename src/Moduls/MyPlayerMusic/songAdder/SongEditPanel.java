/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.UI.components.MyField;
import VControl.UI.components.MyComboUI;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author vojta3310
 */
public class SongEditPanel extends JPanel {

  private final JLabel title;
  private final JLabel album;
  private final JLabel tags;
  private final JLabel specialtags;
  private final JLabel langue;
  private final JLabel autor;
  private final MyButton editLyric;
  private final MyButton gLyric;
  private final JFrame lyricFrame;

  private final JTextArea lyric;
  private final JTextField ftitle;
  private final JTextField falbum;
  private final JTextField ftags;
  private final JTextField fspecialtags;
  private final JComboBox<String> flangue;
  private final JTextField fautor;
  private final WaveEdit WE;

  public SongEditPanel(Skladba s) throws IOException, UnsupportedAudioFileException {
    this();
    load(s);
  }

  public SongEditPanel() throws IOException {

    this.setBackground(AppSettings.getColour("BG_Color"));

    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));
    title = new JLabel("Název:");
    autor = new JLabel("Autor:");
    album = new JLabel("Album:");
    langue = new JLabel("Jazyk:");
    tags = new JLabel("Tagy:");
    specialtags = new JLabel("STagy:");
    WE = new WaveEdit(new MusicAnalizer());
    WE.setStart(0L);
    WE.setEnd(0L);
    editLyric = new MyButton("Upravit text skladby");
    gLyric = new MyButton("Vyhledat text skadby");
    lyric = new JTextArea();
    lyricFrame = new JFrame("Text skladby");
    JScrollPane sp = new JScrollPane(lyric);
    lyricFrame.add(sp);
    lyricFrame.setSize(600, 700);
    lyricFrame.setLocationRelativeTo(editLyric);

    lyricFrame.addWindowListener(new WindowListener() {

      @Override
      public void windowOpened(WindowEvent e) {
      }

      @Override
      public void windowClosing(WindowEvent e) {
        updateButontext();
      }

      @Override
      public void windowClosed(WindowEvent e) {
        updateButontext();
      }

      @Override
      public void windowIconified(WindowEvent e) {
      }

      @Override
      public void windowDeiconified(WindowEvent e) {
        updateButontext();
      }

      @Override
      public void windowActivated(WindowEvent e) {
      }

      @Override
      public void windowDeactivated(WindowEvent e) {
        updateButontext();
      }
    });

    ftitle = new MyField();
    fautor = new MyField();
    falbum = new MyField();
    flangue = new JComboBox<>(new String[]{
      "cs_CZ", "en_GB", "ar_EG", "bg_BG", "ca_ES", "da_DK", "de_AT", "de_BE",
      "de_CH", "de_DE", "de_LI", "de_LU", "el_CY", "el_GR", "en_AU", "en_BE",
      "en_BW", "en_BZ", "en_CA", "en_GB", "en_HK", "en_IE", "en_IN", "en_JM",
      "en_MH", "en_MT", "en_NA", "en_NZ", "en_PH", "en_PK", "en_RH", "en_SG",
      "en_TT", "en_US", "en_US_POSIX", "en_VI", "en_ZA", "en_ZW", "es_AR", "es_BO",
      "es_CL", "es_CO", "es_CR", "es_DO", "es_EC", "es_ES", "es_GT", "es_HN",
      "es_MX", "es_NI", "es_PA", "es_PE", "es_PR", "es_PY", "es_SV", "es_US",
      "es_UY", "es_VE", "et_EE", "eu_ES", "fa_IR", "fi_FI", "fr_BE", "fr_CA",
      "fr_CH", "fr_FR", "fr_LU", "fr_MC", "gl_ES", "hr_HR", "hu_HU", "in_ID",
      "is_IS", "it_CH", "it_IT", "iw_IL", "ja_JP", "kk_KZ", "ko_KR", "lt_LT",
      "lv_LV", "mk_MK", "ms_BN", "ms_MY", "nl_BE", "nl_NL", "no_NO", "no_NO_NY",
      "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "ru_UA", "sh_BA", "sh_CS",
      "sh_YU", "sk_SK", "sl_SI", "sq_AL", "sr_BA", "sr_ME", "sr_RS", "sv_FI",
      "sv_SE", "th_TH", "tr_TR", "uk_UA", "vi_VN", "zh_CN", "zh_HK", "zh_HANS_SG",
      "zh_HANT_MO", "zh_MO", "zh_TW"
    });
    ftags = new MyField();
    fspecialtags = new MyField();

    title.setFont(f);
    autor.setFont(f);
    album.setFont(f);
    langue.setFont(f);
    tags.setFont(f);
    specialtags.setFont(f);
    lyric.setFont(f);

    title.setForeground(AppSettings.getColour("FG_Color"));
    autor.setForeground(AppSettings.getColour("FG_Color"));
    album.setForeground(AppSettings.getColour("FG_Color"));
    langue.setForeground(AppSettings.getColour("FG_Color"));
    tags.setForeground(AppSettings.getColour("FG_Color"));
    specialtags.setForeground(AppSettings.getColour("FG_Color"));

    flangue.setForeground(AppSettings.getColour("FG_Color"));
    flangue.setUI(new MyComboUI());
    flangue.setBackground(AppSettings.getColour("BG_Color"));
    flangue.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    flangue.setFont(f);

    editLyric.addActionListener((ActionEvent e) -> {
      lyricFrame.setVisible(true);
    });

    gLyric.addActionListener((ActionEvent e) -> {
      reGoogle();
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    int labelWidth = AppSettings.getInt("Icon_Size") / 2;
    int fieldWidth = AppSettings.getInt("Icon_Size") * 2;
    int rowHeight = AppSettings.getInt("Font_Size") + 20;

    this.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
        .addComponent(WE, 1000, (int) (AppSettings.getInt("Window_Width")
          - (AppSettings.getInt("Icon_Size") + 20 + AppSettings.getInt("Border_Size"))), Integer.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
          .addGroup(layout.createSequentialGroup()
            //            .addGap(gabsize, gabsize, gabsize)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addGroup(layout.createSequentialGroup()
                .addComponent(title, labelWidth, labelWidth, labelWidth)
                .addComponent(ftitle, fieldWidth, fieldWidth, fieldWidth)
              )
              .addGroup(layout.createSequentialGroup()
                .addComponent(album, labelWidth, labelWidth, labelWidth)
                .addComponent(falbum, fieldWidth, fieldWidth, fieldWidth)
              )
              .addGroup(layout.createSequentialGroup()
                .addComponent(tags, labelWidth, labelWidth, labelWidth)
                .addComponent(ftags, fieldWidth, fieldWidth, fieldWidth)
              )
            )
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addGroup(layout.createSequentialGroup()
                .addComponent(autor, labelWidth, labelWidth, labelWidth)
                .addComponent(fautor, fieldWidth, fieldWidth, fieldWidth)
              )
              .addGroup(layout.createSequentialGroup()
                .addComponent(langue, labelWidth, labelWidth, labelWidth)
                .addComponent(flangue, fieldWidth, fieldWidth, fieldWidth)
              )
              .addGroup(layout.createSequentialGroup()
                .addComponent(specialtags, labelWidth, labelWidth, labelWidth)
                .addComponent(fspecialtags, fieldWidth, fieldWidth, fieldWidth)
              )
            )
          )
        )
        .addGroup(layout.createSequentialGroup()
          .addComponent(editLyric, (fieldWidth + labelWidth) + 24, (fieldWidth + labelWidth) + 24, (fieldWidth + labelWidth) + 24)
          .addComponent(gLyric, (fieldWidth + labelWidth) + 24, (fieldWidth + labelWidth) + 24, (fieldWidth + labelWidth) + 24)
        )
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(rowHeight / 2)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(autor, rowHeight, rowHeight, rowHeight)
              .addComponent(fautor, rowHeight, rowHeight, rowHeight)
            )
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(langue, rowHeight, rowHeight, rowHeight)
              .addComponent(flangue, rowHeight, rowHeight, rowHeight)
            )
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(specialtags, rowHeight, rowHeight, rowHeight)
              .addComponent(fspecialtags, rowHeight, rowHeight, rowHeight)
            )
          )
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(title, rowHeight, rowHeight, rowHeight)
              .addComponent(ftitle, rowHeight, rowHeight, rowHeight)
            )
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(album, rowHeight, rowHeight, rowHeight)
              .addComponent(falbum, rowHeight, rowHeight, rowHeight)
            )
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(tags, rowHeight, rowHeight, rowHeight)
              .addComponent(ftags, rowHeight, rowHeight, rowHeight)
            )
          )
        )
        .addGap(rowHeight / 4)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(editLyric, rowHeight + 8, rowHeight + 8, rowHeight + 8)
          .addComponent(gLyric, rowHeight + 8, rowHeight + 8, rowHeight + 8)
        )
        .addComponent(WE, 10, javax.swing.GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
      )
    );
  }

  private void updateButontext() {
    if (!lyric.getText().equals("")) {
      editLyric.setText(lyric.getText().substring(0, 30) + " ...");
    } else {
      editLyric.setText("Upravit text skladby");
    }
  }

  public final void load(Skladba s) {
    ftitle.setText(s.getTitle());
    fautor.setText(s.getAutor());
    falbum.setText(s.getAlbum());
    if (s.getLangue() != null && !s.getLangue().equals("none")) {
      flangue.setSelectedItem(s.getLangue());
    }
    ftags.setText(s.getTags());
    fspecialtags.setText(s.getSpecialTags());
    if (s.getMusicAnalizer() == null) {
      s.initMusicAnalizer();
    }
    WE.setMa(s.getMusicAnalizer());
    WE.setStart(s.getStart());
    if (s.getLenght() == 0) {
      WE.setEnd(s.getMusicAnalizer().getLenght());
    } else {
      WE.setEnd(s.getStart() + s.getLenght());
    }

    //if (s.getMusicAnalizer() != null) {
    //}
    lyric.setText(s.getLyric());
    updateButontext();
  }

  public final void clear() {
    ftitle.setText("");
    fautor.setText("");
    falbum.setText("");
    flangue.setSelectedItem("");
    ftags.setText("");
    fspecialtags.setText("");
    WE.setStart(0L);
    WE.setEnd(0L);
    lyric.setText("");
    editLyric.setText("Upravit text skladby");
    WE.setMa(new MusicAnalizer());
  }

  private void reGoogle() {
    Skladba s = new Skladba();
    apply(s);
    SongInfo.findLiric(s);
    lyric.setText(s.getLyric());
    updateButontext();
  }

  public void apply(Skladba s) {
    if (s.getMusicAnalizer() != null) {
      s.setVolume(s.getMusicAnalizer().getAverangeVolume());
    }
    s.setTitle(ftitle.getText());
    s.setAutor(fautor.getText());
    s.setAlbum(falbum.getText());
    if (flangue.getSelectedItem() == null) {
      s.setLangue("none");
    } else {
      s.setLangue(flangue.getSelectedItem().toString());
    }
    s.setTags(ftags.getText() + " ");
    s.setSpecialTags(fspecialtags.getText() + " ");
    s.setStart(WE.getStart());
    s.setLenght(WE.getEnd() - WE.getStart());
    s.setLyric(lyric.getText());
  }
}
