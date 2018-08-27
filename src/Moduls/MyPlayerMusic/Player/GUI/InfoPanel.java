/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import VControl.UI.components.Stars;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Settings.AppSettings;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class InfoPanel extends JPanel {

  private final JLabel Title;
  private final JLabel Autor;
  private final JLabel Album;
  private final JLabel Tags;
  private final JLabel Played;
  private final JLabel Repeat;
  private final JLabel PlayedL;
  private final JLabel RepeatL;
  private final Stars stars;
  private final Font font;
  private Skladba sk;

  private AudioPlayer in;
  private FFT fft;

  float smoothing = 0.80F;
  private float[] fftSmooth;
  private int avgSize;

  private float minVal = 0.0F;
  private float maxVal = 0.0F;

  public InfoPanel() {
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setForeground(AppSettings.getColour("FG_Color"));
    font = new Font(AppSettings.getString("Font_Name"), 0, (int) 2 * AppSettings.getInt("Font_Size"));
    Title = new JLabel();
    Autor = new JLabel();
    Album = new JLabel();
    Tags = new JLabel();
    Played = new JLabel();
    Repeat = new JLabel();
    PlayedL = new JLabel();
    RepeatL = new JLabel();
    stars = new Stars();
    Title.setFont(font.deriveFont(1, 3 * AppSettings.getInt("Font_Size")));
    Autor.setFont(font);
    Album.setFont(font);
    Tags.setFont(font);
    Played.setFont(font.deriveFont(0, (int) (1.2 * AppSettings.getInt("Font_Size"))));
    Repeat.setFont(font.deriveFont(0, (int) (1.2 * AppSettings.getInt("Font_Size"))));
    PlayedL.setFont(font.deriveFont(1, (int) (0.5 * AppSettings.getInt("Font_Size"))));
    RepeatL.setFont(font.deriveFont(1, (int) (0.5 * AppSettings.getInt("Font_Size"))));
    Title.setForeground(AppSettings.getColour("FG_Color"));
    Autor.setForeground(AppSettings.getColour("FG_Color"));
    Album.setForeground(AppSettings.getColour("FG_Color"));
    Tags.setForeground(AppSettings.getColour("FG_Color"));
    Played.setForeground(AppSettings.getColour("FG_Color"));
    Repeat.setForeground(AppSettings.getColour("FG_Color"));
    PlayedL.setForeground(AppSettings.getColour("FG_Color"));
    RepeatL.setForeground(AppSettings.getColour("FG_Color"));
    PlayedL.setText("Hrála:");
    RepeatL.setText("Opakovat:");
    stars.setEditabe(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(16, 16, 16)
            .addComponent(Title))
          .addGroup(layout.createSequentialGroup()
            .addGap(50, 50, 50)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(Album)
              .addComponent(Autor))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1000, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(stars, 180, 180, 180)
              .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Played, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Repeat, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
              )
              .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(PlayedL, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RepeatL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
              //.addGap(2, 2, 2)
              )
            )
            .addContainerGap()
          ))
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(Title, 80, 80, 80))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Autor)
          .addComponent(stars, 32, 32, 32))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Album)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(Played)
              .addComponent(Repeat))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(PlayedL)
              .addComponent(RepeatL))
          ))
      )
    );

  }

  public void ShowSong(Skladba s) {
    sk = s;
    int w = 370;
    String txt = s.getTitle();
    if (Title.getGraphics() != null) {
      final FontMetrics fm = Title.getGraphics().getFontMetrics();
      while (fm.stringWidth(txt) > w) {
        txt = txt.substring(0, txt.length() - 4) + "...";
      }
      System.out.println(txt);
    } else if (txt.length() > 19) {
      txt = txt.substring(0, 20) + "...";
    }
    Title.setText(txt);

    Autor.setText(s.getAutor());
    Album.setText(s.getAlbum());
    Played.setText(Integer.toString(s.getPlayed()));
    Repeat.setText(Integer.toString(s.getRepead()));
    Tags.setText(s.getTags());
    stars.setStars(s.getOblibenost() * stars.getMaxStars());

    minVal = 0.0F;
    maxVal = 0.0F;
  }

  public void setupEqualizer(AudioPlayer ap) {

    in = ap;

//    in.loop();
    fft = new FFT(in.bufferSize(), in.sampleRate());

    // Use logarithmically-spaced averaging
    fft.logAverages(200, 10);

    avgSize = fft.avgSize();
    fftSmooth = new float[avgSize];

  }

  private Color makeColor(int i, int imax) {
    int a = (int) ((float) i / imax * 1530);

    int r = 255 - Math.max(Math.min(a - 255, 255), 0) + Math.max(Math.min(a - 1020, 255), 0);
    int g = Math.max(Math.min(a - 512, 255), 0) - Math.max(Math.min(a - 1275, 255), 0);
    int b = Math.min(a, 255) - Math.max(Math.min(a - 765, 255), 0);

    return new Color(r, g, b);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Repeat.setText(Integer.toString(sk.getRepead()));
    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    if (in != null) {
      fft.forward(in.mix);

      int weight = Math.round(getWidth() / (avgSize - 1)) + 1;
      int gap = Math.abs(getWidth() - weight * (avgSize - 1)) / 2;
      float maxHeight = (float) ((getHeight() / 1.5) * 0.75);

      for (int i = 0; i < avgSize; i++) {
        // Get spectrum value (using dB conversion or not, as desired)
        float fftCurr;

        fftCurr = fft.getAvg(i);

        // Smooth using exponential moving average
        fftSmooth[i] = (smoothing) * fftSmooth[i] + ((1 - smoothing) * fftCurr);

        // Find max and min values ever displayed across whole spectrum
        if (fftSmooth[i] > maxVal) {
          maxVal = fftSmooth[i];
        }
        if (fftSmooth[i] < minVal) {
          minVal = fftSmooth[i];
        }
      }

      // Calculate the total range of smoothed spectrum; this will be used to scale all values to range 0...1
      float range = maxVal - minVal;
      float scaleFactor = range + 0.00001F; // avoid div. by zero

      for (int i = 0; i < avgSize - 2; i++) {

        // Y-coord of display line; fftSmooth is scaled to range 0...1; this is then multiplied by maxHeight
        // to make it within display port range
        int fftSmoothDisplay = (int) (maxHeight * ((fftSmooth[i] - minVal) / scaleFactor));

        // X-coord of display line
        int x = i * weight + gap;
        g.setColor(makeColor(i, avgSize));

        g.fillRoundRect(x + 1, getHeight() - fftSmoothDisplay - 20, weight - 2, fftSmoothDisplay + 18, (weight - 2), (weight - 2));
      }
    }
  }

}
