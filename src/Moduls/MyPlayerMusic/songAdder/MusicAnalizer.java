/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import ddf.minim.Minim;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.UnsupportedAudioFileException;
import ddf.minim.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.Math.abs;

/**
 *
 * @author vojta3310
 */
public class MusicAnalizer {

  private ArrayList<Line2D.Double> lines = new ArrayList<>();
  private int h;
  private int w;
  private File file;
  public Minim minim;
  AudioPlayer player;
  float[] audioData;
  long Lenght = 0;
  float Volume;

  public void createWaveForm() throws IOException, UnsupportedAudioFileException {
    if (file != null) {
      if (Lenght == 0) {
        initAudioStream();
      }
      lines = new ArrayList<>();
      int frames_per_pixel =Math.round(audioData.length / (w+0.001f));
      double y_last = 0;
      for (double x = 0; x < w; x++) {
        int idx = (int) (frames_per_pixel * x);
        double y_new = (double) (h * 0.6 * audioData[idx] + h / 2);
        lines.add(new Line2D.Double(x - 1, y_last, x, y_new));
        y_last = y_new;
      }
    }
  }

  public void Paint(Graphics2D g2) {
    for (int i = 1; i < lines.size(); i++) {
      g2.draw((Line2D) lines.get(i));
    }
  }

  public long getLenght() {
    return Lenght;
  }

  public float getAverangeVolume() {
    return Volume;
  }

  public String sketchPath(String fileName) {
    return fileName;
  }

  public InputStream createInput(String fileName) throws FileNotFoundException {
    return new FileInputStream(fileName);
  }

  public void initAudioStream() throws UnsupportedAudioFileException, IOException {
    if (file != null) {
      minim = new Minim(this);
      minim.debugOff();
      AudioSample jingle = minim.loadSample(file.toString(), 2048);
      audioData = jingle.getChannel(AudioSample.LEFT);
      Lenght = jingle.length();
      minim.stop();

      double sum = 0;
      for (int i = 0; i < audioData.length; i++) {
        sum += abs(audioData[i]);
      }
      Volume = (float) sum / audioData.length;
    }
  }

  public void setFile(File f) {
    file = f;
  }

  public int getH() {
    return h;
  }

  public void setH(int h) {
    this.h = h;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public void setSize(Dimension d) {
    this.w = (int) d.getWidth();
    this.h = (int) d.getHeight();
  }

  public Dimension getSize() {
    return new Dimension(w, h);
  }

  public File getFile() {
    return file;
  }
}
