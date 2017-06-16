/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.ICommand;
import VControl.UI.SidebarModule;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author vojta3310
 */
public interface IModul {

  public boolean doCommand(ICommand co);

  public String GetModulName();

  public boolean HaveGUI();

  public ImageIcon GetIcon();

  public void Activate();

  public void Deactivate();

  public JPanel getGrafics();

  public void setButton(SidebarModule sidebatButton);
  
  public SidebarModule getSidebarButton();

  public void DefaultSettings(Properties p);

  void getDefaultSettings(Properties p);

  void Execute(ICommand co);
}
