/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.IModul;
import Moduls.Modul;
import VControl.ICommand;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Properties;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

/**
 *
 * @author vojta3310
 */
public class MyPlayerVideo extends Modul implements IModul {

  private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

  public MyPlayerVideo(VControl.Commander Commander) {
    super(Commander);

    new NativeDiscovery().discover();

    JPanel a = getMyGrafics();
    mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    mediaPlayerComponent.setPreferredSize(new Dimension(500, 500));
    a.setLayout(new FlowLayout());
    a.add(mediaPlayerComponent);
//    a.add(new JLabel("a"));

  }

  @Override
  public void Activate() {
    super.Activate(); //To change body of generated methods, choose Tools | Templates.
      mediaPlayerComponent.getMediaPlayer().playMedia("/home/vojta3310/Videa/0000-0120.avi");
      mediaPlayerComponent.getMediaPlayer().setRepeat(true);
  }

  @Override
  public void Deactivate() {
    mediaPlayerComponent.getMediaPlayer().stop();
    super.Deactivate(); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean doCommand(ICommand co) {
    return false;
  }

  @Override
  public String GetModulName() {
    return "MyPlayerVideo";
  }

  @Override
  public boolean HaveGUI() {
    return true;
  }

  @Override
  public void Execute(ICommand co) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void getDefaultSettings(Properties p) {
  }

}
