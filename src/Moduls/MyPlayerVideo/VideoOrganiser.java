/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import java.awt.Dimension;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 *
 * @author vojta3310
 */
public class VideoOrganiser implements IMediaOrganiser {

  private final EmbeddedMediaPlayerComponent PlayerComp;

  public VideoOrganiser() {

    new NativeDiscovery().discover();

    PlayerComp = new EmbeddedMediaPlayerComponent();
  }

  public EmbeddedMediaPlayerComponent getPlayerComp() {
    return PlayerComp;
  }

  @Override
  public void tooglePause() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void Next() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void Prew() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void Repeat() {
    PlayerComp.getMediaPlayer().setRepeat(!PlayerComp.getMediaPlayer().getRepeat());
  }

  @Override
  public boolean getPaused() {
    return !PlayerComp.getMediaPlayer().isPlaying();
  }

}
