package Moduls.Skladnik.AbstractList;

/**
 *
 * @author hp
 * @param <T>
 */
public interface IIterator<T> {

    boolean hasNext();

    T getNext();

    void vlozNaslednika(T obj);

    T zpristupniAktualni();

    T zpristupniNaslednika();

    T odeberAktualni();

    T odeberNaslednika();
    
    void prepisAktualni(T box);
    
    int vratPolohu();

    void vynulujIterator();

}
