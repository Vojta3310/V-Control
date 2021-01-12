/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl;

import Moduls.KeyLoger;
import Moduls.MyPlayerVideo.MyPlayerVideo;
import Moduls.MyPlayerMusic.MyPlayerMusic;
import Moduls.ResolutionUpdater;
import Moduls.ScreenSaver.ScSaverModule;
import Moduls.Skladnik.SkladnikModule;
import Moduls.Speaker;
import Moduls.Weader.WeaderModule;
import VControl.Settings.AppSettings;
import Moduls.Mailer.Mailer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
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

    Logger rootLogger = Logger.getLogger("");
    Handler[] handlers = rootLogger.getHandlers();
    if (handlers[0] instanceof ConsoleHandler) {
      rootLogger.removeHandler(handlers[0]);
    }

    rootLogger.addHandler(new Handler() {

      @Override
      public void publish(LogRecord record) {
        System.out.println("[" + calcDate(record.getMillis())
          + "] [" + record.getLevel().toString() + "] [" + record.getLoggerName() + ":" + record.getSourceMethodName()
          + "] " + record.getMessage());
      }

      @Override
      public void flush() {

      }

      @Override
      public void close() throws SecurityException {

      }

      private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
      }
    });

    Commander Commander = new Commander();
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "Commander created.");
    if (AppSettings.getBool("Bebugg_log")) {
      LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
      Logger.getLogger("Moduls").setLevel(Level.ALL);
      Logger.getLogger("VControl").setLevel(Level.ALL);
    } else {
      LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.SEVERE);
      Logger.getLogger("Moduls").setLevel(Level.SEVERE);
      Logger.getLogger("VControl").setLevel(Level.SEVERE);
    }

    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Creating modules.");
    Commander.addIModule(new Speaker(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "SpeakerModule added.");
    Commander.addIModule(new ResolutionUpdater(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "ResolutionUpdaterModule added.");
    MyPlayerMusic a = new MyPlayerMusic(Commander);
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "MyPlayerMusicModule created.");
    Commander.addIModule(new KeyLoger(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "KeyLogger created.");
    Commander.addIModule(a);
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "KeyLogger added.");
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "Mailer created.");
    Commander.addIModule(new Mailer(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "Mailer added.");
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "MyPlayerMusicModule added.");
    Commander.addIModule(new MyPlayerVideo(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "MyPlayerVideoModule added.");    
    Commander.addIModule(new SkladnikModule(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "Skladnik added.");
    Commander.addIModule(new WeaderModule(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "WeaderModule added.");
    Commander.addIModule(new ScSaverModule(Commander));
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "ScreenSaver added.");
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Modules prepareded.");
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Starting modules.");
    Commander.StartModules();
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Modules started.");
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "Creating QUI.");
    Commander.RegisterGUI();
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.INFO, "GUI created.");
    a.Activate();
    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.FINEST, "First module activated.");
  }

}
