/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerVideo;

import Moduls.MyPlayerMusic.Player.GUI.IMediaOrganiser;
import Moduls.MyPlayerMusic.Player.GUI.PlayerPanel;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import VControl.UI.components.MyCellRenderer;
import VControl.UI.components.MyComboUI;
import VControl.UI.components.MyField;
import VControl.UI.components.MyScrollbarUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author vojta3310
 */
public class GUI extends JPanel {

  private final Component video;
  private final JList List;
  private final PlayerPanel ppanel;
  private final JPanel a = new JPanel();
  private final MyField searchField = new MyField();
  private final JComboBox Epizode = new JComboBox<>(new String[]{"---"});
  private final MyButton pl;
  private boolean serial;

  private final JPanel serialPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
      g.setColor(AppSettings.getColour("FG_Color"));
//      g.fillRect(getWidth() - AppSettings.getInt("Border_Size"), 0,
//        AppSettings.getInt("Border_Size"), getHeight());
      g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"),
        getWidth(), AppSettings.getInt("Border_Size"));
    }
  };
  private final JPanel searchPanel = new JPanel() {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
      g.setColor(AppSettings.getColour("FG_Color"));
      g.fillRect(getWidth() - AppSettings.getInt("Border_Size"), 0,
        AppSettings.getInt("Border_Size"), getHeight());
      g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"),
        getWidth(), AppSettings.getInt("Border_Size"));
    }
  };

  public GUI(IMediaOrganiser o, JPanel out, Component v) {
    video = v;
    List = new JList();
    ppanel = new PlayerPanel(o);
    JPanel c = new JPanel();
    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));
    JLabel e = new JLabel("Epizoda:");
    pl = new MyButton("Přehrát ...");

    List.setBackground(AppSettings.getColour("BG_Color"));
    serialPanel.setBackground(AppSettings.getColour("BG_Color"));
    Epizode.setBackground(AppSettings.getColour("BG_Color"));
    Epizode.setForeground(AppSettings.getColour("FG_Color"));
    e.setForeground(AppSettings.getColour("FG_Color"));
    Epizode.setFont(f);
    e.setFont(f);
    
    

    List.setCellRenderer(new MyCellRenderer(List));
//    List.setPreferredSize(new Dimension(200, 350));
    JScrollPane b = new JScrollPane(List);
    b.setMaximumSize(new Dimension(350, 500));
    b.setViewportBorder(null);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setBorder(createEmptyBorder());
    JScrollBar sb = b.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());
    JScrollBar sb2 = b.getHorizontalScrollBar();
    sb2.setPreferredSize(new Dimension(Integer.MAX_VALUE, AppSettings.getInt("Border_Size")));
    sb2.setUI(new MyScrollbarUI());

    MyComboUI cui=new MyComboUI();
    Epizode.setUI(cui);
//    cp.getCp().setPopupSize(500, 200);
    cui.getCp().setMaximumSize(new Dimension(1000, 800));
    cui.getCp().setMinimumSize(new Dimension(200, 5));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(serialPanel);
    serialPanel.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(e, 100, 100, 100)
//          .addComponent(pl)
        )
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//          .addComponent(pl, 200, 200, 200)
          .addComponent(Epizode, 200, 200, 200))
        .addContainerGap()
      )
        .addComponent(pl,330,330,330)
      
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(e)
          .addComponent(Epizode, 36, 36, 36))
        .addContainerGap(5, 5)
        .addComponent(pl, 48, 48, 48)
        .addContainerGap(5, 5))
//        .addContainerGap()
    );

    a.setBorder(BorderFactory.createEmptyBorder());
    a.setLayout(new BorderLayout());
    a.add(ppanel, BorderLayout.PAGE_END);
    a.add(video, BorderLayout.CENTER);
    a.setPreferredSize(new Dimension(AppSettings.getInt("Window_Width")
      - (AppSettings.getInt("Icon_Size") + 20 + 2 * AppSettings.getInt("Border_Size")) - 350,
      AppSettings.getInt("Icon_Size") + 20));

    searchField.setPreferredSize(new Dimension(330, 36));
    searchField.setMaximumSize(new Dimension(330, 36));
    searchPanel.setBorder(BorderFactory.createEmptyBorder());
    searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
    searchPanel.add(new Box.Filler(new Dimension(200, 5), new Dimension(200, 5), new Dimension(200, 5)));
    searchPanel.add(searchField);
    searchPanel.add(new Box.Filler(new Dimension(200, 15), new Dimension(200, 20), new Dimension(200, 25)));

    searchPanel.setBackground(AppSettings.getColour("BG_Color"));
    serialPanel.setMaximumSize(new Dimension(330, 500));

    c.setBorder(BorderFactory.createEmptyBorder());
    c.setLayout(new BorderLayout());
    c.add(searchPanel, BorderLayout.PAGE_START);
    c.add(b, BorderLayout.CENTER);
    out.setLayout(new BorderLayout());
    out.add(c, BorderLayout.LINE_START);
    out.add(a, BorderLayout.CENTER);
    out.setBackground(AppSettings.getColour("BG_Color"));

    List.setCellRenderer(new MyCellRenderer(List));

  }

  public void hideVideo() {
    a.remove(video);
  }

  public void showVideo() {
    a.add(video, BorderLayout.CENTER);
    a.revalidate();
  }

  public JList getList() {
    return List;
  }

  public PlayerPanel getPpanel() {
    return ppanel;
  }

  public MyField getSearchField() {
    return searchField;
  }

  public JComboBox getEpizode() {
    return Epizode;
  }

  public MyButton getPlayButton() {
    return pl;
  }

  public void showSerial() {
    if (!serial) {
      searchPanel.add(serialPanel);
//      searchPanel.add(new Box.Filler(new Dimension(200, 15), new Dimension(200, 20), new Dimension(200, 25)));
      serial = true;
    }
  }

  public void hideSerial() {
    if (serial) {
//      searchPanel.remove(new Box.Filler(new Dimension(200, 15), new Dimension(200, 20), new Dimension(200, 25)));
      searchPanel.remove(serialPanel);
      serial = false;
    }
  }
}
