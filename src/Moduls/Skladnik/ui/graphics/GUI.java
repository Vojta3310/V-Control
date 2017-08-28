package Moduls.Skladnik.ui.graphics;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.DataStructure.ModelService;
import Moduls.Skladnik.Enums.typOperace;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.io.xml.XML.Tag;
import Moduls.Skladnik.skladnik.Robot;
import Moduls.Skladnik.ui.graphics.CellRenderers.BufferCellRenderer;
import Moduls.Skladnik.ui.graphics.listeners.BufferListener;
import Moduls.Skladnik.ui.graphics.listeners.SearchFieldListener;
import VControl.UI.components.MyCellRenderer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Ondřej Bleha & Vojta3310
 */
public class GUI extends javax.swing.JFrame {

    private final ISklad sklad;
    private final ModelService vyndane;
    private final Robot rob;
    private final XML xml;
    private final ConsoleFrame console;
    private Box infoBox;
    
    /**
     * Creates new form GUI
     * @param sklad
     * @param rob
     * @param xml
     */
    public GUI(ISklad sklad, Robot rob, XML xml) {
        //podle argumentu konstruktoru naplni dane promene
        this.sklad = sklad;
        this.vyndane = sklad.getVyndane();
        this.rob = rob;
        this.xml = xml;
        this.console = new ConsoleFrame(rob.getConsole());
        
        //zalozi vsechny komponenty, které jsou popsané ve dane fci
        initComponents();
        
        //nastavi modely listum
        listVyndane.setModel(vyndane.getListModel());
        bufferList.setModel(rob.getBufferListModel());
        list.setModel(sklad.getModel());
        
        //vytvoří muj vlastní renderer boxu pri JList
        list.setCellRenderer(new MyCellRenderer(list));        
        listVyndane.setCellRenderer(new MyCellRenderer(listVyndane));
        bufferList.setCellRenderer(new BufferCellRenderer(bufferList, this));
        
        //nastavi filtrovani obsahu listu
        SearchFieldListener listListener = new SearchFieldListener(list, tree, sklad.getKategorie());
        hledatField.getDocument().addDocumentListener(listListener);
        
        //nastavi aktualizovani listu pri zmene v bufferu
        bufferList.getModel().addListDataListener(new BufferListener(list, listVyndane));
        
        //priradi JListu s obsahem skladu dany border
        list.setBorder ( BorderFactory.createEmptyBorder ( 5, 5, 5, 5 ) );
        
        //vytvori instanci objektu, který bude aktualizovat label s casem
        TimeUpdater tu = new TimeUpdater(time);
        
        //vytvori instanci objektu, který bude aktualizovat label se souradnicemi robota
        RobPosUpdater rpu = new RobPosUpdater(progressBar, robPos, this.rob);
        
        
        //rozbali strom kategorii
        tree.setModel(this.sklad.getTreeModel());
        for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
        tree.setSelectionRow(0);
        tree2.setModel(this.sklad.getTreeModel());
        
        //nastavi velikost okna
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        obrazovka.setSize(obrazovka.width-100, obrazovka.height-100);
        this.setSize(obrazovka);
        
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/ui/graphics/icons/logo_16px.png")));
        rob.setGui(this);
        this.setVisible(true);
    }
    
    public void nacti() {
        int vyndaneSelect = listVyndane.getSelectedIndex();
        
        //list.setModel(hledej(typOperace.NIC));
        //listVyndane.setModel(vyndane.getListModel());
        //bufferList.setModel(rob.getBufferListModel());
                
        if (list.getModel().getSize() > 0){
            list.setSelectedIndex(0);
            podej.setEnabled(true);
        }else{
            podej.setEnabled(false);
        }
        if (listVyndane.getModel().getSize() > 0 && (list.getModel().getSize() < sklad.getHLOUBKA()*sklad.getSIRKA()*sklad.getVYSKA()-(sklad.getHLOUBKA()-1))){
            if(vyndaneSelect == -1 || vyndaneSelect <= listVyndane.getModel().getSize()){
                listVyndane.setSelectedIndex(0);
            }else{
                listVyndane.setSelectedIndex(vyndaneSelect);
            }
            vloz.setEnabled(true);
            odeberkrabici.setEnabled(true);
        }else{
            vloz.setEnabled(false);
            odeberkrabici.setEnabled(false);
        }
        zobrazenychPolozek.setText("Zobrazených položek: "+list.getModel().getSize()+"/"+(sklad.getHLOUBKA()*sklad.getSIRKA()*sklad.getVYSKA()-(sklad.getVYSKA()-1)));
        nactiInfo(infoBox);
        /*try {
            xml.stow(sklad);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        revalidate();
        repaint();
    }
    
    private void nactiInfo(Box box){
        infoBox = box;
        if (infoBox != null){            
            nazevField.setText(infoBox.getObsah());
            idField.setValue(infoBox.getID());
            for (int i = tree2.getRowCount(); i > 0; i--) {
                tree2.collapseRow(i);
            }
            tree2.setSelectionPath(sklad.getKategorie().getPath(infoBox.getKategorie()));
        }
    }
    
    public void saveInfoBox(){
        infoBox.setObsah(nazevField.getText());
        infoBox.setID((int) idField.getValue());
        DefaultMutableTreeNode uzel = (DefaultMutableTreeNode) (tree2.getSelectionPath().getLastPathComponent());
        Tag t = (Tag) uzel.getUserObject();
        infoBox.setKategorie(t.getID());
    }
    
    private void vloz(){
      Object value = listVyndane.getSelectedValue();
      if(value != null){
        Box box = (Box) value;
        if(box.getStav() == typOperace.NIC){
          box.setStav(typOperace.VLOZ);
          rob.addToBuffer(box);
          nacti();
        }
      }
    }
    
    private void podej(){
      Object value = list.getSelectedValue();
      if(value != null){
        Box box = (Box) value;
        if(box.getStav() == typOperace.NIC){
          box.setStav(typOperace.PODEJ);
          rob.addToBuffer(box);
          nacti();
        }
      }
    }
    
    private void rozmerySkladuDialog(){
        JSpinner vyska = new JSpinner(new SpinnerNumberModel(sklad.getVYSKA(), 1, 1000, 1)); //default value,lower bound,upper bound,increment by
        JSpinner sirka = new JSpinner(new SpinnerNumberModel(sklad.getSIRKA(), 1, 1000, 1));
        JSpinner hloubka = new JSpinner(new SpinnerNumberModel(sklad.getHLOUBKA(), 1, 1000, 1));
        final JComponent[] inputs = new JComponent[] {
            new JLabel("Výška:"),
            vyska,
            new JLabel("Šířka:"),
            sirka,
            new JLabel("Hloubka:"),
            hloubka
        };
        JOptionPane.showMessageDialog(null, inputs, "Rozměry skladu", JOptionPane.PLAIN_MESSAGE);
        sklad.nastavSirku((int) vyska.getValue());
        sklad.nastavVysku((int) sirka.getValue());
        sklad.nastavHloubku((int) hloubka.getValue());
        nacti();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jTextField1 = new javax.swing.JTextField();
    jScrollPane7 = new javax.swing.JScrollPane();
    jList1 = new javax.swing.JList();
    jRadioButton1 = new javax.swing.JRadioButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    jList2 = new javax.swing.JList();
    jPanel1 = new javax.swing.JPanel();
    jPanel5 = new javax.swing.JPanel();
    zobrazenychPolozek = new javax.swing.JLabel();
    time = new javax.swing.JLabel();
    robPos = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    jPanel9 = new javax.swing.JPanel();
    jSplitPane1 = new javax.swing.JSplitPane();
    jScrollPane1 = new javax.swing.JScrollPane();
    list = new javax.swing.JList();
    jScrollPane5 = new javax.swing.JScrollPane();
    tree = new javax.swing.JTree();
    jSplitPane2 = new javax.swing.JSplitPane();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    jScrollPane4 = new javax.swing.JScrollPane();
    bufferList = new javax.swing.JList();
    jPanel8 = new javax.swing.JPanel();
    nazevField = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    idField = new javax.swing.JSpinner();
    jButton1 = new javax.swing.JButton();
    jScrollPane6 = new javax.swing.JScrollPane();
    tree2 = new javax.swing.JTree();
    jScrollPane3 = new javax.swing.JScrollPane();
    listVyndane = new javax.swing.JList();
    ToolBar = new javax.swing.JToolBar();
    pridejkrabici = new javax.swing.JButton();
    odeberkrabici = new javax.swing.JButton();
    vloz = new javax.swing.JButton();
    jSeparator1 = new javax.swing.JToolBar.Separator();
    hledatField = new javax.swing.JTextField();
    podej = new javax.swing.JButton();
    jSeparator2 = new javax.swing.JToolBar.Separator();
    jButton2 = new javax.swing.JButton();
    addCategory = new javax.swing.JButton();
    removeCategory = new javax.swing.JButton();
    editCategory = new javax.swing.JButton();
    Menu = new javax.swing.JMenuBar();
    menuSklad = new javax.swing.JMenu();
    newBox = new javax.swing.JMenuItem();
    jMenuItem1 = new javax.swing.JMenuItem();
    save = new javax.swing.JMenuItem();
    menuKategorie = new javax.swing.JMenu();
    jMenuItem2 = new javax.swing.JMenuItem();
    jMenuItem3 = new javax.swing.JMenuItem();
    jMenuItem4 = new javax.swing.JMenuItem();

    jTextField1.setText("jTextField1");

    jList1.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jScrollPane7.setViewportView(jList1);

    jRadioButton1.setText("jRadioButton1");

    jList2.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jScrollPane2.setViewportView(jList2);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Skladník");
    setFont(new java.awt.Font("Calibri", 0, 10)); // NOI18N

    jPanel5.setBorder(null);

    zobrazenychPolozek.setFont(new java.awt.Font("Noto Sans", 0, 18)); // NOI18N
    zobrazenychPolozek.setText("Zobrazených položek: 0/0");
    zobrazenychPolozek.setBorder(null);

    time.setFont(new java.awt.Font("Noto Sans", 0, 18)); // NOI18N
    time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    time.setText("00:00:00");
    time.setBorder(null);

    robPos.setFont(new java.awt.Font("Noto Sans", 0, 18)); // NOI18N
    robPos.setText("X: 0 | Y: 0");

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addComponent(zobrazenychPolozek)
        .addGap(18, 18, 18)
        .addComponent(robPos)
        .addGap(65, 65, 65)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0))
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(zobrazenychPolozek, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(robPos))
          .addComponent(time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    jSplitPane1.setDividerLocation(300);
    jSplitPane1.setContinuousLayout(true);

    list.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    list.setFixedCellHeight(80);
    list.setFixedCellWidth(250);
    list.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
    list.setRequestFocusEnabled(false);
    list.setVisibleRowCount(-1);
    list.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        listMouseClicked(evt);
      }
    });
    list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        listValueChanged(evt);
      }
    });
    jScrollPane1.setViewportView(list);

    jSplitPane1.setRightComponent(jScrollPane1);

    javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Vše");
    javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Jídlo");
    javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ovoce");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("zelenina");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
      public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
        treeValueChanged(evt);
      }
    });
    jScrollPane5.setViewportView(tree);

    jSplitPane1.setLeftComponent(jScrollPane5);

    jSplitPane2.setDividerLocation(400);
    jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane2.setContinuousLayout(true);

    jTabbedPane1.setBackground(new java.awt.Color(107, 107, 107));

    bufferList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    bufferList.setFixedCellHeight(80);
    bufferList.setVisibleRowCount(-1);
    jScrollPane4.setViewportView(bufferList);

    jTabbedPane1.addTab("Buffer", jScrollPane4);

    jPanel8.setBackground(new java.awt.Color(236, 236, 236));

    nazevField.setFont(new java.awt.Font("Noto Sans", 0, 24)); // NOI18N
    nazevField.setText("Název");

    jLabel5.setText("ID:");

    jLabel6.setText("Kategorie:");

    jButton1.setText("Uložit");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Vše");
    treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Jídlo");
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ovoce");
    treeNode2.add(treeNode3);
    treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("zelenina");
    treeNode2.add(treeNode3);
    treeNode1.add(treeNode2);
    tree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    tree2.setRequestFocusEnabled(false);
    jScrollPane6.setViewportView(tree2);

    javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
    jPanel8.setLayout(jPanel8Layout);
    jPanel8Layout.setHorizontalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(nazevField)
          .addGroup(jPanel8Layout.createSequentialGroup()
            .addComponent(jLabel6)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(jPanel8Layout.createSequentialGroup()
            .addComponent(jLabel5)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap())
    );
    jPanel8Layout.setVerticalGroup(
      jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel8Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(nazevField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel6)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jButton1)
        .addContainerGap())
    );

    jTabbedPane1.addTab("Nastavení", jPanel8);

    jSplitPane2.setLeftComponent(jTabbedPane1);

    listVyndane.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    listVyndane.setFixedCellHeight(80);
    listVyndane.setFocusable(false);
    listVyndane.setMinimumSize(new java.awt.Dimension(67, 702));
    listVyndane.setPreferredSize(new java.awt.Dimension(67, 702));
    listVyndane.setRequestFocusEnabled(false);
    listVyndane.setVisibleRowCount(-1);
    listVyndane.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        listVyndaneMouseReleased(evt);
      }
    });
    listVyndane.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        listVyndaneValueChanged(evt);
      }
    });
    jScrollPane3.setViewportView(listVyndane);

    jSplitPane2.setRightComponent(jScrollPane3);

    javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
    jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(
      jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel9Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0))
    );
    jPanel9Layout.setVerticalGroup(
      jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel9Layout.createSequentialGroup()
        .addGap(0, 0, 0)
        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE))
        .addGap(0, 0, 0))
    );

    ToolBar.setFloatable(false);
    ToolBar.setRollover(true);
    ToolBar.setFocusable(false);
    ToolBar.setRequestFocusEnabled(false);

    pridejkrabici.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/icon_add_blue_32px.png"))); // NOI18N
    pridejkrabici.setBorderPainted(false);
    pridejkrabici.setFocusable(false);
    pridejkrabici.setHideActionText(true);
    pridejkrabici.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    pridejkrabici.setRequestFocusEnabled(false);
    pridejkrabici.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    pridejkrabici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pridejkrabiciActionPerformed(evt);
      }
    });
    ToolBar.add(pridejkrabici);

    odeberkrabici.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/icon_remove_blue_32px.png"))); // NOI18N
    odeberkrabici.setBorderPainted(false);
    odeberkrabici.setFocusable(false);
    odeberkrabici.setHideActionText(true);
    odeberkrabici.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    odeberkrabici.setRequestFocusEnabled(false);
    odeberkrabici.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    odeberkrabici.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        odeberkrabiciActionPerformed(evt);
      }
    });
    ToolBar.add(odeberkrabici);

    vloz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/icon_vlozit_blueV_32px.png"))); // NOI18N
    vloz.setBorderPainted(false);
    vloz.setFocusable(false);
    vloz.setHideActionText(true);
    vloz.setRequestFocusEnabled(false);
    vloz.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        vlozActionPerformed(evt);
      }
    });
    ToolBar.add(vloz);
    ToolBar.add(jSeparator1);

    hledatField.setFont(hledatField.getFont().deriveFont(hledatField.getFont().getStyle() | java.awt.Font.BOLD, 24));
    hledatField.setFocusCycleRoot(true);
    hledatField.setMaximumSize(new java.awt.Dimension(800, 47));
    hledatField.setMinimumSize(new java.awt.Dimension(10, 46));
    hledatField.setPreferredSize(new java.awt.Dimension(500, 46));
    hledatField.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        hledatFieldKeyPressed(evt);
      }
      public void keyReleased(java.awt.event.KeyEvent evt) {
        hledatFieldKeyReleased(evt);
      }
    });
    ToolBar.add(hledatField);

    podej.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/icon_podej_blue_32px.png"))); // NOI18N
    podej.setBorderPainted(false);
    podej.setFocusable(false);
    podej.setHideActionText(true);
    podej.setRequestFocusEnabled(false);
    podej.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        podejActionPerformed(evt);
      }
    });
    ToolBar.add(podej);
    ToolBar.add(jSeparator2);

    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/icon_console_blue_32px.png"))); // NOI18N
    jButton2.setBorderPainted(false);
    jButton2.setFocusable(false);
    jButton2.setHideActionText(true);
    jButton2.setRequestFocusEnabled(false);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });
    ToolBar.add(jButton2);

    addCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/plus_32px.png"))); // NOI18N
    addCategory.setFocusable(false);
    addCategory.setHideActionText(true);
    addCategory.setRequestFocusEnabled(false);
    addCategory.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addCategoryActionPerformed(evt);
      }
    });
    ToolBar.add(addCategory);

    removeCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/minus_32px.png"))); // NOI18N
    removeCategory.setFocusable(false);
    removeCategory.setHideActionText(true);
    removeCategory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    removeCategory.setRequestFocusEnabled(false);
    removeCategory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    removeCategory.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        removeCategoryActionPerformed(evt);
      }
    });
    ToolBar.add(removeCategory);

    editCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/gear_blue_32px.png"))); // NOI18N
    editCategory.setFocusable(false);
    editCategory.setHideActionText(true);
    editCategory.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    editCategory.setRequestFocusEnabled(false);
    editCategory.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    editCategory.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        editCategoryActionPerformed(evt);
      }
    });
    ToolBar.add(editCategory);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(ToolBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1113, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addComponent(ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(6, 6, 6)
        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, 0))
    );

    menuSklad.setText("Sklad");

    newBox.setText("Nový box");
    newBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newBoxActionPerformed(evt);
      }
    });
    menuSklad.add(newBox);

    jMenuItem1.setText("Změnit rozměry");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem1ActionPerformed(evt);
      }
    });
    menuSklad.add(jMenuItem1);

    save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    save.setText("Uložit");
    save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveActionPerformed(evt);
      }
    });
    menuSklad.add(save);

    Menu.add(menuSklad);

    menuKategorie.setText("Kategorie");

    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/plus_32px.png"))); // NOI18N
    jMenuItem2.setText("Přidat");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem2ActionPerformed(evt);
      }
    });
    menuKategorie.add(jMenuItem2);

    jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/minus_32px.png"))); // NOI18N
    jMenuItem3.setText("Odebrat");
    menuKategorie.add(jMenuItem3);

    jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/graphics/icons/blue/gear_blue_32px.png"))); // NOI18N
    jMenuItem4.setText("Editovat");
    menuKategorie.add(jMenuItem4);

    Menu.add(menuKategorie);

    setJMenuBar(Menu);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGap(0, 0, 0))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void podejActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_podejActionPerformed
        podej();
    }//GEN-LAST:event_podejActionPerformed

    private void hledatFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hledatFieldKeyReleased
        /*if ((evt.getKeyCode() == KeyEvent.VK_ENTER)){
            nacti();
        }*/
        //System.out.println(hledatField.getText());
        nacti();
    }//GEN-LAST:event_hledatFieldKeyReleased

    private void newBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBoxActionPerformed
        //BoxGUI bgui = new BoxGUI();
        JTextField obsah = new JTextField();
        obsah.setFocusable(true);
        obsah.setFocusAccelerator((char)0);
        JTextField pojmy = new JTextField();
        JSpinner id = new JSpinner();
        pojmy.setFocusAccelerator((char)1);
        final JComponent[] inputs = new JComponent[] {
            new JLabel("Obsah:"),
            obsah,
            new JLabel("Pojmy:"),
            pojmy,
            new JLabel("ID:"),
            id
        };
        obsah.requestFocus(true);
        obsah.requestFocusInWindow();
        JOptionPane.showMessageDialog(null, inputs, "Nový box", JOptionPane.PLAIN_MESSAGE);
        if (!obsah.getText().equals("")){
            vyndane.vlozBox(new Box(obsah.getText(), 0, pojmy.getText(), (int) id.getValue(), 0));
            nacti();
        }
    }//GEN-LAST:event_newBoxActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        rozmerySkladuDialog();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void vlozActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vlozActionPerformed
      vloz();
    }//GEN-LAST:event_vlozActionPerformed

    private void listVyndaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listVyndaneMouseReleased
        if (evt.getButton()== 3 && listVyndane.getModel().getSize() > 0){
            JPopupMenu menu = new JPopupMenu();
            
            int index = -1;
            index = listVyndane.locationToIndex(evt.getPoint());
            if(index!=-1){
                listVyndane.setSelectedIndex(index);
            }
            
            JMenuItem item1 = new JMenuItem("Odstranit");            
            item1.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    OdstranitActionPerformed(evt);
                }
            });
            menu.add(item1);
            
            JMenuItem item2 = new JMenuItem("Upravit");            
            item2.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    UpravitActionPerformed(evt);
                }
            });
            menu.add(item2);
            
            menu.show(evt.getComponent(), evt.getX(), evt.getY());
        }else if(evt.getClickCount() == 2){
            vloz(); 
        }
                
    }//GEN-LAST:event_listVyndaneMouseReleased

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        try {
            xml.stow(sklad);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void treeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_treeValueChanged
        //hledej(typOperace.NIC);
        nacti();
    }//GEN-LAST:event_treeValueChanged

    private void listValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listValueChanged
        nactiInfo((Box) list.getSelectedValue());
    }//GEN-LAST:event_listValueChanged

    private void listVyndaneValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listVyndaneValueChanged
        nactiInfo((Box) listVyndane.getSelectedValue());
    }//GEN-LAST:event_listVyndaneValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveInfoBox();
        nacti();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(!console.isVisible()) console.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void odeberkrabiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odeberkrabiciActionPerformed
        vyndane.vyndejBox((Box) listVyndane.getSelectedValue());
        nacti();
    }//GEN-LAST:event_odeberkrabiciActionPerformed

    private void pridejkrabiciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridejkrabiciActionPerformed
        this.newBoxActionPerformed(evt);
    }//GEN-LAST:event_pridejkrabiciActionPerformed

    private void hledatFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hledatFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            podej();
            hledatField.setText("");
        }
    }//GEN-LAST:event_hledatFieldKeyPressed

    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
        if (evt.getClickCount() == 2) {
            podej();
        }
    }//GEN-LAST:event_listMouseClicked

    private void addCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryActionPerformed
        sklad.getKategorie().addKategorie(tree);
    }//GEN-LAST:event_addCategoryActionPerformed

    private void removeCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCategoryActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog (this, "Opravdu chcete smazat danou položku?","Mazání položky", JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            sklad.getKategorie().removeKategorie(tree);
        }
    }//GEN-LAST:event_removeCategoryActionPerformed

    private void editCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCategoryActionPerformed
        sklad.getKategorie().editKategorie(tree);
    }//GEN-LAST:event_editCategoryActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        sklad.getKategorie().addKategorie(tree);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void OdstranitActionPerformed(java.awt.event.ActionEvent evt) {
        vyndane.vyndejBox((Box) listVyndane.getSelectedValue());
        nacti();
    }
    
    private void UpravitActionPerformed(java.awt.event.ActionEvent evt) {
        Box box = (Box) listVyndane.getSelectedValue();
        JTextField obsah = new JTextField(box.getObsah());
        JTextField pojmy = new JTextField(box.getPojmy());
        JSpinner id = new JSpinner();
        id.setValue(box.getID());
        final JComponent[] inputs = new JComponent[] {
            new JLabel("Obsah:"),
            obsah,
            new JLabel("Pojmy:"),
            pojmy,
            new JLabel("ID:"),
            id
        };
        JOptionPane.showMessageDialog(null, inputs, "Box", JOptionPane.PLAIN_MESSAGE);
        box.setID((int) id.getValue());
        box.setPojmy(pojmy.getText());
        box.setObsah(obsah.getText());
    }    

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuBar Menu;
  private javax.swing.JToolBar ToolBar;
  private javax.swing.JButton addCategory;
  private javax.swing.JList bufferList;
  private javax.swing.JButton editCategory;
  private javax.swing.JTextField hledatField;
  private javax.swing.JSpinner idField;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JList jList1;
  private javax.swing.JList jList2;
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JMenuItem jMenuItem3;
  private javax.swing.JMenuItem jMenuItem4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JPanel jPanel8;
  private javax.swing.JPanel jPanel9;
  private javax.swing.JRadioButton jRadioButton1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JScrollPane jScrollPane5;
  private javax.swing.JScrollPane jScrollPane6;
  private javax.swing.JScrollPane jScrollPane7;
  private javax.swing.JToolBar.Separator jSeparator1;
  private javax.swing.JToolBar.Separator jSeparator2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JTextField jTextField1;
  private javax.swing.JList list;
  private javax.swing.JList listVyndane;
  private javax.swing.JMenu menuKategorie;
  private javax.swing.JMenu menuSklad;
  private javax.swing.JTextField nazevField;
  private javax.swing.JMenuItem newBox;
  private javax.swing.JButton odeberkrabici;
  private javax.swing.JButton podej;
  private javax.swing.JButton pridejkrabici;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JButton removeCategory;
  private javax.swing.JLabel robPos;
  private javax.swing.JMenuItem save;
  private javax.swing.JLabel time;
  private javax.swing.JTree tree;
  private javax.swing.JTree tree2;
  private javax.swing.JButton vloz;
  private javax.swing.JLabel zobrazenychPolozek;
  // End of variables declaration//GEN-END:variables
}
