/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import VControl.Settings.AppSettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.LINE_AXIS;
import static javax.swing.BoxLayout.PAGE_AXIS;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
    
//    pane.add(prvni, BorderLayout.PAGE_START);
//    pane.add(druhy, BorderLayout.CENTER);
//    pane.add(treti, BorderLayout.LINE_END);


    //BoxLayout l = new BoxLayout(pane, BoxLayout.LINE_AXIS);
    
    //pane.setLayout(l);
    //this.setLayout(new BoxLayout(this, LINE_AXIS));
  }

}
