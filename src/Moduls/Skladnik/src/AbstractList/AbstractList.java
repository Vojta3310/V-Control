package AbstractList;

/**
 *
 * @author hp
 * @param <T>
 */
public class AbstractList<T> implements IAbstractList<T> {

    private Prvek<T> prvni = null;
    private Prvek<T> posledni = null;
    private int pocet = 0;

    @Override
    public void zrus() {
        prvni = null;
        posledni = null;
        pocet = 0;
    }

    @Override
    public int getPocet() {
        return pocet;
    }

    @Override
    public boolean jePrazdny() {
        return (prvni == null);
    }
    
    @Override
    public boolean obsahuje(T obj) {
        boolean obsahuje = false;
        IIterator<T> iter = vytvorIterator();
        while (iter.hasNext()){
            if (iter.getNext() == obj){
                obsahuje = true;
            }
        }
        return obsahuje;
    }

    private void prvniVlozeni(T obj) {
        Prvek pom = new Prvek(obj, null);
        pocet++;
        prvni = pom;
        prvni.nasledujici = prvni;
        posledni = prvni;
    }

    private Prvek vloz(T obj, Prvek nasledujici) {
        Prvek pom = new Prvek(obj, nasledujici);
        pocet++;
        return pom;
    }

    @Override
    public void vlozPrvni(T obj) {
        if (jePrazdny()) {
            prvniVlozeni(obj);
        } else {
            Prvek pom = vloz(obj, prvni);
            posledni.nasledujici = prvni;
            prvni = pom;
        }
    }

    @Override
    public void vlozPosledni(T obj) {
        if (jePrazdny()) {
            prvniVlozeni(obj);
        } else {
            Prvek pom = vloz(obj, prvni);
            posledni.nasledujici = pom;
            posledni = pom;
        }
    }

    @Override
    public T zpristupniPrvni() {
        if (prvni != null) {
            return prvni.data;
        }
        return null;
    }

    @Override
    public T zpristupniPosledni() {
        if (posledni != null) {
            return posledni.data;
        }
        return null;
    }

    @Override
    public T odeberPrvni() {
        Prvek<T> smazany = prvni;
        if (jePrazdny()) {
            return null;
        }
        if (prvni == posledni) {
            zrus();
            return smazany.data;
        } else {
            prvni = prvni.nasledujici;
            posledni.nasledujici = prvni;
            pocet--;
            return smazany.data;
        }
    }

    @Override
    public T odeberPosledni() {
        Prvek<T> smazany = posledni;
        if (jePrazdny()) {
            return null;
        }
        if (prvni == posledni) {
            zrus();
            return smazany.data;
        } else {
            Prvek<T> p = najdiPredchudce(posledni);
            p.nasledujici = prvni;
            posledni = p;
            pocet--;
            return smazany.data;
        }
    }

    private Prvek<T> najdiPredchudce(Prvek<T> prvek) {
        Prvek<T> p = prvek;
        while (p.nasledujici != prvek) {
            p = p.nasledujici;
        }
        return p;
    }

    private class Prvek<T> {

        T data;
        Prvek<T> nasledujici;

        public Prvek(T data, Prvek<T> nasledujici) {
            this.data = data;
            this.nasledujici = nasledujici;
        }
    }

    @Override
    public IIterator<T> vytvorIterator() {

        return new Iterator();
    }

    public class Iterator implements IIterator<T> {

        Prvek<T> aktualni = null;
        Prvek<T> pom = null;
        private int i = 0;

        @Override
        public boolean hasNext() {
            return (i < pocet);
        }

        @Override
        public T getNext() {

            if (hasNext() == false) {
                return null;
            }
            i++;
            if (aktualni == null) {
                aktualni = prvni;
                return prvni.data;
            }
            aktualni = aktualni.nasledujici;
            return aktualni.data;
        }

        @Override
        public void vynulujIterator() {
            aktualni = null;
            i = 0;
        }

        @Override
        public T zpristupniAktualni() {
            if (aktualni != null) {
                return aktualni.data;
            }
            return null;
        }
        
        @Override
        public void prepisAktualni(T box){
            if (aktualni != null) {
                aktualni.data = box;
            }
        }

        @Override
        public T zpristupniNaslednika() {
            return aktualni.nasledujici.data;
        }

        @Override
        public T odeberAktualni() {
            if (aktualni != null) {
                return odeberNaIndexu(aktualni);
            }
            return null;
        }

        @Override
        public T odeberNaslednika() {
            if (aktualni != null) {
                return odeberNaIndexu(aktualni.nasledujici);
            }
            return null;
        }

        private T odeberNaIndexu(Prvek<T> prvek) {
            if (jePrazdny()) {
                return null;
            }
            i--;
            if (prvek.nasledujici == prvek) {
                zrus();
                aktualni = null;
            } else if (prvek == prvni) {
                odeberPrvni();
                aktualni = posledni;
            } else if (prvek == posledni) {
                odeberPosledni();
                aktualni = posledni;
            } else {
                Prvek<T> po = najdiPredchudce(prvek);
                po.nasledujici = po.nasledujici.nasledujici;
                aktualni = po;
                pocet--;
            }
            return prvek.data;
        }
        
        
        @Override
        public int vratPolohu(){
            return i;
        }

        @Override
        public void vlozNaslednika(T obj) {
            if (jePrazdny()) {
                prvniVlozeni(obj);
                aktualni = prvni;
            } else {
                Prvek p = vloz(obj, aktualni.nasledujici);
                aktualni.nasledujici = p;
                if (aktualni == posledni) {
                    posledni = p;
                }
                aktualni = p;
            }
        }
    }
}
