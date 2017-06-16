/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.ICommand;
import java.util.Properties;

/**
 *
 * @author vojta3310
 */
public class AnotherModule extends Modul implements IModul {

  public AnotherModule(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public boolean doCommand(ICommand co) {
    return false;
  }

  @Override
  public String GetModulName() {
    return "MyPlayerMusic";
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void Execute(ICommand co) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getDefaultSettings(Properties p) {
    ;
  }

}
