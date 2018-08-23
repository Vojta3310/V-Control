/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.ScreenSaver;

import Moduls.Modul;
import VControl.Command;
import ddf.minim.AudioPlayer;
import ddf.minim.analysis.FFT;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author vojta3310
 */
public class SaverPlane extends JPanel {

  private BufferedImage image;
  private final RandomImage RI;
  private final Modul m;
  private int imgTime = 0;
  private int imgDark = 255;
  private Command com;

  private AudioPlayer in;
  private FFT fft;

  float smoothing = 0.80F;
  private float[] fftSmooth;
  private int avgSize;

  private float minVal = 0.0F;
  private float maxVal = 0.0F;

  public SaverPlane(Modul m) {
    this.m = m;
    RI = new RandomImage(m.SgetString("ImageDir"));
    RI.start();
    changeImg();
//    setLayout(new FlowLayout());
//    add(new TimePanel());
//    add(new SongPanel(m));
    com = new Command("APget", "MyPlayerMusic", m.GetModulName());
    JPanel time = new TimePanel();
    JPanel song = new SongPanel(m);
    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

// Create a new blank cursor.
    Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
      cursorImg, new Point(0, 0), "blank cursor");

// Set the blank cursor to the JFrame.
    setCursor(blankCursor);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(song, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(780, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(song, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(377, Short.MAX_VALUE))
    );

    Timer tim = new Timer(57, (ActionEvent ae) -> {
      imgTime++;

      if (imgTime > m.SgetInt("Image_Delay(s)") * 100 - m.SgetInt("Transfet_Lenght") * 50) {

        imgDark = 255 - (int) (((float) (m.SgetInt("Image_Delay(s)") * 100 - imgTime) / (float) (m.SgetInt("Transfet_Lenght") / 2)) * 255 / 100);
        if (imgDark < 0) {
          imgDark = 0;
        }
        if (imgDark > 255) {
          imgDark = 255;
        }
      }
      if (imgTime / 100 > m.SgetInt("Image_Delay(s)")) {
        changeImg();
        imgTime = 0;
        imgDark = 255;
      }
      if (imgTime / 100 <= m.SgetInt("Transfet_Lenght") / 2) {
        imgDark = 255 - (int) ((float) imgTime / (float) (m.SgetInt("Transfet_Lenght") / 2) * 255 / 100);
        if (imgDark < 0) {
          imgDark = 0;
        }
        if (imgDark > 255) {
          imgDark = 255;
        }
      }

      if (imgTime % 100 == 0) {
        if (com.isDone()) {
          if ((AudioPlayer) com.getResults() != in) {
            setupEqualizer((AudioPlayer) com.getResults());
          }
        }
        com = new Command("APget", "MyPlayerMusic", m.GetModulName());
        m.getCommander().Execute(com);
      }
      repaint();
      revalidate();
    });
    tim.start();

  }

private void changeImg() {
    image=RI.getImage();
  }

  public final void setupEqualizer(AudioPlayer ap) {

    in = ap;

//    in.loop();
    fft = new FFT(in.bufferSize(), in.sampleRate());

    // Use logarithmically-spaced averaging
    fft.linAverages(250);
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
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters    
    g.setColor(new Color(0, 0, 0, imgDark));
    g.fillRect(0, 0, getWidth(), getHeight());

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    if (in != null) {
      fft.forward(in.mix);

      int weight = Math.round(getWidth() / (avgSize - 1)) + 1;
      int gap = Math.abs(getWidth() - weight * (avgSize - 1)) / 2;
      float maxHeight = (float) (getHeight() / 5);
      float maxValA=0;
      float minValA=Float.MAX_VALUE;
      
      for (int i = 0; i < avgSize; i++) {
        // Get spectrum value (using dB conversion or not, as desired)
        float fftCurr;

        fftCurr = fft.getAvg(i);

        // Smooth using exponential moving average
        fftSmooth[i] = (smoothing) * fftSmooth[i] + ((1 - smoothing) * (float)Math.atan(0.2*fftCurr/(avgSize-i)+0.0001));

        // Find max and min values ever displayed across whole spectrum
        if (fftSmooth[i] > maxValA) {
          maxValA = fftSmooth[i];
        }
        if (fftSmooth[i] < minValA) {
          minValA = fftSmooth[i];
        }
      }
      maxVal=0.99f*maxVal+0.01f*maxValA;
      minVal=0.99f*minVal+0.01f*minValA;
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

        g.fillRoundRect(x - 65, getHeight() - fftSmoothDisplay - 10, weight - 2, fftSmoothDisplay + 8, (weight - 2), (weight - 2));
      }
    }
  }

}
