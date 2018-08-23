/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.ScreenSaver;

import Moduls.Modul;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Command;
import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author vojta3310
 */
public class SongPanel extends JPanel {

  private final JLabel Name;
  private final JLabel Autor;
  private float pos = 1;
  private final Modul m;
  private Command song;
  private Command posCmd;

  public SongPanel(Modul m) {
    this.m = m;
    Name = new JLabel("Song");
    Autor = new JLabel("Autor");
    song = new Command("Sget", "MyPlayerMusic", m.GetModulName());
    posCmd = new Command("Posget", "MyPlayerMusic", m.GetModulName());
    Name.setForeground(Color.white);
    Autor.setForeground(Color.white);
    Font f = new Font(AppSettings.getString("Font_Name"), 0, (int) 2 * AppSettings.getInt("Font_Size"));
    Name.setFont(f.deriveFont(1, (float) (3 * AppSettings.getInt("Font_Size"))));
    Autor.setFont(f);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(Name))
          .addComponent(Autor))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(Name))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(Autor)
        .addContainerGap(18, Short.MAX_VALUE))
    );
    Timer tim = new Timer(101, (ActionEvent ae) -> {
      update();
    }
    );
    tim.start();
  }

  private void update() {
    if (song.isDone()) {
      Skladba s = (Skladba) song.getResults();
      String n = s.getTitle();
      if (n.length() > 22) {
        n = n.substring(0, 20) + "...";
      }
      Name.setText(n);
      Autor.setText(s.getAutor());
    }
    if (posCmd.isDone()) {
      pos = (float) posCmd.getResults();
    }
    posCmd = new Command("Posget", "MyPlayerMusic", m.GetModulName());
    m.getCommander().Execute(posCmd);
    song = new Command("Sget", "MyPlayerMusic", m.GetModulName());
    m.getCommander().Execute(song);

    getParent().revalidate();
    getParent().repaint();

  }

  @Override
  protected void paintComponent(Graphics g) {
//    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    g.setColor(new Color(0, 0, 0, 127));
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.GRAY);
    g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"), getWidth(), AppSettings.getInt("Border_Size"));
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"), (int) (getWidth() * pos), AppSettings.getInt("Border_Size"));

  }
}
