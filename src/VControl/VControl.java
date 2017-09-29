/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import Moduls.KeyLoger;
import Moduls.MyPlayerVideo.MyPlayerVideo;
import Moduls.MyPlayerMusic.MyPlayerMusic;
import Moduls.Skladnik.SkladnikModule;
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
   * @throws javax.sound.sampled.UnsupportedAudioFileException
   */
  public static void main(String[] args) throws LineUnavailableException, IOException, TagException, UnsupportedAudioFileException {
    Commander Commander = new Commander();
    MyPlayerMusic a = new MyPlayerMusic(Commander);
    Commander.addIModule(a);
    Commander.addIModule(new SkladnikModule(Commander));
    Commander.addIModule(new MyPlayerVideo(Commander));
    Commander.addIModule(new KeyLoger(Commander));
    Commander.StartModules();
    Commander.RegisterGUI();
    a.Activate();
  }

}
