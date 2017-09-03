/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

/**
 *
 * @author vojta3310
 */
public interface IMediaOrganiser {

  public void tooglePause();

  public void Next();

  public void Prew();

  public void Repeat();

  public boolean getPaused();
}
