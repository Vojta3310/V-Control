/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player.GUI;

import VControl.UI.components.Stars;
import Moduls.MyPlayerMusic.Player.MusicOrganiser;
import Moduls.MyPlayerMusic.Player.RandomSong;
import Moduls.MyPlayerMusic.Player.Term;
import VControl.UI.components.MyField;
import VControl.Settings.AppSettings;
import VControl.UI.components.MyButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author vojta3310
 */
public class NextSongFilter extends JPanel {

//  private ArrayList<Term> podminky = new ArrayList<>();
  private final ArrayList<TermPanel> panely = new ArrayList<>();
  private RandomSong RS;
  private Stars stars = new Stars();
  private MyField STags = new MyField();
  private MyField Repeat = new MyField();

  public NextSongFilter(final MPgui gui, MusicOrganiser o) {

    Font f = new Font(AppSettings.getString("Font_Name"), 1, AppSettings.getInt("Font_Size"));
    this.setBackground(AppSettings.getColour("BG_Color"));

    stars.setEditabe(true);
    stars.setMaxStars(5);
    stars.setStars(0.0F);
    Repeat.setHorizontalAlignment(JTextField.CENTER);

    for (int i = 0; i < 5; i++) {
      panely.add(new TermPanel(o));
//      podminky.add(panely.get(i).getPodminka());
    }

    MyButton apply = new MyButton("Použít");
    apply.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        ArrayList<Term> podminky = new ArrayList<>();
        
        Term star = new Term();
        star.setType("Stars");
        star.setVal(stars.getStars() / stars.getMaxStars());
        podminky.add(star);

        Term stag = new Term();
        stag.setType("STag");
        stag.setValue(STags.getText());
        podminky.add(stag);
        
        for (TermPanel panel : panely) {
          podminky.add(panel.getPodminka());
        }
        
        if (Repeat.getText() != null && Repeat.getText().matches("[-+]?\\d*\\.?\\d+")) {
          RS.setRepead(Integer.parseInt(Repeat.getText()));
        } else {
          Repeat.setText(Integer.toString(RS.getRepead()));
        }
        RS.setPodm(podminky);
        gui.showInfo();
      }
    });

    MyButton remove = new MyButton("Odebrat");
    remove.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (gui.getSpanel().getRlist().getModel().getSize() > 2) {
          ((DefaultListModel) gui.getSpanel().getRlist().getModel()).removeElement(RS);
          gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
          gui.getSpanel().getRlist().setSelectedIndex(0);
          gui.getSpanel().getRlist().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
      }
    });

    MyButton hide = new MyButton("<") {

      @Override
      protected void paintComponent(Graphics g) {
        g.setColor(getForeground());
        g.fillRect(0, 0, AppSettings.getInt("Border_Size") / 2, getHeight());

        int r = AppSettings.getInt("Border_Size");
        g.fillPolygon(new int[]{
          (int) (getWidth() / 2 + r),
          (int) (getWidth() / 2 - r),
          (int) (getWidth() / 2 + r)
        }, new int[]{
          (int) (getHeight() / 2 - r * 1.5),
          (int) (getHeight() / 2),
          (int) (getHeight() / 2 + r * 1.5)
        }, 3);
      }
    };
    hide.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        gui.showInfo();
      }
    });

    JLabel opakovat = new JLabel("Opakovat:");
    JLabel LSTag = new JLabel("STagy:");
    JLabel Nadpis = new JLabel("Podmínky pro výběr skladby:");

    opakovat.setFont(f);
    LSTag.setFont(f);
    Nadpis.setFont(f.deriveFont(1, 2 * AppSettings.getInt("Font_Size")));

    opakovat.setForeground(AppSettings.getColour("FG_Color"));
    LSTag.setForeground(AppSettings.getColour("FG_Color"));
    Nadpis.setForeground(AppSettings.getColour("FG_Color"));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(Nadpis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(remove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(apply, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(opakovat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Repeat, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LSTag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(STags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(stars, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(panely.get(0), 810, javax.swing.GroupLayout.DEFAULT_SIZE, 810)
              .addComponent(panely.get(1), 810, javax.swing.GroupLayout.DEFAULT_SIZE, 810)
              .addComponent(panely.get(2), 810, javax.swing.GroupLayout.DEFAULT_SIZE, 810)
              .addComponent(panely.get(3), 810, javax.swing.GroupLayout.DEFAULT_SIZE, 810)
              .addComponent(panely.get(4), 810, javax.swing.GroupLayout.DEFAULT_SIZE, 810))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(hide, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
      )
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(hide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(Nadpis, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(opakovat)
          .addComponent(Repeat, 40, 40, 50)
          .addComponent(LSTag)
          .addComponent(STags, 40, 40, 50)
          .addComponent(stars, 40, 40, 50))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panely.get(0), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panely.get(1), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panely.get(2), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panely.get(3), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(panely.get(4), javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(apply, 40, 50, 64)
          .addComponent(remove, 40, 50, 64))
        .addContainerGap())
    );

    revalidate();
    repaint();
    stars.repaint();
  }

  public void setPodminky(ArrayList<Term> podminky) {
    for (TermPanel panel : panely) {
      panel.setPodminka(new Term());
      panel.setOstatni(podminky);
    }
    int i = 0;
    for (Term podminka : podminky) {
      if (podminka != null && !(podminka.getType().equals("Stars") || podminka.getType().equals("STag"))) {
        panely.get(i).setPodminka(podminka);
        i++;
      } else if (podminka != null && podminka.getType().equals("Stars")) {
        stars.setStars(podminka.getVal() * stars.getMaxStars());
      } else if (podminka != null && podminka.getType().equals("STag")) {
        STags.setText(podminka.getValue());
      }
    }
  }

  public void setRS(RandomSong RS) {
    this.RS = RS;
    setPodminky(RS.getPodm());
    Repeat.setText(Integer.toString(RS.getRepead()));
  }
}
