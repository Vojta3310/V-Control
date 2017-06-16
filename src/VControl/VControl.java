/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import Moduls.AnotherModule;
import Moduls.MyPlayerMusic.MyPlayerMusic;

/**
 *
 * @author vojta3310
 */
public class VControl {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Commander Commander = new Commander();
    MyPlayerMusic a = new MyPlayerMusic(Commander);
    Commander.addIModule(a);
    Commander.addIModule(new AnotherModule(Commander));
    Commander.addIModule(new AnotherModule(Commander));
    Commander.addIModule(new AnotherModule(Commander));
    Commander.addIModule(new AnotherModule(Commander));
    Commander.addIModule(new AnotherModule(Commander));
    Commander.addIModule(new AnotherModule(Commander));


    Commander.RegisterGUI();
    a.Activate();
  }

}
