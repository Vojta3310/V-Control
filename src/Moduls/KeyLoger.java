/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

/**
 *
 * @author vojta3310
 */
public class KeyLoger extends Modul {

  private boolean special = false;
  private boolean special2 = false;

  public KeyLoger(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "KeyLogger";
  }

  @Override
  public boolean HaveGUI() {
    return false;
  }

  @Override
  public void getDefaultSettings(Properties p) {
    //do nothing
  }

  @Override
  public void Execute(Command co) {
    //do nothing
  }

  @Override
  public void StartModule() {
// Get the logger for "org.jnativehook" and set the level to warning.
    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.WARNING);
//      Logger.getLogger(KeyLoger.class.getName()).log(Level.SEVERE, "text");

// Don't forget to disable the parent handlers.
    logger.setUseParentHandlers(false);
    try {
      GlobalScreen.registerNativeHook();
    } catch (NativeHookException ex) {
      Logger.getLogger(KeyLoger.class.getName()).log(Level.SEVERE, null, ex);
    }
    GlobalScreen.addNativeMouseListener(new NativeMouseListener() {

      @Override
      public void nativeMouseClicked(NativeMouseEvent nme) {
//        System.out.println("clik");
      }

      @Override
      public void nativeMousePressed(NativeMouseEvent nme) {
        Command c = new Command("MouseClick", "all", GetModulName());
        getCommander().Execute(c);
//        System.out.println("clik");
      }

      @Override
      public void nativeMouseReleased(NativeMouseEvent nme) {
//        System.out.println("clik");
      }
    });

    GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

      @Override
      public void nativeKeyTyped(NativeKeyEvent e) {
        //do nothing
      }

      @Override
      public void nativeKeyReleased(NativeKeyEvent e) {
        if (e.getRawCode() == 165) {
          special = false;
        }
        if (e.getRawCode() == 65507) {
          special = false;
        }
      }

      @Override
      public void nativeKeyPressed(NativeKeyEvent e) {
        Command c = new Command("KeyPress", (Object) e.getRawCode(), "all", GetModulName());
        getCommander().Execute(c);
//        System.out.println("Pres some key:" + e.getRawCode() + " " + e.getKeyCode());
        if (e.getRawCode() == 65506) {
          special = true;
        } else if (e.getRawCode() == 65507) {
          special2 = true;
        } else if (special) {
          switch (e.getRawCode()) {
            case 198:
              getCommander().Execute(new Command("VUp", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 2769:
              getCommander().Execute(new Command("VDown", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 169:
              getCommander().Execute(new Command("Next", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 208:
              getCommander().Execute(new Command("Prew", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 69:
              getCommander().Execute(new Command("ToglePause", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 170:
              getCommander().Execute(new Command("Repeat", "MyPlayerMusic", GetModulName()));
              special = false;
              break;
            case 957:
              int h = LocalDateTime.now().getHour();
              int m = LocalDateTime.now().getMinute();
              getCommander().Execute(new Command("Say", "je " + h + " hodin " + m + " minut.", "Speaker", GetModulName()));
              special = false;
              break;
            case 673:
              String d = LocalDateTime.now().format(DateTimeFormatter.ofPattern("cccc  d.  MMMM  yyyy"));
              getCommander().Execute(new Command("Say", "Dnes je: " + d, "Speaker", GetModulName()));
              special = false;
              break;
//            case 73:
//              getCommander().Execute(new Command("SSong", "MyPlayerMusic", GetModulName()));
//              break;
//            default:
//              getCommander().Execute(new Command("Say", "Tlačítko bez funkce!", "Speaker", GetModulName()));
//              break;
          }
        } else if (special2) {
          switch (e.getRawCode()) {
            case 105:
              getCommander().Execute(new Command("SSong", "MyPlayerMusic", GetModulName()));
              break;
            case 104:
              int h = LocalDateTime.now().getHour();
              int m = LocalDateTime.now().getMinute();
              getCommander().Execute(new Command("Say", "je " + h + " hodin " + m + " minut.", "Speaker", GetModulName()));
              break;
            case 100:
              String d = LocalDateTime.now().format(DateTimeFormatter.ofPattern("cccc  d.  MMMM  yyyy"));
              getCommander().Execute(new Command("Say", "Dnes je: " + d, "Speaker", GetModulName()));
              break;
          }
          special2 = false;
        } else {
          switch (e.getRawCode()) {
            case 65303:
              getCommander().Execute(new Command("Next", "MyPlayerMusic", GetModulName()));
              break;
            case 65302:
              getCommander().Execute(new Command("Prew", "MyPlayerMusic", GetModulName()));
              break;
            case 65300:
              getCommander().Execute(new Command("ToglePause", "MyPlayerMusic", GetModulName()));
              break;
            case 65301:
              getCommander().Execute(new Command("Repeat", "MyPlayerMusic", GetModulName()));
              break;
            case 65297:
              getCommander().Execute(new Command("VDown", "MyPlayerMusic", GetModulName()));
              break;
            case 65299:
              getCommander().Execute(new Command("VUp", "MyPlayerMusic", GetModulName()));
              break;
            default:
//              System.out.println("Pres other key:"+e.getRawCode());
              getCommander().Execute(new Command("OtherKeyPressed", e, getCommander().getActive().GetModulName(), GetModulName()));
              break;
          }
        }
      }
    });
  }
}
