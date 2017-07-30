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

  public static String MilToTime(long m) {
    String min = Long.toString(m / 60000);
    String s = Double.toString((double) Math.round(((double) m / 60000 - Math.floor(m / 60000)) * 6000) / 100);
    return min + ":" + s;
  }
}
