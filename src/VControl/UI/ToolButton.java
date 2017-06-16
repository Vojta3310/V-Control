/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI;

import Moduls.IModul;
import VControl.Settings.AppSettings;
import VControl.utiliti;
import com.sun.org.apache.xpath.internal.functions.Function;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.xml.transform.TransformerException;

/**
 *
 * @author vojta3310
 */
public class ToolButton extends JButton {

  public ToolButton(ImageIcon ico) {
    if (AppSettings.getBool("Icon_Chanhe_Color")) {
      BufferedImage a = utiliti.toBufferedImage(ico.getImage());
      utiliti.changeColor(a, AppSettings.getColour("BG_Color"),
        AppSettings.getColour("FG_Color"));
      ico.setImage(a);
    }
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setIcon(ico);
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), 0));
    this.setPreferredSize(new Dimension(AppSettings.getInt("Icon_Size"),
      AppSettings.getInt("Icon_Size")));

  }

  public void Activate() {
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), AppSettings.getInt("Border_Size")));
  }

  public void Deactivate() {
    this.setBorder(javax.swing.BorderFactory.createLineBorder(
      AppSettings.getColour("FG_Color"), 0));
  }

  public void onClick() {

  }
}
