/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik.ui.ModulUI;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.DataStructure.ModelService;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.skladnik.Robot;
import Moduls.Skladnik.ui.graphics.CellRenderers.BufferCellRenderer;
import Moduls.Skladnik.ui.graphics.IGUI;
import Moduls.Skladnik.ui.graphics.listeners.BufferListener;
import Moduls.Skladnik.ui.graphics.listeners.SearchFieldListener;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyCellRenderer;
import VControl.UI.components.MyField;
import VControl.UI.components.MyScrollbarUI;
import VControl.UI.components.MyTreeCellRenderer;
import com.sun.java.swing.plaf.motif.MotifTreeUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import static javax.swing.BorderFactory.createEmptyBorder;
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

    bufferList.setModel(rob.getBufferListModel());
    list.setModel(sklad.getModel());

    tree.setModel(this.sklad.getTreeModel());
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }
    tree.setSelectionRow(0);
    //nastavi filtrovani obsahu listu
    SearchFieldListener listListener = new SearchFieldListener(list, tree, sklad.getKategorie());
    hledatField.getDocument().addDocumentListener(listListener);

    //nastavi aktualizovani listu pri zmene v bufferu
    bufferList.getModel().addListDataListener(new BufferListener(list, list));
    rob.setGui(this);

    //nastavení vzhledu komponent
    tree.setBackground(AppSettings.getColour("BG_Color"));
    tree.setForeground(AppSettings.getColour("FG_Color"));
    tree.setCellRenderer(new MyTreeCellRenderer());
    tree.setUI(new MotifTreeUI());
    tree.setMinimumSize(new Dimension(250, 500));
    JScrollPane a = new JScrollPane(tree);
    a.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    a.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    a.setViewportBorder(null);
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
    JScrollPane b = new JScrollPane(list);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setViewportBorder(null);
    b.setBorder(createEmptyBorder());
    JScrollBar sb2 = b.getVerticalScrollBar();
    sb2.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb2.setUI(new MyScrollbarUI());
    
    bufferList.setBackground(AppSettings.getColour("BG_Color"));
    bufferList.setForeground(AppSettings.getColour("FG_Color"));
    bufferList.setCellRenderer(new BufferCellRenderer(list, this));
    bufferList.setFixedCellHeight(80);
    bufferList.setFixedCellWidth(230);
    bufferList.setVisibleRowCount(-1);
    JScrollPane c = new JScrollPane(bufferList);
    c.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    c.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    c.setViewportBorder(null);
    c.setBorder(createEmptyBorder());
    JScrollBar sb3 = c.getVerticalScrollBar();
    sb3.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb3.setUI(new MyScrollbarUI());
    
    //pridání komponent
    MyGui.setLayout(new BorderLayout());
    MyGui.add(a, BorderLayout.LINE_START);
    MyGui.add(b, BorderLayout.CENTER);
    MyGui.add(c, BorderLayout.LINE_END);
    MyGui.add(hledatField, BorderLayout.PAGE_START);

  }

  @Override
  public void nacti() {
    MyGui.revalidate();
    MyGui.repaint();
  }

}
