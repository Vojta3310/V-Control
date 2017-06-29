/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

/**
 *
 * @author vojta3310
 */
public class MusicOrganiser {
  private final MPgui gui;

  public MusicOrganiser() {
    gui=new MPgui(this);
  }

  public MPgui getGui() {
    return gui;
  }
  
  
}
