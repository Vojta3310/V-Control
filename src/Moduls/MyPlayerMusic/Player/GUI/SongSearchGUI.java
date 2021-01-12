/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.RandomSong;
import Moduls.MyPlayerMusic.Player.Skladba;
import Moduls.MyPlayerMusic.Player.Songs;
import Moduls.MyPlayerVideo.SearchFieldListener;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author vojta3310
 */
public class SongSearchGUI extends JFrame {

  private final Songs songs;
  private final JTextField search;
  private final JList list;
  private final SearchFieldListener sl;
  private boolean first = true;
  private final JList Rsongs;

  public SongSearchGUI(Songs songs, JList random) throws HeadlessException {
    this.songs = songs;
    Rsongs = random;
    search = new JTextField();
    list = new JList();
    sl = new SearchFieldListener(list);

    search.getDocument().addDocumentListener(sl);
    setSize(new Dimension(500, 500));

    JScrollPane b = new JScrollPane(list);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setViewportBorder(null);
    b.setBorder(createEmptyBorder());
    JScrollBar sb = b.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());

    Container pane = this.getContentPane();
    pane.setLayout(new BorderLayout());
    pane.add(search, BorderLayout.PAGE_START);
    pane.add(b, BorderLayout.CENTER);

    list.setDragEnabled(true);
    list.setDropMode(DropMode.INSERT);
    list.setTransferHandler(new SlistTransferHandler(songs));
    setTitle("Search for song ...");
    list.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
          addToRS();
        }
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

    search.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent ke) {
      }

      @Override
      public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
          list.setSelectedIndex(0);
          addToRS();
        }
      }

      @Override
      public void keyReleased(KeyEvent ke) {
      }
    });
  }

  @Override
  public void setVisible(boolean bln) {
    super.setVisible(bln); //To change body of generated methods, choose Tools | Templates.
    if (first) {
      DefaultListModel lm = new DefaultListModel();
      list.setModel(lm);
      songs.getSongs().forEach((s) -> {
        lm.addElement(s);
      });
      sl.setModel(lm);
      first = false;
    }
    search.setText("");
  }

  private void addToRS() {
    Rsongs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    RandomSong rs = new RandomSong(songs);
    rs.setSong((Skladba) list.getModel().getElementAt(list.getSelectedIndex()));
    ((DefaultListModel) Rsongs.getModel()).add(Rsongs.getModel().getSize() - 1, rs);
    Rsongs.setSelectedIndex(Rsongs.getModel().getSize() - 2);
    Rsongs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

}
