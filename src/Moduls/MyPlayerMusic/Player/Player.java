/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import java.io.File;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author vojta3310
 */
public class Player {

  private float volume;
  private final MyAudioPlayer Aplayer;
  private final FloatControl gainControl;
  private boolean paused;

  public Player() {
    this.volume = 0.5f;
    this.Aplayer = new MyAudioPlayer();
    this.gainControl = Aplayer.getGain();
  }

  public void PrepareSong(Skladba s) {
    Aplayer.open(new File(s.getPath()));
  }

  public void play() {
    Aplayer.play();
    paused = false;
  }

  public void pause() {
    Aplayer.pause();
    paused = true;
  }

  public long getPos() {
    return Aplayer.getPosition();
  }

  public float getVolume(){
    return volume;
  }
  
  public boolean getPaused(){
    return paused;
  }
  
  public void setVolume(float v) {
    if (v < this.gainControl.getMaximum() && v > this.gainControl.getMinimum()) {
      this.volume = (v);
    } else if (v > this.gainControl.getMaximum()) {
      this.volume = (this.gainControl.getMaximum());
    } else if (v < this.gainControl.getMinimum()) {
      this.volume = (this.gainControl.getMinimum());
    }
    this.setV();
  }

  private void setV() {
    this.gainControl.setValue(volume);
  }

  public void Vup() {
    this.setVolume(this.volume + 0.05f);
    setV();
  }

  public void Vdown() {
    this.setVolume(this.volume - 0.05f);
    setV();
  }

}
