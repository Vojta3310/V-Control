package Moduls.Skladnik.ui.graphics.CellRenderers;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.Enums.typOperace;
import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ModulCellRenderer extends JLabel implements ListCellRenderer {

  protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
  private final CustomLabel renderer;

  public ModulCellRenderer(final JList list) {
    renderer = new CustomLabel();
  }

  /**
   * Returns custom renderer for each cell of the list.
   *
   * @param list list to process
   * @param value cell value (CustomData object in our case)
   * @param index cell index
   * @param isSelected whether cell is selected or not
   * @param cellHasFocus whether cell has focus or not
   * @return custom renderer for each cell of the list
   */
  @Override
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    renderer.setSelected(isSelected);
    renderer.setData((Box) value);
    return renderer;
  }

  /**
   * Label that has some custom decorations.
   */
  private static class CustomLabel extends JLabel {

    private static final Color backgroundColor = AppSettings.getColour("BG_Color");
    private final int GAP = 10;

    private boolean selected;
    private Box data;

    public CustomLabel() {
      super();
      setOpaque(false);
      setBorder(BorderFactory.createEmptyBorder(0, 36 + 5, 0, 40));
    }

    private void setSelected(boolean selected) {
      this.selected = selected;
      setForeground(selected ? Color.WHITE : Color.BLACK);
    }

    private void setData(Box data) {
      this.data = data;
      //setText ( this.data.toString());
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      Color color;
      if (data.getStav() == typOperace.NIC) {
        color = AppSettings.getColour("FG_Color");
      } else {
        color = Color.GRAY;
      }

      int s = 3;
      if (selected) {
        s = 5;
      }
//            60X200
      RoundRectangle2D x = new RoundRectangle2D.Double(GAP / 2, GAP / 2, getWidth() - GAP, getHeight() - GAP, 6, 6);

      g2d.setColor(color);
      g2d.fill(x); //vykresli vybrany ctverec
      RoundRectangle2D y = new RoundRectangle2D.Double(GAP / 2 + s, GAP / 2 + s, getWidth() - GAP - 2 * s, getHeight() - GAP - 2 * s, 6 - s, 6 - s);
      g2d.setPaint(backgroundColor);
      g2d.fill(y); //vyrizne ve vybranem ctverci bili ramec

      String text = data.getObsah();
      Font Font = new Font(AppSettings.getString("Font_Name"), 0, (int) 2 * AppSettings.getInt("Font_Size"));
      g2d.setFont(Font);
      FontMetrics fm = g2d.getFontMetrics();
      while ((fm.stringWidth(text) > (getWidth() - GAP - 2 * s)) || (fm.getHeight() > (getHeight() - GAP - 2 * s))) {
        g2d.setFont(Font.deriveFont(g2d.getFont().getSize() - 3f));
        fm = g2d.getFontMetrics();
      }

      g2d.setColor(color);
      g2d.drawString(text, ((getWidth() - GAP - 2 * s) - fm.stringWidth(text)) / 2 + GAP / 2 + s, getHeight() - ((getHeight() - GAP - 2 * s) - fm.getHeight()) / 2 - GAP / 2-
 2*s);
      super.paintComponent(g);
    }

    public void paintString(String text, int x, int y, Color c, Graphics2D g2d) {
      final Font oldFont = g2d.getFont();
      g2d.setFont(oldFont.deriveFont(oldFont.getSize() + 3f));
      final FontMetrics fm = g2d.getFontMetrics();
      g2d.setPaint(c);
      g2d.drawString(text, x + GAP - fm.stringWidth(text) / 2, y); //vykresli obsah boxu
      g2d.setFont(oldFont);
    }

    @Override
    public Dimension getPreferredSize() {
      final Dimension ps = super.getPreferredSize();
      ps.height = 36;
      return ps;
    }
  }
}
