/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik.ui.ModulUI;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.DataStructure.ModelService;
import Moduls.Skladnik.Enums.typOperace;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.skladnik.Robot;
import Moduls.Skladnik.ui.graphics.CellRenderers.BufferCellRenderer;
import Moduls.Skladnik.ui.graphics.IGUI;
import Moduls.Skladnik.ui.graphics.listeners.SearchFieldListener;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import VControl.UI.components.MyField;
import VControl.UI.components.MyScrollbarUI;
import VControl.UI.components.MyTreeCellRenderer;
import com.sun.java.swing.plaf.motif.MotifTreeUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;

/**
 *
 * @author vojta3310
 */
public class MGUI implements IGUI {

  private final ISklad sklad;
  private final ModelService vyndane;
  private final Robot rob;
  private final XML xml;
  private final JPanel MyGui;
  private final JTree tree;
  private final JList<Box> list;
  private final JList<Box> bufferList;
  private final JTextField hledatField;
  private final SearchFieldListener listListener;
  private typOperace kam = typOperace.PODEJ;
  private final MyButton ok;
  private final BoxEditor editor;
  private final JScrollPane ListPanel;
  private boolean edit = false;

  public MGUI(ISklad sklad, Robot rob, XML xml, JPanel MyGui) {
    this.sklad = sklad;
    this.vyndane = sklad.getVyndane();
    this.rob = rob;
    this.xml = xml;
    this.MyGui = MyGui;

    tree = new JTree();
    list = new JList<>();
    bufferList = new JList<>();
    hledatField = new MyField();
    ok = new MyButton("Podat");

    bufferList.setModel(rob.getBufferListModel());
    list.setModel(sklad.getModel());

    tree.setModel(this.sklad.getTreeModel());
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }
    tree.setSelectionRow(0);
    //nastavi filtrovani obsahu listu
    listListener = new SearchFieldListener(list, tree, sklad.getKategorie());
    editor = new BoxEditor(tree, sklad, xml);
    hledatField.getDocument().addDocumentListener(listListener);
    hledatField.getDocument().addDocumentListener(editor.getListListener());

    //nastavi aktualizovani listu pri zmene v bufferu
//    bufferList.getModel().addListDataListener(new BufferListener(list, list));
    rob.setGui(this);

    //nastavení vzhledu komponent
    //----------------------------------------------------------------------------
    tree.setBackground(AppSettings.getColour("BG_Color"));
    tree.setForeground(AppSettings.getColour("FG_Color"));
    tree.setCellRenderer(new MyTreeCellRenderer());
    tree.setUI(new MotifTreeUI());
    tree.setMaximumSize(new Dimension(250, 5000));
    JScrollPane a = new JScrollPane(tree);
    a.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    a.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    a.setViewportBorder(null);
    a.setMaximumSize(new Dimension(250, 5000));
    a.setPreferredSize(new Dimension(250, 5000));
    a.setBorder(createEmptyBorder());
    JScrollBar sb = a.getVerticalScrollBar();
    sb.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb.setUI(new MyScrollbarUI());

    list.setBackground(AppSettings.getColour("BG_Color"));
    list.setForeground(AppSettings.getColour("FG_Color"));
    list.setCellRenderer(new Moduls.Skladnik.ui.graphics.CellRenderers.MyCellRenderer(list));
    list.setFixedCellHeight(80);
    list.setFixedCellWidth(230);
    list.setVisibleRowCount(-1);
    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    ListPanel = new JScrollPane(list);
    ListPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    ListPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    ListPanel.setViewportBorder(null);
    ListPanel.setBorder(createEmptyBorder());
    JScrollBar sb2 = ListPanel.getVerticalScrollBar();
    sb2.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb2.setUI(new MyScrollbarUI());

    bufferList.setBackground(AppSettings.getColour("BG_Color"));
    bufferList.setForeground(AppSettings.getColour("FG_Color"));
    bufferList.setCellRenderer(new BufferCellRenderer(bufferList, this));
    bufferList.setFixedCellHeight(80);
    bufferList.setFixedCellWidth(230);
    bufferList.setVisibleRowCount(-1);
    JScrollPane c = new JScrollPane(bufferList);
    c.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    c.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    c.setViewportBorder(null);
    c.setBorder(createEmptyBorder());
    JScrollBar sb3 = c.getVerticalScrollBar();
    sb3.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb3.setUI(new MyScrollbarUI());

    JLabel lab = new JLabel("Hledat:");
    lab.setForeground(AppSettings.getColour("FG_Color"));
    lab.setFont(new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size")));
    hledatField.setPreferredSize(new Dimension(300, 24));
    JPanel d = new JPanel() {

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.setColor(AppSettings.getColour("FG_Color"));
        g.fillRect(0, getHeight() - AppSettings.getInt("Border_Size"), getWidth(), AppSettings.getInt("Border_Size"));
      }

    };
    d.setBackground(AppSettings.getColour("BG_Color"));
//    d.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(d);
    d.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(32, 32)
        .addComponent(lab)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(hledatField, javax.swing.GroupLayout.DEFAULT_SIZE, 200, 600)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(ok, 150, 150, 150)
        .addContainerGap(10, 16))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lab)
          .addComponent(hledatField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(ok, 48, 48, 48)
        )
        .addContainerGap(20, 20))
    );

    //pridání komponent
    //----------------------------------------------------------------------------
    MyGui.setLayout(new BorderLayout());
    MyGui.add(a, BorderLayout.LINE_START);
    MyGui.add(ListPanel, BorderLayout.CENTER);
    MyGui.add(c, BorderLayout.LINE_END);
    MyGui.add(d, BorderLayout.PAGE_START);

    //lsteners
    //----------------------------------------------------------------------------
    list.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          podej_vloz();
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    hledatField.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
          if (list.getModel().getSize() > 0) {
            list.setSelectedIndex(0);
          }
          podej_vloz();
          hledatField.setText("");
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });

    ok.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        podej_vloz();
      }
    });

  }

  @Override
  public synchronized void nacti() {
    if (kam == typOperace.VLOZ) {
//      list.setModel(sklad.getVyndane().getListModel());
      listListener.setModel(sklad.getVyndane().getListModel());
    } else {
//      list.setModel(sklad.getModel());
      listListener.setModel(sklad.getModel());
    }
    listListener.updateList();
    MyGui.revalidate();
    MyGui.repaint();
    hledatField.requestFocus();
  }

  private void podej_vloz() {
    if (kam != typOperace.NIC) {
      Object value = list.getSelectedValue();
      if (value != null) {
        Box box = (Box) value;
        if (box.getStav() == typOperace.NIC) {
          box.setStav(kam);
          rob.addToBuffer(box);
          nacti();
          hledatField.setText("");
          nacti();
        }
      }
    } else {
      editor.load();
    }
  }

  public void Vkladani() {
    kam = typOperace.VLOZ;
    ok.setText("Vložit");
    if (edit) {
      MyGui.remove(editor);
      MyGui.add(ListPanel, BorderLayout.CENTER);
    }
    edit = false;
    nacti();
  }

  public void Podavani() {
    kam = typOperace.PODEJ;
    ok.setText("Podat");
    if (edit) {
      MyGui.remove(editor);
      MyGui.add(ListPanel, BorderLayout.CENTER);
    }
    edit = false;
    nacti();
  }

  public void Editovani() {
    kam = typOperace.NIC;
    ok.setText("Upravit");
    if (!edit) {
      MyGui.remove(ListPanel);
      MyGui.add(editor, BorderLayout.CENTER);
    }
    edit = true;
    nacti();
  }
}
