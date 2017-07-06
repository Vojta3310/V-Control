/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.MyPlayerMusic.Player.RandomSong;

/**
 *
 * @author vojta3310
 */
public class AddSongSign extends RandomSong{

  public AddSongSign(Songs s) {
    super(s);
  }

  @Override
  public String getLabel() {
    return "...";
  }
  
  @Override
  public boolean isEmty(){
    return false;
  }
}
