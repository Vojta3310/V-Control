package DataStructure;

import javax.swing.DefaultListModel;

/**
 *
 * @author Ondřej Bleha
 * @param <T>
 */
public class Buffer <T>{
    
    private final DefaultListModel<T> model;
    
    public Buffer(){
        model = new DefaultListModel();
    }

    public boolean jeVeFronte(T t) {
        return model.contains(t);
    }
    
    public T OdeberPrvek(T t) {
        return model.remove(model.indexOf(t));
    }

    public int getPocet() {
        return model.getSize();
    }

    public void Vloz(T data) {
        model.addElement(data);
    }

    public T Odeber() {
        return model.remove(0);
    }

    public boolean jePrazdny() {
        return  model.getSize() == 0;
    }

    public T Zpristupni(){
        return model.getElementAt(0);
    }
    
    public T zpristupniPrvek(int poradi) {
        return model.getElementAt(poradi);
    }

    public DefaultListModel getListModel() {
        return model;
    }
    
}
