/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import ddf.minim.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author vojta3310
 */
public class Player {

  private float volume;
  private AudioPlayer Aplayer;
  private boolean paused;
  public Minim minim;

  public Player() {
    this.volume = 0.5f;
//    this.Aplayer = new MyAudioPlayer();
    minim = new Minim(this);

  }

  public void PrepareSong(Skladba s) {
    setV();
    Aplayer = minim.loadFile(s.getPath());
  }

  public void play() {
    setV();
    Aplayer.play();
    paused = false;
  }

  public void pause() {
    Aplayer.pause();
    paused = true;
  }

  public long getPos() {
    return Aplayer.position();
  }

  public float getVolume() {
    return volume;
  }

  public boolean getPaused() {
    return paused;
  }

  public void setVolume(float v) {
    volume = v;
    setV();
  }

  private void setV() {
    //todo recalculate volume
    if (Aplayer != null) {
      Aplayer.setGain(volume);
    }
  }

  public String sketchPath(String fileName) {
    return fileName;
  }

  public InputStream createInput(String fileName) throws FileNotFoundException {
    return new FileInputStream(fileName);
  }
}
