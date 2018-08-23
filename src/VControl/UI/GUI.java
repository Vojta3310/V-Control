/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import VControl.Settings.AppSettings;
import VControl.utiliti;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author vojta3310
 */
public class GUI extends JFrame {

  public void update() {
    this.setSize(AppSettings.getInt("Window_Width"), AppSettings.getInt("Window_Height"));
    //this.setVisible(true);
    repaint();
    revalidate();
  }

  public GUI() throws HeadlessException {
    super("V-Control v0.0.2");
    this.setSize(AppSettings.getInt("Window_Width"), AppSettings.getInt("Window_Height"));
    this.setUndecorated(AppSettings.getBool("Window_Undecorated"));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    Container pane = this.getContentPane();
    pane.setLayout(new BorderLayout());
    if (AppSettings.getBool("Change_Cursor")) {
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      try {
        BufferedImage image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/app/Cursor.png")));
        if (AppSettings.getBool("Icon_Chanhe_Color")) {
          utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
            AppSettings.getColour("FG_Color"));
        }
        Cursor c = toolkit.createCustomCursor(image, new Point(0, 0), "img");

        setCursor(c);
      } catch (IOException ex) {
        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

}
