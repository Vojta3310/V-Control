/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik.ui.ModulUI;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.ui.graphics.listeners.SearchFieldListener;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import VControl.UI.components.MyField;
import VControl.UI.components.MyScrollbarUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.BorderFactory.createEmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author vojta3310
 */
public class BoxEditor extends JPanel {

  private final JTree tree;
  private final JList list = new JList<>();

  private final ISklad sklad;
  private final XML xml;
  private final SearchFieldListener listListener;
  private Box infoBox;

  private final MyField Obsah = new MyField();
  private final MyField Synonima = new MyField();
  private final JLabel ID = new JLabel("0");
  private final JLabel vytazeno = new JLabel("0");
  private final JLabel kategorie = new JLabel("Vše");
  private final JLabel operace = new JLabel("Neexistuje");

  private final MyButton save = new MyButton("Uložit");
  private final MyButton cancel = new MyButton("Zrušit");
  private final MyButton create = new MyButton("Přidat");
  private final MyButton delete = new MyButton("Smazat");

  public BoxEditor(JTree tr, ISklad sk, XML xm) {
    this.tree = tr;
    this.sklad = sk;
    this.xml = xm;

    //nastavení vzhledu komponent
    //----------------------------------------------------------------------------
    setBackground(AppSettings.getColour("BG_Color"));
    list.setBackground(AppSettings.getColour("BG_Color"));
    list.setForeground(AppSettings.getColour("FG_Color"));
    list.setCellRenderer(new Moduls.Skladnik.ui.graphics.CellRenderers.MyCellRenderer(list));
    list.setFixedCellHeight(80);
    list.setFixedCellWidth(230);
    list.setVisibleRowCount(-1);
//    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    JScrollPane b = new JScrollPane(list);
    b.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    b.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    b.setViewportBorder(null);
    b.setBorder(createEmptyBorder());
    JScrollBar sb2 = b.getVerticalScrollBar();
    sb2.setPreferredSize(new Dimension(AppSettings.getInt("Border_Size"), Integer.MAX_VALUE));
    sb2.setUI(new MyScrollbarUI());

    JLabel ObsahL = new JLabel("Obsah:");
    JLabel SynonimaL = new JLabel("Synonima:");
    JLabel IDL = new JLabel("ID boxu:");
    JLabel vytazenoL = new JLabel("Vytaženo:");
    JLabel kategorieL = new JLabel("Kategorie:");
    JLabel operaceL = new JLabel("Stav boxu:");
    JLabel nadpis = new JLabel("Vlastnosti boxu:");

    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));

    ID.setFont(f);
    vytazeno.setFont(f);
    kategorie.setFont(f);
    operace.setFont(f);
    ObsahL.setFont(f);
    SynonimaL.setFont(f);
    IDL.setFont(f);
    vytazenoL.setFont(f);
    kategorieL.setFont(f);
    operaceL.setFont(f);
    nadpis.setFont(f.deriveFont(1, 2 * AppSettings.getInt("Font_Size")));

    ID.setForeground(AppSettings.getColour("FG_Color"));
    vytazeno.setForeground(AppSettings.getColour("FG_Color"));
    kategorie.setForeground(AppSettings.getColour("FG_Color"));
    operace.setForeground(AppSettings.getColour("FG_Color"));
    ObsahL.setForeground(AppSettings.getColour("FG_Color"));
    SynonimaL.setForeground(AppSettings.getColour("FG_Color"));
    IDL.setForeground(AppSettings.getColour("FG_Color"));
    vytazenoL.setForeground(AppSettings.getColour("FG_Color"));
    kategorieL.setForeground(AppSettings.getColour("FG_Color"));
    operaceL.setForeground(AppSettings.getColour("FG_Color"));
    nadpis.setForeground(AppSettings.getColour("FG_Color"));

    //pridání komponent
    //----------------------------------------------------------------------------
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(b, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(16, 16, 16)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(ObsahL)
              .addComponent(SynonimaL)
              .addComponent(IDL)
              .addComponent(vytazenoL)
              .addComponent(kategorieL)
              .addComponent(operaceL))
            .addGap(32, 32, 32)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(Obsah, 200, 255, 260)
              .addComponent(Synonima, 200, 255, 260)
              .addComponent(ID)
              .addComponent(vytazeno)
              .addComponent(kategorie)
              .addComponent(operace))
            .addContainerGap(374, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(nadpis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(create, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )))
            .addGap(16 + AppSettings.getInt("Border_Size"), 16 + AppSettings.getInt("Border_Size"), 16 + AppSettings.getInt("Border_Size")))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(b)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(nadpis, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(ObsahL)
          .addComponent(Obsah, 36, 36, 36))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(SynonimaL)
          .addComponent(Synonima, 36, 36, 36))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(IDL)
          .addComponent(ID))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(vytazenoL)
          .addComponent(vytazeno))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(kategorieL)
          .addComponent(kategorie))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(operaceL)
          .addComponent(operace))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(delete, 54, 54, 54)
          .addComponent(save, 54, 54, 54)
        )
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(create, 54, 54, 54)
          .addComponent(cancel, 54, 54, 54)
        )
        .addContainerGap())
    );
    //lsteners
    //----------------------------------------------------------------------------

    list.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          LoadBox((Box) list.getModel().getElementAt(list.getSelectedIndex()));
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

    cancel.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        LoadBox(null);
      }
    });

    save.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        saveBox();
        UpdateModel();
      }
    });

    create.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (!Obsah.getText().equals("")) {
          DefaultMutableTreeNode uzel = (DefaultMutableTreeNode) (tree.getSelectionPath().getLastPathComponent());
          XML.Tag t = (XML.Tag) uzel.getUserObject();
          sklad.getVyndane().vlozBox(new Box(Obsah.getText(), t.getID(), Synonima.getText(), Integer.parseInt(ID.getText()), 0));
          LoadBox(null);
          UpdateModel();
          try {
            xml.stow(sklad);
          } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(BoxEditor.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    });

    delete.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (infoBox != null) {
          sklad.getVyndane().vyndejBox(infoBox);
          LoadBox(null);
          UpdateModel();
          try {
            xml.stow(sklad);
          } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(BoxEditor.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    });

    DefaultListModel a = new DefaultListModel();
    for (int i = 0; i < sklad.getModel().size(); i++) {
      a.addElement(sklad.getModel().get(i));
    }
    for (int i = 0; i < sklad.getVyndane().getListModel().size(); i++) {
      a.addElement(sklad.getVyndane().getListModel().get(i));
    }

    list.setModel(a);
    listListener = new SearchFieldListener(list, tree, sklad.getKategorie());
    UpdateModel();
    LoadBox(null);
  }

  private void UpdateModel() {
    DefaultListModel a = new DefaultListModel();
    for (int i = 0; i < sklad.getModel().size(); i++) {
      a.addElement(sklad.getModel().get(i));
    }
    for (int i = 0; i < sklad.getVyndane().getListModel().size(); i++) {
      a.addElement(sklad.getVyndane().getListModel().get(i));
    }

    listListener.setModel(a);
    listListener.updateList();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
    g.setColor(AppSettings.getColour("FG_Color"));
    g.fillRect(getWidth() - AppSettings.getInt("Border_Size"), 0, AppSettings.getInt("Border_Size"), getHeight());
  }

  private void LoadBox(Box b) {
    infoBox = b;
    if (infoBox != null) {
      Obsah.setText(infoBox.getObsah());
      Synonima.setText(infoBox.getPojmy());
      ID.setText(Integer.toString(infoBox.getID()));
      vytazeno.setText(Integer.toString(infoBox.getVytazeno()));
      delete.setEnabled(false);
      switch (infoBox.getStav()) {
        case PODEJ: {
          operace.setText("Vydávání ze skladu");
          break;
        }
        case VLOZ: {
          operace.setText("Vkládání do skladu");
          break;
        }
        case NIC: {
          if (sklad.getVyndane().containBox(infoBox)) {
            operace.setText("Mimo sklad");
            delete.setEnabled(true);
          } else {
            operace.setText("Uklizen ve skladu");
          }
          break;
        }
      }
      tree.setSelectionPath(sklad.getKategorie().getPath(infoBox.getKategorie()));
      kategorie.setText(sklad.getKategorie().getPath(infoBox.getKategorie()).getLastPathComponent().toString());
      save.setEnabled(true);
    } else {
      Obsah.setText("");
      Synonima.setText("");
      ID.setText(Integer.toString(sklad.getFreeID()));
      vytazeno.setText(Integer.toString(0));
      operace.setText("Neexistuje");
      tree.setSelectionPath(sklad.getKategorie().getPath(0));
      kategorie.setText(sklad.getKategorie().getPath(0).getLastPathComponent().toString());
      save.setEnabled(false);
      delete.setEnabled(false);
    }
  }

  private void saveBox() {
    infoBox.setObsah(Obsah.getText());
    infoBox.setPojmy(Synonima.getText());
    infoBox.setID(Integer.parseInt(ID.getText()));
    DefaultMutableTreeNode uzel = (DefaultMutableTreeNode) (tree.getSelectionPath().getLastPathComponent());
    XML.Tag t = (XML.Tag) uzel.getUserObject();
    infoBox.setKategorie(t.getID());
    try {
      xml.stow(sklad);
    } catch (IOException | XMLStreamException ex) {
      Logger.getLogger(BoxEditor.class.getName()).log(Level.SEVERE, null, ex);
    }
    LoadBox(null);
  }

  public void load() {
    if (list.getSelectedIndex() >= 0) {
      LoadBox((Box) list.getModel().getElementAt(list.getSelectedIndex()));
    }
  }

  public SearchFieldListener getListListener() {
    return listListener;
  }

}
