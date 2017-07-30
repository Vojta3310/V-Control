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
import javax.sound.sampled.UnsupportedAudioFileException;
import org.farng.mp3.TagException;

/**
 *
 * @author vojta3310
 */
public class VControl {

  /**
   * @param args the command line arguments
   * @throws javax.sound.sampled.LineUnavailableException
   * @throws java.io.IOException
   * @throws org.farng.mp3.TagException
   */
  public static void main(String[] args) throws LineUnavailableException, IOException, TagException, UnsupportedAudioFileException {
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
