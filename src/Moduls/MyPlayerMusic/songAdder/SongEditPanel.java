/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import VControl.UI.components.MyComboUI;
import VControl.Settings.AppSettings;
import java.awt.Font;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author vojta3310
 */
public class SongEditPanel extends JPanel {

  private String[] settings;
  private final JLabel title;
  private final JLabel album;
  private final JLabel tags;
  private final JLabel specialtags;
  private final JLabel langue;
  private final JLabel autor;

  private final JTextField ftitle;
  private final JTextField falbum;
  private final JTextField ftags;
  private final JTextField fspecialtags;
  private final JComboBox<String> flangue;
  private final JTextField fautor;
  private final WaveEdit WE;
  private MusicAnalizer ma;

  public SongEditPanel(String[] set, MusicAnalizer ma) throws IOException, UnsupportedAudioFileException {
    this();
    load(set, ma);
  }

  public SongEditPanel() throws IOException {
    settings = new String[]{"", "", "", "", "", "", "", "",};
    ma = new MusicAnalizer();

    this.setBackground(AppSettings.getColour("BG_Color"));

    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));
    title = new JLabel("Název:");
    autor = new JLabel("Autor:");
    album = new JLabel("Album:");
    langue = new JLabel("Jazyk:");
    tags = new JLabel("Tagy:");
    specialtags = new JLabel("STagy:");
    WE = new WaveEdit(ma);

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

    title.setForeground(AppSettings.getColour("FG_Color"));
    autor.setForeground(AppSettings.getColour("FG_Color"));
    album.setForeground(AppSettings.getColour("FG_Color"));
    langue.setForeground(AppSettings.getColour("FG_Color"));
    tags.setForeground(AppSettings.getColour("FG_Color"));
    specialtags.setForeground(AppSettings.getColour("FG_Color"));

//    ftitle.setForeground(AppSettings.getColour("FG_Color"));
//    fautor.setForeground(AppSettings.getColour("FG_Color"));
//    falbum.setForeground(AppSettings.getColour("FG_Color"));
    flangue.setForeground(AppSettings.getColour("FG_Color"));
//    ftags.setForeground(AppSettings.getColour("FG_Color"));
//    fspecialtags.setForeground(AppSettings.getColour("FG_Color"));
//
//    ftitle.setCaretColor(AppSettings.getColour("FG_Color"));
//    fautor.setCaretColor(AppSettings.getColour("FG_Color"));
//    falbum.setCaretColor(AppSettings.getColour("FG_Color"));
//    ftags.setCaretColor(AppSettings.getColour("FG_Color"));
//    fspecialtags.setCaretColor(AppSettings.getColour("FG_Color"));
    flangue.setUI(new MyComboUI());
//
//    ftitle.setBackground(AppSettings.getColour("BG_Color"));
//    fautor.setBackground(AppSettings.getColour("BG_Color"));
//    falbum.setBackground(AppSettings.getColour("BG_Color"));
    flangue.setBackground(AppSettings.getColour("BG_Color"));
//    ftags.setBackground(AppSettings.getColour("BG_Color"));
//    fspecialtags.setBackground(AppSettings.getColour("BG_Color"));
//
//    ftitle.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//    fautor.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//    falbum.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    flangue.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//    ftags.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//    fspecialtags.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//
//    ftitle.setFont(f);
//    fautor.setFont(f);
//    falbum.setFont(f);
    flangue.setFont(f);
//    ftags.setFont(f);
//    fspecialtags.setFont(f);

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
        .addComponent(WE, 10, javax.swing.GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
      )
    );
  }

  public final void load(String[] set, MusicAnalizer m) throws IOException, UnsupportedAudioFileException {
    ma = m;
    settings = set;
    ftitle.setText(settings[0]);
    fautor.setText(settings[1]);
    falbum.setText(settings[2]);
    flangue.setSelectedItem(settings[3]);
    ftags.setText(settings[4]);
    fspecialtags.setText(settings[5]);
    if (!"".equals(settings[6])) {
      WE.setStart(Long.parseLong(settings[6]));
      if (!"".equals(settings[7])) {
        WE.setEnd(WE.getStart()+Long.parseLong(settings[7]));
      } else {
        WE.setEnd(0L);
      }
    } else {
      WE.setStart(0L);
    }
    WE.setMa(ma);
  }

  public String[] getSet() {
    String[] set = new String[]{
      ftitle.getText(),
      fautor.getText(),
      falbum.getText(),
      flangue.getSelectedItem().toString(),
      ftags.getText(),
      fspecialtags.getText(),
      Long.toString(WE.getStart()),
      Long.toString(WE.getEnd())};
    return set;
  }

}
