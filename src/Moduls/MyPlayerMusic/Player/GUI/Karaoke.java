/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.Player;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import VControl.UI.components.VolumeControl;
import VControl.utiliti;
import ddf.minim.AudioInput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author vojta3310
 */
public class Karaoke extends JPanel {

  private final JLabel Title = new JLabel("", SwingConstants.CENTER);
  private final JLabel Autor = new JLabel("", SwingConstants.CENTER);
  private final VolumeControl Volume = new VolumeControl();
  private final SimpleAttributeSet center = new SimpleAttributeSet();
  private final JScrollBar sb;
  private int ofset = 0;
  private boolean move = false;
  private final int micWidth = 122;
  private final AudioInput in;
  private float lastL;
  private float lastR;
  private float volume = 0;
  private float maxVolume = 0;
  private String volumeCmd = "";

  private AudioPlayer pl;
  private FFT fftPl;
  private FFT fftIn;

  float smoothing = 0.80F;
  private float[] fftSmoothPl;
  private float[] fftSmoothIn;
  private int avgSize;

  private float minVal = 0.0F;
  private float maxVal = 0.0F;

  private long start = 0;
  private long len = 0;

  private BufferedImage image = null;

  private final JTextPane Text = new JTextPane() {

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
      int a = 64;

      Rectangle r = getVisibleRect();
      g.setColor(Color.red);
      for (int i = 0; i < a; i++) {
        g.setColor(new Color(0, 0, 0, 255 - Math.round(i * 255 / a)));
        g.fillRect(r.x, r.y + i, r.width, 1);
        g.fillRect(r.x, r.y + r.height - i, r.width, 1);
      }
    }
  };

  public Karaoke() {
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.setForeground(AppSettings.getColour("FG_Color"));

    Font font = new Font(AppSettings.getString("Font_Name"), 0, (int) AppSettings.getInt("Font_Size"));
    Title.setFont(font.deriveFont(1, 3 * AppSettings.getInt("Font_Size")));
    Autor.setFont(font);
    Text.setFont(font.deriveFont(1, 2 * AppSettings.getInt("Font_Size")));

    Title.setForeground(AppSettings.getColour("FG_Color"));
    Autor.setForeground(AppSettings.getColour("FG_Color"));
    Text.setForeground(AppSettings.getColour("FG_Color"));
    Text.setBackground(AppSettings.getColour("BG_Color"));

    StyledDocument docs = Text.getStyledDocument();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    docs.setParagraphAttributes(0, docs.getLength(), center, false);
    Text.setEditable(false);

    Title.setBorder(BorderFactory.createEmptyBorder());
    Autor.setBorder(BorderFactory.createEmptyBorder());
    Text.setBorder(BorderFactory.createEmptyBorder());

    JPanel noWrapPanel = new JPanel(new BorderLayout());
    noWrapPanel.add(Text);

    JScrollPane sp = new JScrollPane(noWrapPanel);
    sp.setViewportView(Text);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    sp.setViewportBorder(null);
    sp.setBorder(BorderFactory.createEmptyBorder());
    sp.addMouseWheelListener((MouseWheelEvent e) -> {
      ofset += e.getWheelRotation() * 6;
    });
    sp.setWheelScrollingEnabled(false);
    sb = sp.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI() {
      @Override
      protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(AppSettings.getColour("BG_Color"));
        g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(Color.darkGray);
        g.fillRoundRect(r.x, r.y, r.width, r.height, (r.width - 2), (r.width - 2));
        g.setColor(Color.red);
      }

      @Override
      public void paint(Graphics g, JComponent c) {
        super.paint(g, c); //To change body of generated methods, choose Tools | Templates.
        int a = 64;
        for (int i = 0; i < a; i++) {
          g.setColor(new Color(0, 0, 0, 255 - Math.round(i * 255 / a)));
          g.fillRect(0, i, c.getWidth(), 1);
          g.fillRect(0, c.getHeight() - i, c.getWidth(), 1);
        }
      }

    });
    sb.setMaximum(Integer.MAX_VALUE);
    sb.setMinimum(0);
    sb.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        move = true;
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        ofset = sb.getValue() - (int) (((pl.position() - start) / (float) len) * sb.getMaximum());
        move = false;
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    int liricGap = 64;

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
          .addComponent(Autor, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
        )
        .addGap(16 + micWidth, 16 + micWidth, 16 + micWidth)
      )
      .addGroup(layout.createSequentialGroup()
        .addGap(liricGap, liricGap, liricGap)
        .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        .addGap(liricGap, liricGap, liricGap)
        .addGap((micWidth - 42) / 2, (micWidth - 42) / 2, (micWidth - 42) / 2)
        .addComponent(Volume, 42, 42, 42)
        .addGap((micWidth - 42) / 2, (micWidth - 42) / 2, (micWidth - 42) / 2)
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(16, 16, 16)
        .addComponent(Title)
        .addGap(8, 8, 8)
        .addComponent(Autor)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(16, 16, 16)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
            .addGap(16, 16, 16)
          )
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Volume, 48, 48, 48)
            .addGap(16, 16, 16)
          )
        )
      )
    );

    Timer tim = new Timer(31, (ActionEvent ae) -> {
      if (len != 0l && pl != null && !move) {
        sb.setValue(ofset + (int) (((pl.position() - start) / (float) len) * sb.getMaximum()));
        updateVolume();
      }
    });
    tim.start();

    Minim minim;
    minim = new Minim(this);
    in = minim.getLineIn();

    try {
      image = utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/MyPlayerMusic/karaoke.png")));
      if (AppSettings.getBool("Icon_Chanhe_Color")) {
        utiliti.changeColor(image, AppSettings.getColour("BG_Color"),
          AppSettings.getColour("FG_Color"));
      }
      image = utiliti.resize(image, 101, 101);
    } catch (IOException ex) {
      Logger.getLogger(Karaoke.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRect(getWidth() - micWidth - AppSettings.getInt("Border_Size") / 2, 0,
      AppSettings.getInt("Border_Size"), getHeight());

    g.drawImage(image, getWidth() - 8 - 101, 8, null);

    g.setColor(Color.lightGray);
    g.fillRect(getWidth() - micWidth / 2 - 32, 117, 28, getHeight() - 128 - 64);
    g.fillRect(getWidth() - micWidth / 2 + 4, 117, 28, getHeight() - 128 - 64);

    int m = getHeight() - 128 - 64;

    lastL = in.left.level() * 0.6f + lastL * 0.4f;
    for (int i = 0; i <= Math.round(lastL * m); i++) {
      int y = 117 + m - i;
      g.setColor(level(m, i));
      g.fillRect(getWidth() - micWidth / 2 - 32, y, 28, 1);
    }
    lastR = in.right.level() * 0.6f + lastR * 0.4f;
    for (int i = 0; i <= Math.round(lastR * m); i++) {
      int y = 117 + m - i;
      g.setColor(level(m, i));
      g.fillRect(getWidth() - micWidth / 2 + 4, y, 28, 1);
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    if (pl != null) {
      fftPl.forward(pl.mix);
      fftIn.forward(in.mix);
      int height = Math.round(getHeight() / (avgSize - 1)) + 1;
      int gap = Math.abs(getHeight() - height * (avgSize - 1)) / 2;
      float maxHeight = 32;

      for (int i = 0; i < avgSize; i++) {
        float fftCurr;
        fftCurr = fftPl.getAvg(i);
        fftSmoothPl[i] = (smoothing) * fftSmoothPl[i] + ((1 - smoothing) * fftCurr);
        if (fftSmoothPl[i] > maxVal) {
          maxVal = fftSmoothPl[i];
        }
        if (fftSmoothPl[i] < minVal) {
          minVal = fftSmoothPl[i];
        }
      }
      for (int i = 0; i < avgSize; i++) {
        float fftCurr;
        fftCurr = fftIn.getAvg(i);
        fftSmoothIn[i] = (smoothing) * fftSmoothIn[i] + ((1 - smoothing) * fftCurr);
        if (fftSmoothIn[i] > maxVal) {
          maxVal = fftSmoothIn[i];
        }
        if (fftSmoothIn[i] < minVal) {
          minVal = fftSmoothIn[i];
        }
      }

      float range = maxVal - minVal;
      float scaleFactor = range + 0.00001F; // avoid div. by zero

      for (int i = 0; i < avgSize - 2; i++) {
        int fftSmoothDisplayPl = (int) (maxHeight * ((fftSmoothPl[i] - minVal) / scaleFactor));
        int fftSmoothDisplayIn = (int) (maxHeight * ((fftSmoothIn[i] - minVal) / scaleFactor));
        g.setColor(makeColor(i, avgSize));

        g.fillRoundRect(1, getHeight() - i * height, fftSmoothDisplayPl + 8, height - 1, (height - 2), (height - 2));
        g.fillRoundRect(getWidth() - micWidth - AppSettings.getInt("Border_Size") / 2 - 1 - (fftSmoothDisplayIn + 8),
          getHeight() - i * height, fftSmoothDisplayIn + 8, height - 1, (height - 2), (height - 2));
      }
    }

  }

  private Color level(int max, int actual) {
    float p = 0.3f;
    float a = (float) actual / (float) max;
    float r = (float) Math.max(0, Math.min(1, (a - 0.5 + p) / p));
    float g = (float) Math.max(0, Math.min(1, 1 - (a - 0.75) / p));
    float b = 0;
    return new Color(r, g, b);
  }

  public void setupEqualizer(AudioPlayer ap) {
    pl = ap;
    fftPl = new FFT(pl.bufferSize(), pl.sampleRate());
    fftPl.logAverages(200, 10);

    fftIn = new FFT(in.bufferSize(), in.sampleRate());
    fftIn.logAverages(200, 10);

    avgSize = fftPl.avgSize();
    fftSmoothPl = new float[avgSize];
    fftSmoothIn = new float[avgSize];
  }

  private Color makeColor(int i, int imax) {
    int a = (int) ((float) i / imax * 1530);

    int r = 255 - Math.max(Math.min(a - 255, 255), 0) + Math.max(Math.min(a - 1020, 255), 0);
    int g = Math.max(Math.min(a - 512, 255), 0) - Math.max(Math.min(a - 1275, 255), 0);
    int b = Math.min(a, 255) - Math.max(Math.min(a - 765, 255), 0);

    return new Color(r, g, b);
  }

  public void ShowSong(Skladba s) {
    int w = Title.getWidth();
    String txt = s.getTitle();
    if (Title.getGraphics() != null) {
      final FontMetrics fm = Title.getGraphics().getFontMetrics();
      while (fm.stringWidth(txt) > w) {
        txt = txt.substring(0, txt.length() - 5) + " ...";
      }
      System.out.println(txt);
    } else if (txt.length() > 19) {
      txt = txt.substring(0, 20) + " ...";
    }
    Title.setText(txt);

    Autor.setText(s.getAutor());
    Text.setText("\n\n\n" + s.getLyric() + "\n\n\n");

    start = s.getStart();
    len = s.getLenght();
    ofset = 0;

    minVal = 0.0F;
    maxVal = 0.0F;
  }

  private void updateVolume() {
    if (!volumeCmd.equals("") && volume != Volume.getVolume()) {
      String vol = Integer.toString((int) (Volume.getVolume() * maxVolume));
      try {
        Runtime.getRuntime().exec(volumeCmd.replace("%v%", vol));
      } catch (IOException ex) {
        Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
      }
      volume = Volume.getVolume();
    }
  }

  public void setVolumeCmd(String volumeCmd) {
    this.volumeCmd = volumeCmd;
  }

  public void setMaxVolume(float maxVolume) {
    this.maxVolume = maxVolume;
  }
}
