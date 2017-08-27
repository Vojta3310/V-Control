package ui.graphics.listeners;

import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Ondřej Bleha
 */
public class BufferListener implements ListDataListener {
    private final JList sklad;
    private final JList vyndane;
    
    public BufferListener(JList sklad, JList vyndane){
        this.sklad = sklad;
        this.vyndane = vyndane;
    }
    
    private void repaintLists(){
        sklad.repaint();
        vyndane.repaint();
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        repaintLists();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        repaintLists();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        //do nothing
    }
    
}
