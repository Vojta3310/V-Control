package VControl.UI.components;

import VControl.Settings.AppSettings;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    renderer.setData( value);
    return renderer;
  }

  /**
   * Label that has some custom decorations.
   */
  private static class CustomLabel extends JLabel {

    private final int GAP = AppSettings.getInt("Border_Size");
    private boolean selected;
    private Object data;

    private void setSelected(boolean selected) {
      this.selected = selected;
    }

    private void setData(Object data) {
      this.data = data;
    }

    @Override
    protected void paintComponent(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setBackground(AppSettings.getColour("BG_Color"));
      g2d.setColor(AppSettings.getColour("FG_Color"));
      if (selected) {
        g2d.fillRect(2 * GAP, getHeight() - GAP / 2, getWidth() - 4 * GAP, GAP / 2);
      }

      g2d.setFont(new Font(AppSettings.getString("Font_Name"), 0, AppSettings.getInt("Font_Size")));
      final FontMetrics fm = g2d.getFontMetrics();
      String s = data.toString();
      if (fm.stringWidth(s) > getWidth() - 4 * GAP) {
        while (fm.stringWidth(s) + fm.stringWidth("...") > getWidth() - 4 * GAP) {
          s = s.substring(0, s.length() - 1);
        }
        s = s + "...";
      }
      g2d.drawString(s, 2 * GAP, (int) (GAP / 2 + fm.getHeight() - 5));
    }

    @Override
    public Dimension getPreferredSize() {
      final Dimension ps = super.getPreferredSize();
      ps.height = (int) (AppSettings.getInt("Font_Size") + 2 * GAP);
      ps.width = 200;
      return ps;
    }
  }
}
