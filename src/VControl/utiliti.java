/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 *
 * @author vojta3310
 */
public class utiliti {

  public static void changeColor(BufferedImage img, Color old, Color newColor) {
    final int oldRGB = old.getRGB();
    final int newRGB = newColor.getRGB();
    for (int x = 0; x < img.getWidth(); x++) {
      for (int y = 0; y < img.getHeight(); y++) {
        if (img.getRGB(x, y) == oldRGB) {
          img.setRGB(x, y, newRGB);
        }
      }
    }
  }

  public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }

  public static BufferedImage resize(BufferedImage img, int newW, int newH) {
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }

  public static String MilToTime(long m) {
    String min = Long.toString(m / 60000);
    String s = Double.toString((double) Math.round(((double) m / 60000 - Math.floor(m / 60000)) * 6000) / 100);
    return min + ":" + s;
  }

  public static BufferedImage getImage(String path) {
    BufferedImage img = null;
    try {

      URL url = new URL(path);
      final HttpURLConnection connection = (HttpURLConnection) url
        .openConnection();
      connection.setReadTimeout(500);
      connection.setConnectTimeout(500);
      connection.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
//      System.out.println(connection.getInputStream().read());
      img = ImageIO.read(connection.getInputStream());

    } catch (MalformedURLException ex) {
      Logger.getLogger(utiliti.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(utiliti.class.getName()).log(Level.SEVERE, null, ex);
    }
    return img;
  }

  public static String titleize(final String in) {
    String input = in.toLowerCase();
    Pattern bound = Pattern.compile("\\b(\\w)");
    StringBuffer sb = new StringBuffer(input.length());
    Matcher mat = bound.matcher(input);
    while (mat.find()) {
      mat.appendReplacement(sb, mat.group().toUpperCase());
    }
    mat.appendTail(sb);
    return sb.toString();
  }

  public static String stripAccents(String s) {
    s = Normalizer.normalize(s, Normalizer.Form.NFD);
    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    return s;
  }
}
