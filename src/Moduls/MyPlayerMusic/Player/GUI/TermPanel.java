/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import Moduls.MyPlayerMusic.Player.Songs;
import Moduls.MyPlayerMusic.Player.Term;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyComboUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class TermPanel extends JPanel {

  private final JComboBox<String> Type;
  private final JComboBox<String> Comparation;
  private final JComboBox<String> Value;
  private final ArrayList<Term> ostatni;
  private Term podminka;
  private final Songs songs;

  public TermPanel(MusicOrganiser o) {
    ostatni = new ArrayList<>();
    songs = o.getSongs();
    podminka = new Term();
    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));
    this.setBackground(AppSettings.getColour("BG_Color"));
    this.Type = new JComboBox<>(new String[]{"Název", "Autor", "Album", "Jazyk", "Tagy"});
    this.Comparation = new JComboBox<>(new String[]{"Odpovídá", "Neodpovídá",
      "Obsahuje", "Neobsahuje", "Začíná", "Končí"});
    this.Value = new JComboBox<>();

    Type.setUI(new MyComboUI());
    Comparation.setUI(new MyComboUI());
    Value.setUI(new MyComboUI());

    Type.setForeground(AppSettings.getColour("FG_Color"));
    Comparation.setForeground(AppSettings.getColour("FG_Color"));
    Value.setForeground(AppSettings.getColour("FG_Color"));

    Type.setBackground(AppSettings.getColour("BG_Color"));
    Comparation.setBackground(AppSettings.getColour("BG_Color"));
    Value.setBackground(AppSettings.getColour("BG_Color"));

    Type.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    Comparation.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    Value.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    Type.setFont(f);
    Comparation.setFont(f);
    Value.setFont(f);

    Dimension pref = new Dimension(150, AppSettings.getInt("Font_Size") + 20);
    Dimension max = new Dimension(500, AppSettings.getInt("Font_Size") + 20);
    Type.setPreferredSize(pref);
    Type.setMaximumSize(pref);
    Comparation.setPreferredSize(pref);
    Comparation.setMaximumSize(pref);
    Value.setPreferredSize(max);
    Value.setMaximumSize(max);

    this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    this.add(Type);
    this.add(Box.createRigidArea(new Dimension(5, 10)));
    this.add(Comparation);
    this.add(Box.createRigidArea(new Dimension(5, 10)));
    this.add(Value);
    
    Type.setSelectedIndex(0);
    Comparation.setSelectedIndex(0);
    Value.setEditable(true);
    Value.getEditor().getEditorComponent().setForeground(AppSettings.getColour("FG_Color"));
    Value.getEditor().getEditorComponent().setBackground(AppSettings.getColour("BG_Color"));

//    updateList();
    podminka.setComparation(Comparation.getSelectedItem().toString());
    podminka.setType(Type.getSelectedItem().toString());

    Value.addActionListener((ActionEvent e) -> {
      podminka.setValue(Value.getSelectedItem().toString());
    });

    Comparation.addActionListener((ActionEvent e) -> {
      podminka.setComparation(Comparation.getSelectedItem().toString());
    });

    Type.addActionListener((ActionEvent e) -> {
      podminka.setType(Type.getSelectedItem().toString());
      updateList();
    });
  }

  public void updateList() {
    ArrayList<String> a = songs.getLabelsByTerms(Type.getSelectedItem().toString(), ostatni);
    a.add("...");
    Value.setModel(new DefaultComboBoxModel(a.toArray()));
    Value.setSelectedItem("...");
  }

  public void setOstatni(ArrayList<Term> vse) {
    this.ostatni.clear();
    vse.stream().filter((vse1) -> (vse1 != podminka)).forEachOrdered((vse1) -> {
      ostatni.add(vse1);
    });
  }

  public Term getPodminka() {
    return podminka;
  }

  public void setPodminka(Term podm) {
    podminka = podm;

    String T = podminka.getType();
    String C = podminka.getComparation();
    String V = podminka.getValue();

    Type.setSelectedItem(T);
    setOstatni(ostatni);
    updateList();
    Comparation.setSelectedItem(C);
    Value.getEditor().setItem(V);

    podminka.setComparation(Comparation.getSelectedItem().toString());
    podminka.setType(Type.getSelectedItem().toString());
    podminka.setValue(Value.getEditor().getItem().toString());

  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(900, AppSettings.getInt("Font_Size") + 20);
  }
  
}
