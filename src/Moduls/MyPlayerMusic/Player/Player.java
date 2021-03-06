/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import Moduls.Modul;
import ddf.minim.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vojta3310
 */
public class Player {

  private float volume;
  private AudioPlayer Aplayer;
  private boolean paused;
  public Minim minim;
  private boolean onStart = false;
  private Skladba s;
  private float statMinVol;
  private float statMaxVol;
  private final float Strenght;
  private final String command;

  public Player(Modul mod) {
    this.volume = 0.5f;
    minim = new Minim(this);
    minim.debugOff();
    command = mod.SgetString("Volume_Command");
    Strenght = Float.parseFloat(mod.SgetString("Loudness_Controll_Strenght"));
  }

  public AudioPlayer PrepareSong(Skladba s) {
    this.s = s;
    if (Aplayer != null) {
      Aplayer.close();
    }
    onStart = true;
    Aplayer = minim.loadFile(s.getPath());
    setV();
    return Aplayer;
  }

  public void play() {
    setV();
    Aplayer.play();
    if (onStart) {
      Aplayer.cue((int) s.getStart());
      onStart = false;
    }
    paused = false;
  }

  public void pause() {
    Aplayer.pause();
    paused = true;
  }

  public long getPos() {
    return Aplayer.position();
  }

  public void setPos(long p) {
    Aplayer.cue((int) p);
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

  public boolean finished() {
    return (!paused && !Aplayer.isPlaying()) || (Aplayer.position() >= (s.getLenght() + s.getStart()));
  }

  private void setV() {
    float v = ((1 / s.getVolume()) - 1 / statMaxVol) / (1 / statMinVol - 1 / statMaxVol) * Strenght + (1 - Strenght);
    switch (command) {
      case "V":
        Aplayer.setVolume(volume * v);
        break;
      case "G":
        Aplayer.setGain((float) (v * volume * 100) - 50);
        break;
      default:
        String vol = Integer.toString((int) ((float)v * (float)volume * (float)100));
        try {
          Runtime.getRuntime().exec(command.replace("%v%", vol));
        } catch (IOException ex) {
          Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;
    }
  }

  public String sketchPath(String fileName) {
    return fileName;
  }

  public InputStream createInput(String fileName) throws FileNotFoundException {
    return new FileInputStream(fileName);
  }

  public void setStatMinVol(float statMinVol) {
    this.statMinVol = statMinVol;
  }

  public void setStatMaxVol(float statMaxVol) {
    this.statMaxVol = statMaxVol;
  }
}
