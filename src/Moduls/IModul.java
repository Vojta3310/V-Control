/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import VControl.UI.SidebarModule;
import java.awt.Image;
import java.util.Properties;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public interface IModul {

  public void doCommand(Command co);

  public String GetModulName();

  public boolean HaveGUI();

  public Image GetIcon();

  public void Activate();

  public void Deactivate();

  public JPanel getGrafics();

  public void setButton(SidebarModule sidebatButton);

  public SidebarModule getSidebarButton();

  public void DefaultSettings(Properties p);

  void getDefaultSettings(Properties p);

  void Execute(Command co);

  void start();

  void StartModule();
}
