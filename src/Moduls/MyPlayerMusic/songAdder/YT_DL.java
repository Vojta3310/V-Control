/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.songAdder;

import Moduls.MyPlayerMusic.MyPlayerMusic;
import Moduls.MyPlayerMusic.Player.Skladba;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import VControl.UI.components.MyField;
import VControl.UI.components.MyScrollbarUI;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author vojta3310
 */
public class YT_DL {

  private final MyPlayerMusic mpm;
  private final JPanel gr;
  private final JTextPane out;
  private final MyField url;
  private final MyField find;
  private final MyButton download;
  private final MyButton search;

  public YT_DL(MyPlayerMusic mp) {
    this.mpm = mp;
    gr = new JPanel();
    out = new JTextPane();
    url = new MyField();
    find = new MyField();
    download = new MyButton("Stáhnout");
    search = new MyButton("Hledat");

    int rowHeight = AppSettings.getInt("Font_Size") + 20;
    gr.setBackground(AppSettings.getColour("BG_Color"));
    out.setBackground(AppSettings.getColour("BG_Color"));
    JScrollPane sp = new JScrollPane(out);
    sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    sp.setViewportBorder(null);
    sp.setBorder(BorderFactory.createLineBorder(AppSettings.getColour("FG_Color")));
    JScrollBar sb = sp.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());
    out.setForeground(AppSettings.getColour("FG_Color"));
    out.setText("Vložte URL adresu YouTube videa a stiskněte stáhnout ...\n");
    out.setEditable(false);
    url.setText("");
    find.setText("");
    out.addComponentListener(new ComponentListener() {

      @Override
      public void componentResized(ComponentEvent e) {
        sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMaximum());
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }

      @Override
      public void componentShown(ComponentEvent e) {
      }

      @Override
      public void componentHidden(ComponentEvent e) {
      }
    });

    GroupLayout layout = new javax.swing.GroupLayout(gr);
    gr.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(find, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
              .addComponent(url, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(search, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
              .addComponent(download, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
          .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(find))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(url)
          .addComponent(download, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    download.addActionListener((java.awt.event.ActionEvent evt) -> {
      download(url.getText());
    });
    
    search.addActionListener((java.awt.event.ActionEvent evt) -> {
      find(find.getText());
    });
  }

  public final void download(String url) {
    if (url.equals("")) {
      return;
    }
    this.url.setText(url);
    find.setText("");
    downloader a = new downloader("", this.url, out);
    a.start();
  }

  public final void find(String co) {
    find.setText(co);
    this.url.setText("");
    downloader a = new downloader(co, this.url, out);
    a.start();
    find.setText("");
  }

  private class downloader extends Thread {

    MyField url;
    JTextPane out;
    String find;

    downloader(String find, MyField url, JTextPane out) {
      this.url = url;
      this.out = out;
      this.find = find;
    }

    @Override
    public void run() {
      if (url.getText().equals("")) {
        if (find.equals("")) {
          return;
        }
        url.setText(find(find));
        return;
      }

      Skladba a = download(url.getText());
      if (a != null) {
        mpm.addSong(a);
      }
      url.setText("");
    }

    public final String find(String co) {
      String o = "";
      try {
        String f = new File(YT_DL.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        this.out.setText(this.out.getText() + "Run from: " + f + "\n");

        out.setText("Finding ...\n");
        executeCmd("python3 " + f + File.separator
          + "youtube-dl --get-title --get-filename --get-id -o '%(uploader)s'  ytsearch:"
          + co.replaceAll(" ", "_"));
        String[] a = out.getText().split("\n");
        String title = a[2];
        String id = a[3];
        String autor = a[4];

        out.setText(this.out.getText() + "\nFound:\n");
        out.setText(this.out.getText() + "  title: " + title + "\n");
        out.setText(this.out.getText() + "  autor: " + autor + "\n");
        out.setText(this.out.getText() + "  video ID: " + id + "\n");

        o = id;
        out.setText(out.getText() + "\nDone ...\n");
      } catch (URISyntaxException ex) {
        Logger.getLogger(YT_DL.class.getName()).log(Level.SEVERE, null, ex);
      }
      return o;
    }

    private void executeCmd(String cmd) {
      out.setText(out.getText() + cmd + "\n");
      try {
        Process proc = Runtime.getRuntime().exec(cmd);
        InputStream is = proc.getInputStream();
        InputStream es = proc.getErrorStream();
        int i;
        int ie = -1;
        while (((i = is.read()) != -1) || ((ie = es.read()) != -1)) {
          if (i != -1) {
            out.getDocument().insertString(out.getDocument().getLength(), Character.toString((char) i), null );
          }
          if (ie != -1) {
            out.getDocument().insertString(out.getDocument().getLength(), Character.toString((char) ie), null );
          }
          Thread.sleep(3);
        }
        out.setText(out.getText() + "Done ..." + "\n");
      } catch (IOException|InterruptedException | BadLocationException ex) {
        Logger.getLogger(YT_DL.class.getName()).log(Level.SEVERE, null, ex);
        out.setText(out.getText() + "Error ..." + "\n"+ex.toString());
      }
    }

    public final Skladba download(String url) {
      Skladba o = null;
      try {
        String f = new File(YT_DL.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        this.out.setText(this.out.getText() + "Run from: " + f + "\n");

        out.setText("Getting info ...\n");
        executeCmd("python3 " + f + File.separator
          + "youtube-dl --get-title --get-filename -o '%(uploader)s' " + url);
        executeCmd("python3 " + f + File.separator
          + "youtube-dl --get-description " + url);
        String[] a = out.getText().split("\n");
        String title = a[2];
        String autor = a[3];
        String lirics = String.join("\n", Arrays.copyOfRange(a, 6, a.length - 1));

        out.setText(this.out.getText() + "\ntitle:" + title + "\n");
        out.setText(this.out.getText() + "autor:" + autor + "\n");

        out.setText(this.out.getText() + "Downloading ...\n");
        File temp = File.createTempFile("13541563", ".mp4");
        executeCmd("python3 " + f + File.separator + "youtube-dl --no-continue --newline -o "
          + temp.getAbsolutePath() + " " + url);
        File tmpp = File.createTempFile("053105155", ".mp3");
        executeCmd(f + File.separator + "ffmpeg -loglevel 0 -y -i " + temp.getAbsolutePath()
          + " -q:a 0 -map a " + tmpp.getAbsolutePath() + "");
        if (tmpp.length() < 5) {
          out.setText(out.getText() + "File isnt complete!!" + "\n");
          out.setText(out.getText() + "Check url and internet conection and try it again." + "\n");
        } else {
          o = new Skladba(tmpp.getPath());
          o.setLyric(lirics);
          o.setTitle(title);
          o.setAutor(autor);
        }
        out.setText(out.getText() + "\nDone ...\n");
      } catch (URISyntaxException | IOException ex) {
        Logger.getLogger(YT_DL.class.getName()).log(Level.SEVERE, null, ex);
      }
      return o;
    }
  }

  public JPanel getGui() {
    return gr;
  }
}
