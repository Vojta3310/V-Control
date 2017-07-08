/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import javax.swing.JFrame;

/**
 *
 * @author vojta3310
 */
public class GUI extends JFrame {

  public GUI() throws HeadlessException {
    super("V-Control v1.0");
    this.setSize(AppSettings.getInt("Window_Width"), AppSettings.getInt("Window_Height"));
    this.setUndecorated(AppSettings.getBool("Window_Undecorated"));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    Container pane = this.getContentPane();
    pane.setLayout(new BorderLayout());
  }

}
