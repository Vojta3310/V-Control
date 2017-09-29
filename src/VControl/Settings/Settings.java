/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.Settings;

import VControl.Commander;
import VControl.Command;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author vojta3310
 */
public class Settings {

  private final Commander commander;
  public static Properties p;
  public static Path path;

  public Settings(Commander comander) {
    if (System.getProperty("os.name").equals("Linux")) {
      Settings.path = Paths.get(System.getProperty("user.home") + File.separator + ".V-Control");
    } else {
      Settings.path = Paths.get(System.getenv("APPDATA") + File.separator + ".V-Control");
    }
    this.commander = comander;
    Settings.p = new Properties();
  }

  public void load() {
    InputStream input = null;
    try {
      if (!Files.exists(path)) {
        this.makeDefault();
        this.save();
        return;
      }
      input = new FileInputStream(path.toString() + File.separator + "config.properties");
      p.load(input);
    } catch (IOException ex) {
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public void save() {
    OutputStream output = null;
    try {
      if (!Files.exists(path)) {
        Files.createDirectory(path);
      }
      output = new FileOutputStream(path.toString() + File.separator + "config.properties");
      p.store(output, null);
    } catch (IOException io) {
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
        }
      }
    }
  }

  public void makeDefault() {
    commander.getDefaultSettings(p);
  }

  public void doCommand(Command co) {
    if (co.GetFor().equals("Settings")) {
      if (co.GetCommand().equals("Save")) {
        this.save();
      }
      if (co.GetCommand().equals("Load")) {
        this.load();
      }
      if (co.GetCommand().equals("RestoreDefault")) {
        this.makeDefault();
      }
    }
  }
}
