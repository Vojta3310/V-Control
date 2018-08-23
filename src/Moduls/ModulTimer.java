/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import VControl.CommandStats;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class ModulTimer extends Thread {

  private final Modul mod;

  public ModulTimer(Modul mod) {
    this.mod = mod;
  }

  @Override
  public void run() {
    while (true) {
      if (!mod.getCommands().isEmpty()) {
        Command c = mod.getCommands().get(0);
        if (c != null) {
          c.setStats(CommandStats.InProgress);
          mod.Execute(c);
          c.setStats(CommandStats.Done);
        }
        mod.getCommands().remove(0);
      }
      try {
        Thread.sleep(14);
      } catch (InterruptedException ex) {
        Logger.getLogger(ModulTimer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
