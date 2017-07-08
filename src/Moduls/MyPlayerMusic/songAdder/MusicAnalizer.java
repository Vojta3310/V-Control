/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 *
 * @author vojta3310
 */
public class MusicAnalizer {

  private AudioInputStream audioInputStream;
  private ArrayList<Line2D.Double> lines = new ArrayList<>();
  protected AudioFileFormat audioFileFormat;
  protected Map<String, Object> properties;
  private int h;
  private int w;
  private File file;

  public void createWaveForm() throws IOException, UnsupportedAudioFileException {
    initAudioStream();
    lines = new ArrayList<>();
    if (audioInputStream != null) {
      byte[] audioBytes;
      lines.clear();
      AudioFormat format = audioInputStream.getFormat();
      System.out.println(audioFileFormat.getFrameLength());
      audioBytes = new byte[getByteLength()];//(int) (audioFileFormat.getFrameLength() * format.getFrameSize())];
      audioInputStream.read(audioBytes);

      int[] audioData = null;

      if (format.getSampleSizeInBits() == 16) {
        int nlengthInSamples = audioBytes.length / 2;
        audioData = new int[nlengthInSamples];
        if (format.isBigEndian()) {
          for (int i = 0; i < nlengthInSamples; i++) {
            /* First byte is MSB (high order) */
            int MSB = (int) audioBytes[2 * i];
            /* Second byte is LSB (low order) */
            int LSB = (int) audioBytes[2 * i + 1];
            audioData[i] = MSB << 8 | (255 & LSB);
          }
        } else {
          for (int i = 0; i < nlengthInSamples; i++) {
            /* First byte is LSB (low order) */
            int LSB = (int) audioBytes[2 * i];
            /* Second byte is MSB (high order) */
            int MSB = (int) audioBytes[2 * i + 1];
            audioData[i] = MSB << 8 | (255 & LSB);
          }
        }
      } else if (format.getSampleSizeInBits() == 8) {
        int nlengthInSamples = audioBytes.length;
        audioData = new int[nlengthInSamples];
        if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
          for (int i = 0; i < audioBytes.length; i++) {
            audioData[i] = audioBytes[i];
          }
        } else {
          for (int i = 0; i < audioBytes.length; i++) {
            audioData[i] = audioBytes[i] - 128;
          }
        }
      }

      int frames_per_pixel = audioBytes.length / format.getFrameSize() / w;
      byte my_byte;
      double y_last = 0;
      int numChannels = format.getChannels();
      for (double x = 0; x < w && audioData != null; x++) {
        int idx = (int) (frames_per_pixel * numChannels * x);
        if (format.getSampleSizeInBits() == 8) {
          my_byte = (byte) audioData[idx];
        } else {
          my_byte = (byte) (128 * audioData[idx] / 32768);
        }
        double y_new = (double) (h * (128 - my_byte) / 256);
        lines.add(new Line2D.Double(x, y_last, x, y_new));
        y_last = y_new;
      }
    }
  }

  public void saveToFile(String filename) {
    BufferedImage bufferedImage = new BufferedImage(lines.size(), h, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = bufferedImage.createGraphics();

    Paint(g2);
    g2.dispose();
    // Write generated image to a file
    try {
      // Save as PNG
      File file = new File(filename);
      ImageIO.write(bufferedImage, "png", file);
    } catch (IOException e) {
    }
  }

  public void Paint(Graphics2D g2) {
    for (int i = 1; i < lines.size(); i++) {
      g2.draw((Line2D) lines.get(i));
    }
  }

  public int getByteLength() {
    int bytesLength = AudioSystem.NOT_SPECIFIED;
    if (properties != null) {
      if (properties.containsKey("audio.length.bytes")) {
        bytesLength = ((Integer) properties.get("audio.length.bytes"));
      }
    }
    return bytesLength;
  }

  private void initAudioInputStream() {

//                throw new PlayerException(ex);
//                throw new PlayerException(ex);
    AudioFormat sourceAudioFormat = audioInputStream.getFormat();
    int nSampleSizeInBits = sourceAudioFormat.getSampleSizeInBits();
    if (nSampleSizeInBits <= 0) {
      nSampleSizeInBits = 16;
    }
    if ((sourceAudioFormat.getEncoding() == AudioFormat.Encoding.ULAW) || (sourceAudioFormat.getEncoding() == AudioFormat.Encoding.ALAW)) {
      nSampleSizeInBits = 16;
    }
    if (nSampleSizeInBits != 8) {
      nSampleSizeInBits = 16;
    }
    AudioFormat targetAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceAudioFormat.getSampleRate(), nSampleSizeInBits, sourceAudioFormat.getChannels(), sourceAudioFormat.getChannels() * (nSampleSizeInBits / 8), sourceAudioFormat.getSampleRate(), false);
    audioInputStream = AudioSystem.getAudioInputStream(targetAudioFormat, audioInputStream);
    if (audioFileFormat instanceof TAudioFileFormat) {
      // Tritonus SPI compliant audio file format.
      properties = ((TAudioFileFormat) audioFileFormat).properties();
      // Clone the Map because it is not mutable.
      properties = deepCopy(properties);
    } else {
      properties = new HashMap<>();
    }
    if (audioFileFormat.getByteLength() > 0) {
      properties.put("audio.length.bytes", audioFileFormat.getByteLength());
    }
    if (audioFileFormat.getFrameLength() > 0) {
      properties.put("audio.length.frames", audioFileFormat.getFrameLength());
    }
    if (audioFileFormat.getType() != null) {
      properties.put("audio.type", audioFileFormat.getType().toString());
    }
    AudioFormat audioFormat = audioFileFormat.getFormat();
    if (audioFormat.getFrameRate() > 0) {
      properties.put("audio.framerate.fps", audioFormat.getFrameRate());
    }
    if (audioFormat.getFrameSize() > 0) {
      properties.put("audio.framesize.bytes", audioFormat.getFrameSize());
    }
    if (audioFormat.getSampleRate() > 0) {
      properties.put("audio.samplerate.hz", audioFormat.getSampleRate());
    }
    if (audioFormat.getSampleSizeInBits() > 0) {
      properties.put("audio.samplesize.bits", audioFormat.getSampleSizeInBits());
    }
    if (audioFormat.getChannels() > 0) {
      properties.put("audio.channels", audioFormat.getChannels());
    }
    if (audioFormat instanceof TAudioFormat) {
      // Tritonus SPI compliant audio format.
      properties.putAll(((TAudioFormat) audioFormat).properties());
    }
  }

  protected Map<String, Object> deepCopy(Map<String, Object> src) {
    Map<String, Object> map = new HashMap<>();
    if (src != null) {
      Set<String> keySet = src.keySet();
      for (String key : keySet) {
        Object value = src.get(key);
        map.put(key, value);
//                logger.info("key: {}, value: {}", key, value);
      }
    }
    return map;
  }

  protected long getTimeLengthEstimation(Map properties) {
    long milliseconds = AudioSystem.NOT_SPECIFIED;
    int byteslength = AudioSystem.NOT_SPECIFIED;
    if (properties != null) {
      if (properties.containsKey("audio.length.bytes")) {
        byteslength = ((Integer) properties.get("audio.length.bytes"));
      }
      if (properties.containsKey("duration")) {
        milliseconds = (int) (((Long) properties.get("duration")) / 1000);
      } else {
        // Try to compute duration
        int bitspersample = AudioSystem.NOT_SPECIFIED;
        int channels = AudioSystem.NOT_SPECIFIED;
        float samplerate = AudioSystem.NOT_SPECIFIED;
        int framesize = AudioSystem.NOT_SPECIFIED;
        if (properties.containsKey("audio.samplesize.bits")) {
          bitspersample = ((Integer) properties.get("audio.samplesize.bits"));
        }
        if (properties.containsKey("audio.channels")) {
          channels = ((Integer) properties.get("audio.channels"));
        }
        if (properties.containsKey("audio.samplerate.hz")) {
          samplerate = ((Float) properties.get("audio.samplerate.hz"));
        }
        if (properties.containsKey("audio.framesize.bytes")) {
          framesize = ((Integer) properties.get("audio.framesize.bytes"));
        }
        if (bitspersample > 0) {
          milliseconds = (long) (1000.0f * byteslength / (samplerate * channels * (bitspersample / 8)));
        } else {
          milliseconds = (long) (1000.0f * byteslength / (samplerate * framesize));
        }
      }
    }
    return milliseconds * 1000;
  }

  public long getLenght() {
    if (audioInputStream != null) {
      long duration;
      if (properties.containsKey("duration")) {
        duration = ((Long) properties.get("duration"));
      } else {
        duration = getTimeLengthEstimation(properties);
      }
      return duration;
    } else {
      return 1;
    }
  }

  public float getAverangeVolume() {
    return 0; //TODO calculate it
  }

  public AudioInputStream getAudioInputStream() {
    return audioInputStream;
  }

  public void initAudioStream() throws UnsupportedAudioFileException, IOException {
    if (file != null) {
      final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(file);

      AudioFormat sourceFormat = sourceAIS.getFormat();
      AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
      final AudioInputStream convert1AIS = AudioSystem.getAudioInputStream(convertFormat, sourceAIS);
      final AudioInputStream convert2AIS = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, convert1AIS);

      audioInputStream = convert2AIS;
      audioFileFormat = AudioSystem.getAudioFileFormat(file);
      initAudioInputStream();
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
}
