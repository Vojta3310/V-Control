/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls;

import VControl.Command;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author vojta3310
 */
public class KeyLoger extends Modul {

  private boolean special = false;

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

    try {
      GlobalScreen.registerNativeHook();
    } catch (NativeHookException ex) {
      Logger.getLogger(KeyLoger.class.getName()).log(Level.SEVERE, null, ex);
    }
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
      }

      @Override
      public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getRawCode() == 165) {
          special = true;
        }
        if (special) {
          switch (e.getRawCode()) {
            case 65:
              getCommander().Execute(new Command("VUp", "MyPlayerMusic", GetModulName()));
              break;
            case 66:
              getCommander().Execute(new Command("VDown", "MyPlayerMusic", GetModulName()));
              break;
            case 67:
              getCommander().Execute(new Command("Next", "MyPlayerMusic", GetModulName()));
              break;
            case 68:
              getCommander().Execute(new Command("Prew", "MyPlayerMusic", GetModulName()));
              break;
            case 69:
              getCommander().Execute(new Command("ToglePause", "MyPlayerMusic", GetModulName()));
              break;
            case 70:
              getCommander().Execute(new Command("Repeat", "MyPlayerMusic", GetModulName()));
              break;
          }
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
            default:
              getCommander().Execute(new Command("KeyPressed", e, getCommander().getActive().GetModulName(), GetModulName()));
              break;
          }
        }
      }
    });

  }

}
