/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import VControl.Settings.AppSettings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author vojta3310
 */
public class MyTreeCellRenderer implements TreeCellRenderer {

  private final CustomLabel renderer;

  public MyTreeCellRenderer() {
    renderer = new CustomLabel();
  }

  /**
   * Returns custom renderer for each cell of the list.
   *
   * @param tree
   * @param selected
   * @param value cell value (CustomData object in our case)
   * @param expanded
   * @param leaf
   * @param row
   * @param hasFocus
   * @return custom renderer for each cell of the list
   */
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    renderer.setSelected(selected);
    renderer.setData(value);
    renderer.setExpandet(expanded);
    renderer.setLeaf(leaf);
    return renderer;
  }

  /**
   * Label that has some custom decorations.
   */
  private static class CustomLabel extends JLabel {

    private final int GAP = AppSettings.getInt("Border_Size");
    private boolean selected;
    private boolean expandet;
    private boolean leaf;
    private Object data;
    private Graphics2D g2d;

    private void setSelected(boolean selected) {
      this.selected = selected;
    }

    private void setData(Object data) {
      this.data = data;
    }

    public void setExpandet(boolean expandet) {
      this.expandet = expandet;
    }

    public void setLeaf(boolean leaf) {
      this.leaf = leaf;
    }

    @Override
    protected void paintComponent(Graphics g) {
      this.g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setBackground(AppSettings.getColour("BG_Color"));
      g2d.setColor(AppSettings.getColour("FG_Color"));

      g2d.setFont(new Font(AppSettings.getString("Font_Name"), 0, AppSettings.getInt("Font_Size")));
      final FontMetrics fm = g2d.getFontMetrics();
      String s = data.toString();
      if (fm.stringWidth(s) > getWidth() - 4 * GAP) {
        while (fm.stringWidth(s) + fm.stringWidth("...") > getWidth() - 4 * GAP && !s.equals("")) {
          s = s.substring(0, Math.max(s.length() - 1, 0));
        }
        s = s + "...";
      }
      if (!leaf) {

        g2d.setColor(new Color(184, 207, 229));
        g2d.fillRect(3, (getHeight() - (AppSettings.getInt("Font_Size") - 6)) / 2+1, AppSettings.getInt("Font_Size") - 6, AppSettings.getInt("Font_Size") - 6);
        g2d.setColor(AppSettings.getColour("BG_Color"));
        g2d.fillRect(5, (getHeight() - (AppSettings.getInt("Font_Size") - 10)) / 2+1, AppSettings.getInt("Font_Size") - 10, AppSettings.getInt("Font_Size") - 10);
        g2d.setColor(new Color(184, 207, 229));

        g2d.fillRect(6, getHeight()/2 , AppSettings.getInt("Font_Size") - 12, 2);
        if (!expandet) {
         g2d.fillRect(5+(AppSettings.getInt("Font_Size") - 10)/2 - 1, GAP+7, 2, AppSettings.getInt("Font_Size") - 12);
        }

        g2d.setColor(AppSettings.getColour("FG_Color"));
        g2d.drawString(s, AppSettings.getInt("Font_Size"), (int) (GAP / 2 + fm.getHeight() - 5));
        if (selected) {
          g2d.fillRect(AppSettings.getInt("Font_Size"), getHeight() - GAP / 2, fm.stringWidth(s), GAP / 2);
        }
      } else {
        g2d.drawString(s, GAP, (int) (GAP / 2 + fm.getHeight() - 5));
        if (selected) {
          g2d.fillRect(GAP, getHeight() - GAP / 2, fm.stringWidth(s), GAP / 2);
        }
      }
    }

    @Override
    public Dimension getPreferredSize() {
      final Dimension ps = super.getPreferredSize();
      ps.width = 250;
      ps.height = (int) (AppSettings.getInt("Font_Size") + 2 * GAP);
//      if (g2d != null) {
//        g2d.setFont(new Font(AppSettings.getString("Font_Name"), 0, AppSettings.getInt("Font_Size")));
//        final FontMetrics fm = g2d.getFontMetrics();
//        String s = data.toString();
//        ps.width = Math.max(fm.stringWidth(s) + 2 * GAP, 10);
//      }
      return ps;
    }
  }

}
