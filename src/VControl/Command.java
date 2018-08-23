/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class Command {

  private final String command;
  private final Object parms;
  private Object results;
  private final String For;
  private final String From;
  private CommandStats stats;

  public Command(String command, String For, String From) {
    this.command = command;
    this.For = For;
    this.From = From;
    this.parms = "nan";
    this.stats = CommandStats.Set;
  }

  public Command(String command, Object parms, String For, String From) {
    this.command = command;
    this.parms = parms;
    this.For = For;
    this.From = From;
    this.stats = CommandStats.Set;
  }

  public String GetFor() {
    return For;
  }

  public String GetFrom() {
    return From;
  }

  public String GetCommand() {
    return command;
  }

  public Object GetParms() {
    return parms;
  }

  public Object getResults() {
    return results;
  }

  public boolean isDone() {
    return stats == CommandStats.Done;
  }

  public CommandStats getStat() {
    return stats;
  }

  public void setResults(Object results) {
    this.results = results;
  }

  public void setStats(CommandStats stats) {
    this.stats = stats;
  }

  public Object vaitForDone() {
    while (!isDone()) {
      try {
        Thread.sleep(106);
      } catch (InterruptedException ex) {
        Logger.getLogger(Command.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return results;
  }
}
