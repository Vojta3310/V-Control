package Moduls.MyPlayerMusic;

import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyCellRenderer extends JLabel implements ListCellRenderer {

  protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
  private final CustomLabel renderer;

  public MyCellRenderer(final JList list) {
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
    renderer.setData((Skladba) value);
    return renderer;
  }

  /**
   * Label that has some custom decorations.
   */
  private static class CustomLabel extends JLabel {

    private static final Color backgroundColor = Color.WHITE;
    private final int GAP = AppSettings.getInt("Border_Size");

    private boolean selected;
    private Skladba data;

    public CustomLabel() {
      super();
      setOpaque(false);
      setBorder(BorderFactory.createEmptyBorder(0, 36 + 5, 0, 40));
    }

    private void setSelected(boolean selected) {
      this.selected = selected;
//      setForeground(selected ? Color.WHITE : Color.BLACK);
    }

    private void setData(Skladba data) {
      this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setBackground(AppSettings.getColour("BG_Color"));
      paintString(data.getTitle()+"-"+data.getAutor(), 0, getHeight()/2, AppSettings.getColour("FG_Color"), g2d);

      if (selected) {
        g2d.fillRect(0, getHeight()-GAP, getWidth(), GAP);
      }

      super.paintComponent(g);
    }

    public void paintString(String text, int x, int y, Color c, Graphics2D g2d) {
      final Font oldFont = g2d.getFont();
      g2d.setFont(oldFont.deriveFont(oldFont.getSize() + 3f));
      final FontMetrics fm = g2d.getFontMetrics();
      g2d.setPaint(c);
      g2d.drawString(text, x + GAP - fm.stringWidth(text) / 2, y-fm.getHeight()); //vykresli obsah boxu
      g2d.setFont(oldFont);
    }

    @Override
    public Dimension getPreferredSize() {
      final Dimension ps = super.getPreferredSize();
      ps.height = 15;
      return ps;
    }
  }
}
