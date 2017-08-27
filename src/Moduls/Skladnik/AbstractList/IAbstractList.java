package Moduls.Skladnik.AbstractList;
//import java.util.Iterator;

/**
 *
 * @author hp
 * @param <T>
 */
public interface IAbstractList<T> {

    void zrus();

    boolean jePrazdny();
    
    boolean obsahuje(T obj);

    void vlozPrvni(T obj);

    void vlozPosledni(T obj);

    T zpristupniPrvni();

    T zpristupniPosledni();

    T odeberPrvni();

    T odeberPosledni();

    public int getPocet();

    IIterator vytvorIterator();
}
