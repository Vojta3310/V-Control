/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic;

import VControl.Settings.AppSettings;
import java.awt.Graphics;
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
  private final javax.swing.JLabel StateLabel;
  private final javax.swing.JLabel SizeLabel;
  private final javax.swing.JSlider Slider;

  public PlayerPanel(MusicOrganiser m) {
    StateLabel = new javax.swing.JLabel();
    SizeLabel = new javax.swing.JLabel();
    Slider = new javax.swing.JSlider();
    prew = new PlayerButon(ButonTipe.prew, m);
    play = new PlayerButon(ButonTipe.pause, m);
    next = new PlayerButon(ButonTipe.next, m);
    this.setBackground(AppSettings.getColour("BG_Color"));
    Slider.setBackground(AppSettings.getColour("BG_Color"));
    StateLabel.setForeground(AppSettings.getColour("FG_Color"));
    SizeLabel.setForeground(AppSettings.getColour("FG_Color"));

    StateLabel.setText("00:00");
    SizeLabel.setText("00:22");

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
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(Slider, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(StateLabel)
          .addComponent(SizeLabel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(prew)
          .addComponent(play)
          .addComponent(next))
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
}
