/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.ScreenSaver;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class RandomImage extends Thread {

  private BufferedImage image;
  private ArrayList<String> Imges = new ArrayList<>();
  private boolean newImage = true;
  private final String dir;

  public RandomImage(String dir) {
    this.dir = dir;
    changeImg();
  }

  public synchronized BufferedImage getImage() {
    newImage = true;
    return image;
  }

  @Override
  public void run() {
    while (true) {
      if (newImage) {
        changeImg();
        newImage = false;
      }
      try {
        Thread.sleep(1001);
      } catch (InterruptedException ex) {
        Logger.getLogger(RandomImage.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  private void changeImg() {
    try {
//      image = ImageIO.read(new File("/home/vojta3310/Obrázky/Walpapapers/1cfdd2ae9f28d352d2853628cdb70659-TreyRatcliff.jpg"));
      if (Imges.isEmpty()) {
        loadDir(dir);
      }
      int i = (int) Math.round(Math.random() * (Imges.size() - 1));
      image = ImageIO.read(new File(Imges.get(i)));
      Imges.remove(i);

    } catch (IOException | TagException ex) {
      Logger.getLogger(SaverPlane.class.getName()).log(Level.SEVERE, null, ex);
    }
    image = utiliti.resize(image, AppSettings.getInt("Window_Width"), AppSettings.getInt("Window_Height"));

  }

  private void loadDir(String path) throws IOException, TagException {
    File f = new File(path);
    String[] files = f.list();
    Imges = new ArrayList<>();
    for (String file : files) {
      File fil = new File(path + File.separator + file);
      if (fil.isDirectory()) {
        loadDir(fil.getPath());
      } else {
        Imges.add(fil.getPath());
      }
    }
  }
}
