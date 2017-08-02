/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import Moduls.MyPlayerMusic.Player.GUI.zoomButton;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import VControl.utiliti;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author vojta3310
 */
public class WaveEdit extends JPanel {

  private MusicAnalizer ma;
  private final JTextField start;
  private final JTextField end;
  private Long Lstart;
  private Long Lend;
  private final JPanel wave;

  public WaveEdit(MusicAnalizer m) throws IOException {
    ma = m;
    start = new JTextField();//new JSpinner(new SpinnerNumberModel(0L,Long.MIN_VALUE,Long.MAX_VALUE,1L));
    end = new JTextField();//new JSpinner(new SpinnerNumberModel(0L,Long.MIN_VALUE,Long.MAX_VALUE,1L));
    zoomButton zoomIN = new zoomButton("zoomin");
    zoomButton zoomOUT = new zoomButton("zoomout");

    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size") - 3);
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setEnd(ma.getLenght());
    start.setText("0");
    end.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    start.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    end.setForeground(AppSettings.getColour("FG_Color"));
    start.setForeground(AppSettings.getColour("FG_Color"));
    end.setBackground(AppSettings.getColour("BG_Color"));
    start.setBackground(AppSettings.getColour("BG_Color"));
    end.setFont(f);
    start.setFont(f);
    end.setEditable(false);
    start.setEditable(false);
    end.setHorizontalAlignment(SwingConstants.RIGHT);

    wave = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.fillRect(toPixels(Lstart), 0, toPixels(Lend) - toPixels(Lstart), getHeight());
        
        g.setColor(Color.DARK_GRAY);
        int v = (int) (getHeight() * 0.6 * ma.getAverangeVolume() + getHeight() / 2);
        int v2 = getHeight() - v;
        g.drawLine(0, v, getWidth(), v);
        g.drawLine(0, v2, getWidth(), v2);

        g.setColor(Color.BLUE);
        ma.Paint((Graphics2D) g);

        g.setColor(Color.GREEN);
        g.fillRect(toPixels(Lstart), 0, 1, getHeight());
        g.setColor(Color.RED);
        g.fillRect(toPixels(Lend), 0, 1, getHeight());
      }
    };

    wave.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent me) {
        if (me.getButton() == 1) {
          if (toLen(me.getX()) < Lend) {
            setStart(toLen(me.getX()));
          }
        } else if (me.getButton() == 3) {
          if (toLen(me.getX()) > Lstart) {
            setEnd(toLen(me.getX()));
          }
        }
        wave.repaint();
        wave.revalidate();
      }

      @Override
      public void mousePressed(MouseEvent me) {

      }

      @Override
      public void mouseReleased(MouseEvent me) {

      }

      @Override
      public void mouseEntered(MouseEvent me) {

      }

      @Override
      public void mouseExited(MouseEvent me) {

      }
    });

    zoomIN.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae) {
        Dimension a = new Dimension(wave.getSize().width * 2, wave.getSize().height);
        wave.setPreferredSize(a);
        ma.setSize(a);
        try {
          ma.createWaveForm();
        } catch (IOException | UnsupportedAudioFileException ex) {
          Logger.getLogger(WaveEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        wave.repaint();
        wave.revalidate();
      }
    });

    zoomOUT.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae) {
        Dimension a = new Dimension(wave.getSize().width / 2, wave.getSize().height);
        wave.setPreferredSize(a);
        ma.setSize(a);
        try {
          ma.createWaveForm();
        } catch (IOException | UnsupportedAudioFileException ex) {
          Logger.getLogger(WaveEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        wave.repaint();
        wave.revalidate();
      }
    });

    ma.setSize(wave.getSize());

    JScrollPane jScrollPane1 = new JScrollPane(wave);
    jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(AppSettings.getColour("FG_Color"), 2));
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    JScrollBar sb = jScrollPane1.getHorizontalScrollBar();
    sb.setPreferredSize(new Dimension(Integer.MAX_VALUE, AppSettings.getInt("Border_Size")));
    sb.setUI(new MyScrollbarUI());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(start, 80, 80, 80)
            .addComponent(zoomIN)
            .addComponent(zoomOUT)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(end, 80,80,80)))
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(zoomIN)
          .addComponent(zoomOUT)
          .addComponent(end, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, 10, javax.swing.GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
      )
    );
  }

  private int toPixels(long len) {
    return (int) ((((float) len / (float) ma.getLenght())) * wave.getWidth());
  }

  private long toLen(int pixels) {
    return (long) (((float) pixels / (float) wave.getWidth()) * ma.getLenght());
  }

  public MusicAnalizer getMa() {
    return ma;
  }

  public void setMa(MusicAnalizer m) throws IOException, UnsupportedAudioFileException {
    this.ma = m;
    ma.setSize(wave.getSize());
    ma.createWaveForm();
    this.setEnd(ma.getLenght());
    wave.repaint();
    wave.revalidate();
  }

  public long getStart() {
    return Lstart;
  }

  public void setStart(long start) {
    Lstart = start;
    this.start.setText(utiliti.MilToTime(start));
  }

  public long getEnd() {
    return Lend;
  }

  public final void setEnd(Long end) {
    Lend = end;
    this.end.setText(utiliti.MilToTime(end));
  }
}
