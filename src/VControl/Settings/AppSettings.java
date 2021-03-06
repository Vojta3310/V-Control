/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.Settings;

import java.awt.Color;
import java.util.Properties;

/**
 *
 * @author vojta3310
 */
public class AppSettings {

  public static void DefaultSettings(Properties p) {
    p.setProperty("@_FG_Color", "00A0FF");
    p.setProperty("@_BG_Color", "000000");
    p.setProperty("@_Window_Width", "1366");
    p.setProperty("@_Window_Height", "768");
    p.setProperty("@_Window_Undecorated", "false");
    p.setProperty("@_Icon_Size", "150");
    p.setProperty("@_Border_Size", "6");
    p.setProperty("@_Icon_Chanhe_Color", "true");
    p.setProperty("@_Font_Name", "Arial");
    p.setProperty("@_Font_Size", "20");
    p.setProperty("@_Bebugg_log", "true");
    p.setProperty("@_Change_Cursor", "true");

  }

  public static String getString(String key) {
    return Settings.p.getProperty("@_" + key);
  }

  public static int getInt(String key) {
    return Integer.parseInt(Settings.p.getProperty("@_" + key));
  }

  public static boolean getBool(String key) {
    return Boolean.parseBoolean(Settings.p.getProperty("@_" + key));
  }

  public static Color getColour(String key) {
    return new Color(Integer.parseInt(Settings.p.getProperty("@_" + key), 16));
  }
}
