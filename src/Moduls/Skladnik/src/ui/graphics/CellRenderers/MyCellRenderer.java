package ui.graphics.CellRenderers;
import DataStructure.Box;
import Enums.typOperace;
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
import java.awt.image.ColorModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class MyCellRenderer extends JLabel implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    private final CustomLabel renderer;
    
    public MyCellRenderer ( final JList list ){
        renderer = new CustomLabel();
    }
    
    /**
     * Returns custom renderer for each cell of the list.
     *
     * @param list         list to process
     * @param value        cell value (CustomData object in our case)
     * @param index        cell index
     * @param isSelected   whether cell is selected or not
     * @param cellHasFocus whether cell has focus or not
     * @return custom renderer for each cell of the list
     */
    @Override
    public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ){
        renderer.setSelected ( isSelected );
        renderer.setData ( ( Box ) value );
        return renderer;
    }

    /**
     * Label that has some custom decorations.
     */
    private static class CustomLabel extends JLabel{
        private static final Color backgroundColor = Color.WHITE;
        private final int GAP = 5;

        private boolean selected;
        private Box data;

        public CustomLabel(){
            super ();
            setOpaque (false);
            setBorder (BorderFactory.createEmptyBorder (0, 36 + 5, 0, 40));
        }

        private void setSelected(boolean selected){
            this.selected = selected;
            setForeground (selected ? Color.WHITE : Color.BLACK);
        }

        private void setData(Box data){
            this.data = data; 
            //setText ( this.data.toString());
        }

        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2d = ( Graphics2D ) g;
            g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            
            Color color;
            if (data.getStav() == typOperace.NIC){
              color = Color.DARK_GRAY;
            }else{
              color = Color.GRAY;
            }
            
            int posun = -1;
            
            //lepsi stin
            if (selected){              
              Color barva;
              for (int i = 1; i < 5; i++) {
                RoundRectangle2D sh = new RoundRectangle2D.Double ( 18+GAP+5+i, 3+4+3+i, getWidth () - 18-GAP*2-2*i, getHeight() - 18-2*i, 6, 6 );
                barva= new Color (200-i*30,200-i*30,200-i*30);
                g2d.setPaint (barva);
                g2d.fill(sh); //vykresli stin
                posun = 0;                
              }
            }
            
            RoundRectangle2D x = new RoundRectangle2D.Double ( 15+GAP-posun, -posun, getWidth () - 18-GAP*2, getHeight() - 18, 6, 6 );
            g2d.setPaint (color);
            g2d.fill(x); //vykresli vybrany ctverec
            RoundRectangle2D y = new RoundRectangle2D.Double ( 17+GAP-posun, 2-posun, getWidth () - 22-GAP*2, getHeight() - 22, 6, 6 );                
            g2d.setPaint (backgroundColor);
            g2d.fill(y); //vyrizne ve vybranem ctverci bili ramec
            Rectangle z = new Rectangle(17+GAP-posun, 2-posun, getWidth () - 22-GAP*2, getHeight()/5+5);
            g2d.setPaint(color);
            g2d.fill(z); //vykresli podklad pro obsah                

            paintString(data.getObsah(), getWidth()/2-posun, 20-posun, backgroundColor, g2d);
            paintString("ID: "+data.getID(), getWidth()/2-posun, 40-posun, color, g2d);
            paintString("Vytaženo: "+data.getVytazeno(), getWidth()/2-posun, 57-posun, color, g2d);
            
            super.paintComponent ( g );
        }
        
        public void paintString(String text, int x, int y, Color c, Graphics2D g2d){
                final Font oldFont = g2d.getFont ();
                g2d.setFont ( oldFont.deriveFont ( oldFont.getSize () + 3f ) );
                final FontMetrics fm = g2d.getFontMetrics ();
                g2d.setPaint(c);
                g2d.drawString ( text, x+GAP - fm.stringWidth ( text )/2, y); //vykresli obsah boxu
                g2d.setFont ( oldFont );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            final Dimension ps = super.getPreferredSize ();
            ps.height = 36;
            return ps;
        }
    }
}