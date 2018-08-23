/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class Speaker extends Modul {

  public Speaker(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "Speaker";
  }

  @Override
  public boolean HaveGUI() {
    return false;
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_Command", "spd-say -l %L% -w \"%T%\"");
    p.setProperty("Modul_" + this.GetModulName() + "_Default_language", "cs");
  }

  @Override
  public void Execute(Command co) {
    if (co.GetCommand().equals("Say")) {
      Command c =new Command("Pause", "MyPlayerMusic", GetModulName());
      getCommander().Execute(c);
      c.vaitForDone();
      String text = co.GetParms().toString().replace(" ", "_");
      String l = SgetString("Default_language");
      while (text.contains("%")) {
        int i = text.indexOf("%");
        String t1 = text.substring(0, i);
        String l1 = text.substring(i + 1, i + 3);
        text = text.substring(i + 3);
        say(t1, l);
        l = l1;
      }
      say(text, l);
      getCommander().Execute(new Command("Play", "MyPlayerMusic", GetModulName()));
    }
  }

  private void say(String t, String l) {
    if (!t.equals("")) {
      try {
        String c = SgetString("Command");
        c = c.replace("%L%", l);
        c = c.replace("%T%", t);
//        System.out.println(c);
        Process p = Runtime.getRuntime().exec(c);
        p.waitFor();
        p.destroy();
      } catch (IOException | InterruptedException ex) {
        Logger.getLogger(Speaker.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void StartModule() {
  }

}
