/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import Moduls.AnotherModule;
import Moduls.MyPlayerMusic.MyPlayerMusic;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class VControl {

  /**
   * @param args the command line arguments
   * @throws javax.sound.sampled.LineUnavailableException
   */
  public static void main(String[] args) throws LineUnavailableException, IOException, TagException {
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
