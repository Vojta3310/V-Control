/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import VControl.Settings.AppSettings;
import VControl.UI.components.MySliderUI;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 *
 * @author vojta3310
 */
public class PlayerPanel extends JPanel {

  private final PlayerButon prew;
  private final PlayerButon play;
  private final PlayerButon next;
  private final PlayerButon rep;
  private final javax.swing.JLabel StateLabel;
  private final javax.swing.JLabel SizeLabel;
  private final javax.swing.JSlider Slider;
  private final VolumeControl Vcontrol;
  private boolean resume = false;

  public PlayerPanel(final MusicOrganiser m) {
    StateLabel = new javax.swing.JLabel();
    SizeLabel = new javax.swing.JLabel();
    Slider = new javax.swing.JSlider();
    prew = new PlayerButon(ButonTipe.prew, m);
    play = new PlayerButon(ButonTipe.play_pause, m);
    next = new PlayerButon(ButonTipe.next, m);
    rep = new PlayerButon(ButonTipe.repeat, m);
    Vcontrol = new VolumeControl();
    this.setBackground(AppSettings.getColour("BG_Color"));
    Slider.setBackground(AppSettings.getColour("BG_Color"));
    StateLabel.setForeground(AppSettings.getColour("FG_Color"));
    SizeLabel.setForeground(AppSettings.getColour("FG_Color"));

    Font font = new Font(AppSettings.getString("Font_Name"), 0, AppSettings.getInt("Font_Size"));
    StateLabel.setFont(font);
    SizeLabel.setFont(font);

    StateLabel.setText("00:00");
    SizeLabel.setText("00:22");

    Slider.setUI(new MySliderUI(Slider));

    Slider.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        resume = false;
        if (!m.getAplayer().getPaused()) {
          resume = true;
          m.getAplayer().pause();
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        m.getAplayer().play();
        m.getAplayer().setPos(m.getPlaying().getStart() + (long) ((float) ((float) Slider.getValue() / (float) Slider.getMaximum()) * m.getPlaying().getLenght()));
        if (!resume) {
          m.getAplayer().pause();
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Slider)
          .addGroup(layout.createSequentialGroup()
            .addComponent(StateLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(SizeLabel))
          .addGroup(layout.createSequentialGroup()
            .addComponent(prew)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(play)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(next)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(rep)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1000, Short.MAX_VALUE)
            .addComponent(Vcontrol, 42, 42, 42)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(20, 40)
        .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(StateLabel)
          .addComponent(SizeLabel))
        .addGap(10, 10, 10)//PreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(prew)
          .addComponent(play)
          .addComponent(next)
          .addComponent(rep)
          .addComponent(Vcontrol)
        )
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRect(0, 0, getWidth(), AppSettings.getInt("Border_Size"));
  }

  public JLabel getStateLabel() {
    return StateLabel;
  }

  public JLabel getSizeLabel() {
    return SizeLabel;
  }

  public JSlider getSlider() {
    return Slider;
  }

  public VolumeControl getVcontrol() {
    return Vcontrol;
  }
  
}
