package Moduls.Skladnik.ui.graphics.CellRenderers;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.Enums.typOperace;
import Moduls.Skladnik.ui.graphics.IGUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

public class BufferCellRenderer extends JLabel implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    private final ImageIcon crossIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/cross.png"));
    private final ImageIcon downIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/down.png"));
    private final ImageIcon upIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/up.png"));
    private final ImageIcon doubleUpIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/double_arrow_16px.png"));
    private final ImageIcon plusIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/vlozit.png"));
    private final ImageIcon minusIcon = new ImageIcon(getClass().getResource("/icons/modules/Skladnik/buffer/podat.png"));
    private final CustomLabel renderer;
    
    public BufferCellRenderer (final JList list, final IGUI gui){
        renderer = new CustomLabel();

        list.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseReleased ( MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    int index = list.locationToIndex ( e.getPoint () );
                    if ( index != -1 && list.isSelectedIndex ( index ) )
                    {
                        Rectangle rect = list.getCellBounds ( index, index ); //obdelnik reprezentujici bunku
                        Point pointWithinCell = new Point ( e.getX () - rect.x, e.getY () - rect.y ); //souradnice kliknuti v bunce
                        Rectangle crossRect = new Rectangle ( rect.width - 9 - 15 - 21*0 - crossIcon.getIconWidth () / 2,
                                rect.height / 2 - crossIcon.getIconHeight () / 2, crossIcon.getIconWidth (), crossIcon.getIconHeight () ); //ctverec krizku                        
                        Rectangle downRect = new Rectangle ( rect.width - 9 - 15 - 21*1 - crossIcon.getIconWidth () / 2,
                                rect.height / 2 - crossIcon.getIconHeight () / 2, crossIcon.getIconWidth (), crossIcon.getIconHeight () ); //ctverec sipky nahoru
                        Rectangle upRect = new Rectangle ( rect.width - 9 - 15 - 21*2 - crossIcon.getIconWidth () / 2,
                                rect.height / 2 - crossIcon.getIconHeight () / 2, crossIcon.getIconWidth (), crossIcon.getIconHeight () ); //ctverec sipky nahoru
                        Rectangle doubleUpRect = new Rectangle ( rect.width - 9 - 15 - 21*3 - crossIcon.getIconWidth () / 2,
                                rect.height / 2 - crossIcon.getIconHeight () / 2, crossIcon.getIconWidth (), crossIcon.getIconHeight () ); //ctverec dvojite sipky nahoru
                        
                        DefaultListModel<Box> model = ( DefaultListModel ) list.getModel ();
                        
                        if (crossRect.contains(pointWithinCell)){                            
                            Box box = model.remove(index);
                            box.setStav(typOperace.NIC);
                            gui.nacti();
                            list.setSelectedIndex(index);                            
                        }else if (downRect.contains(pointWithinCell) && index+1 < model.getSize()){
                            model.add(index+1, model.remove(index));
                            list.setSelectedIndex(index+1);
                        }else if (upRect.contains(pointWithinCell) && index != 0){
                            model.add(index-1, model.remove(index));
                            list.setSelectedIndex(index-1);
                        }else if (doubleUpRect.contains(pointWithinCell) && index != 0){
                            model.add(0, model.remove(index));
                            list.setSelectedIndex(0);
                        }
                    }
                }
            }
        } );
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
    public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        renderer.setSelected ( isSelected );
        renderer.setData ( ( Box ) value );
        return renderer;
    }



/**
     * Label that has some custom decorations.
     */
    private class CustomLabel extends JLabel
    {
        private final Color selectionColor = Color.DARK_GRAY;//new Color ( 82, 158, 202 );
        private final Color backgroundColor = Color.WHITE;
        private final int GAP = 5;

        private boolean selected;
        private Box data;

        public CustomLabel ()
        {
            super ();
            setOpaque ( false );
            setBorder ( BorderFactory.createEmptyBorder ( 0, 36 + 5, 0, 40 ) );
        }

        private void setSelected ( boolean selected )
        {
            this.selected = selected;
            setForeground ( selected ? Color.WHITE : Color.BLACK );
        }

        private void setData ( Box data )
        {
            this.data = data; 
            //setText ( this.data.toString());
        }

        @Override
        protected void paintComponent ( Graphics g )
        {
            Graphics2D g2d = ( Graphics2D ) g;
            g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            
            Color color = selectionColor;//=selected ? Color.DARK_GRAY.brighter() : Color.DARK_GRAY;
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
            
            if ( selected )
            {
                g2d.drawImage ( crossIcon.getImage (), getWidth () - 9 - 15 - 21*0 - crossIcon.getIconWidth () / 2,
                        getHeight () / 2 - crossIcon.getIconHeight () / 2, null );
                g2d.drawImage ( downIcon.getImage (), getWidth () - 9 - 15 - 21*1 - downIcon.getIconWidth () / 2,
                        getHeight () / 2 - downIcon.getIconHeight () / 2, null );
                g2d.drawImage ( upIcon.getImage (), getWidth () - 9 - 15 - 21*2 - upIcon.getIconWidth () / 2,
                        getHeight () / 2 - upIcon.getIconHeight () / 2, null );
                g2d.drawImage ( doubleUpIcon.getImage (), getWidth () - 9 - 15 - 21*3 - doubleUpIcon.getIconWidth () / 2,
                        getHeight () / 2 - doubleUpIcon.getIconHeight () / 2, null );
            }
            
            if(data.getStav() == typOperace.PODEJ){
                g2d.drawImage (minusIcon.getImage(), 80 - minusIcon.getIconWidth () / 2 -posun,
                        getHeight () / 2 - minusIcon.getIconHeight () / 2 -posun, null );
            }else if(data.getStav() == typOperace.VLOZ){
                g2d.drawImage (plusIcon.getImage(), 80 - plusIcon.getIconWidth () / 2 -posun,
                        getHeight () / 2 - plusIcon.getIconHeight () / 2 -posun, null );
            }

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