package DataStructure;
import AbstractList.AbstractList;
import AbstractList.IAbstractList;
import AbstractList.IIterator;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import utilities.Settings;

/**
 *
 * @author Ondřej Bleha
 */
public class Sklad implements ISklad{
    private int SIRKA;
    private int VYSKA;
    private int HLOUBKA;
    private final IAbstractList<IAbstractList> sloupce = new AbstractList<>();
    private IAbstractList<IAbstractList> radek;
    private IAbstractList<Box> hloubka;
    private IAbstractList<IAbstractList> a;
    
    private final Kategorie kategorie;
    private final ModelService vyndane;
    private final ModelService sklad;
    
    
    public Sklad(int SIRKA, int VYSKA, int HLOUBKA, DefaultTreeModel model, ModelService vyndane) {
        this.SIRKA = SIRKA;
        this.VYSKA = VYSKA;
        this.HLOUBKA = HLOUBKA;
        this.kategorie = new Kategorie(model);
        this.vyndane = vyndane;
        this.sklad = new ModelService();
        
        vytvorSklad();
    }
    
    @Override
    public DefaultTreeModel getTreeModel(){
        return kategorie.getModel();
    }
    
    @Override
    public DefaultMutableTreeNode getTreeRoot(){
        return kategorie.getRoot();
    }
    
    @Override
    public Kategorie getKategorie(){
        return kategorie;
    }
    
    @Override
    public ModelService getVyndane(){
        return vyndane;
    }
    
    @Override
    public DefaultListModel<Box> getModel(){
        return sklad.getListModel();
    }
    
    private void vytvorSklad(){
        for (int i = 0; i < this.SIRKA; i++) {
            sloupce.vlozPosledni(new AbstractList());
            for (int j = 0; j < this.VYSKA; j++) {
                sloupce.zpristupniPosledni().vlozPosledni(new AbstractList());
                for (int k = 0; k < this.HLOUBKA; k++) {
                    a = sloupce.zpristupniPosledni();
                    a.zpristupniPosledni().vlozPosledni(null);
                }
            }
        }
        //iter2 = a.zpristupniPosledni().vytvorIterator();
    }
    
    /**
     *
     * @param sloupec
     * @param radek
     * @return
     */
    @Override
    synchronized public Box odeberBox(int sloupec, int radek) {
        IIterator<AbstractList> ra = setRadek(setSloupec(sloupec), radek);
        IIterator<Box> hl;
        Box box;
        if (ra != null){
            hl = ra.zpristupniAktualni().vytvorIterator();
            hl.getNext();
            box = hl.odeberAktualni();
            sklad.vyndejBox(box);
            ra.zpristupniAktualni().vlozPosledni(null);
            if (box != null){
                return box;
            }else{
                System.err.println("Nelze ze skladu odebrat nulový box!");
                System.exit(0);
            }
        }else{
            System.err.println("Nelze ze skladu odebrat box z důvodu: Nulový iterátor!");
            System.exit(0);
        }
        return null;
    }
    
    /**
     *
     * @param sloupec
     * @param radek
     * @param box
     */
    @Override
    synchronized public void pridejBox(int sloupec, int radek, Box box) {
        IIterator<AbstractList> sl = setSloupec(sloupec);
        IIterator<AbstractList> ra = setRadek(sl, radek);
        if (ra != null){
            if (box != null){
                hloubka = ra.zpristupniAktualni();
                if (hloubka.zpristupniPosledni() == null){
                    hloubka.odeberPosledni();
                    hloubka.vlozPrvni(box);
                    sklad.vlozBox(box);
                }else{
                    System.out.println("Není místo!");
                }
            }else{
                System.err.println("Nelze do skladu vložit nulový box!");
                System.exit(0);
            }
        }else{
            System.err.println("Nelze do skladu vložit box z důvodu: Nulový iterátor!");
            System.exit(0);
        }
    }
    
    private IIterator<AbstractList> setSloupec(int sloupec){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        if(0 < sloupec && sloupec <= sloupce.getPocet()){
            while (sloupec != 0){
                sloupec--;
                sl.getNext();
            }
        }else{
            System.err.println("Argument funkce setSloupec není ve vymezené hodnotě! - "+sloupec);
            System.exit(0);
        }
        return sl;
    }
    
    private IIterator<AbstractList> setRadek(IIterator<AbstractList> sl, int radek){
        IIterator<AbstractList> ra = null;
        if (sl != null){
            if(0 < radek && radek <= sl.zpristupniAktualni().getPocet()){
                ra = sl.zpristupniAktualni().vytvorIterator();
                while (radek != 0){
                    radek--;
                    ra.getNext();
                }
            }else{
                System.err.println("Argument funkce setRadek není ve vymezené hodnotě!");
                System.exit(0);
            }
        }
        return ra;
    }
    
    private IIterator<Box> setHloubka(IIterator<AbstractList> ra, int hloubka){
        IIterator<Box> hl = null;
        if(0 < hloubka && hloubka <= ra.zpristupniAktualni().getPocet()){
            hl = ra.zpristupniAktualni().vytvorIterator();
            while (hloubka != 0){
                hloubka--;
                hl.getNext();
            }
        }else{
            System.err.println("Argument funkce setHloubka není ve vymezené hodnotě!");
            System.exit(0);
        }
        return hl;
    }
    
    /**
     *
     * @param sloupec
     * @param radek
     * @param hloubka
     * @return
     */
    @Override
    synchronized public Box getBox(int sloupec, int radek, int hloubka){
        IIterator<Box> hl = setHloubka(setRadek(setSloupec(sloupec), radek), hloubka);
        if (hl != null){
            return hl.zpristupniAktualni();
        }else{
            System.err.print("Nullový iterátor!");
            System.exit(0);
        }
        return null;
    }
    
    @Override
    synchronized public void setBox(int sloupec, int radek, int hloubka, Box box){
        IIterator<Box> hl = setHloubka(setRadek(setSloupec(sloupec), radek), hloubka);
        if (hl != null){
            hl.prepisAktualni(box);
            sklad.vlozBox(box);
        }else{
            System.err.print("Nullový iterátor!");
            System.exit(0);
        }
    }
    
    @Override
    synchronized public int[] najdiBox(Box box){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        IIterator<AbstractList> ra;
        IIterator<Box> hl;
        int[] souradnice = new int[3];
        while (sl.hasNext()){
            radek = sl.getNext();
            ra = radek.vytvorIterator();
            while (ra.hasNext()){
                hloubka = ra.getNext();
                hl = hloubka.vytvorIterator();
                while (hl.hasNext()){
                    if (hl.getNext() == box){
                        souradnice[0] = sl.vratPolohu();
                        souradnice[1] = ra.vratPolohu();
                        souradnice[2] = hl.vratPolohu();
                    }
                }
            }
        }
        if (souradnice[0]*souradnice[1]*souradnice[2] == 0){
            System.err.println("Box s obsahem "+box.getObsah()+" nenalezen!");
            System.exit(0);
        }
        return souradnice;
    }
    
    /**
     *
     * @param sloupec
     * @param radek
     * @return
     */
    @Override
    synchronized public int getZaplneno(int sloupec, int radek){
        IIterator<AbstractList> ra = setRadek(setSloupec(sloupec), radek);
        IIterator<Box> hl; //v nastavovani se nepouziva
        if (ra != null){
            hl = ra.zpristupniAktualni().vytvorIterator();
            int zaplneno = 0;
            while (hl.hasNext()){
                if (hl.getNext() != null){
                    zaplneno++;
                }
            }
            return zaplneno;
        }else{
            System.err.print("Nullový iterátor!");
            System.exit(0);
        }
        return 0;        
    }
    
    synchronized private IAbstractList<IVzdalenost> getVzdalenosti(int do_r, int do_s){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        IIterator<AbstractList> ra;
        AbstractList<IVzdalenost> pozice = new AbstractList<>();
        while (sl.hasNext()){
            radek = sl.getNext();
            ra = radek.vytvorIterator();            
            while (ra.hasNext()){
                ra.getNext();
                if ((Settings.X_vydej != sl.vratPolohu() || Settings.Y_vydej != ra.vratPolohu()) &&
                    (sl.vratPolohu() != do_s || ra.vratPolohu() != do_r) &&
                    getZaplneno(sl.vratPolohu(),ra.vratPolohu()) < HLOUBKA){                    
                        pozice.vlozPosledni(new Vzdalenost(sl.vratPolohu(), ra.vratPolohu(), do_s, do_r));
                    }
                }
            }
        System.out.println("Počet pozic: "+pozice.getPocet());
        return pozice;
    }
    
    synchronized private IVzdalenost vyberNejmensi(IAbstractList<IVzdalenost> vzdalenosti){
        IVzdalenost nejmensi = null;
        IIterator<IVzdalenost> iter = vzdalenosti.vytvorIterator();
        while (iter.hasNext()){
            if (nejmensi == null){
                nejmensi = iter.getNext();
            }else if (iter.getNext().getVzdalenost() < nejmensi.getVzdalenost()){
                nejmensi = iter.zpristupniAktualni();
            }
        }
        if (nejmensi != null){
            return nejmensi;
        }else{
            System.err.println("Ve skladu není žádné volné místo!");
            System.exit(0);
        }
        return null;
    }
    
    @Override
    synchronized public IVzdalenost nejblizsiVolne(int sloupec, int radek){
        return vyberNejmensi(getVzdalenosti(radek, sloupec));        
    }
    
    @Override
    synchronized public void nastavSirku(int kolik){
        if (kolik > 0){
            if (kolik < SIRKA){
                odeberSloupec(SIRKA - kolik);
            }else if (kolik > SIRKA){
                pridejSloupec(kolik - SIRKA);
            }
        }else{
            System.err.println("Vyska skladu musi byt vetsi nez 0!");
        }
    }
    
    @Override
    synchronized public void nastavVysku(int kolik){
        if (kolik > 0){
            if (kolik < VYSKA){
                odeberRadek(VYSKA - kolik);
            }else if (kolik > VYSKA){
                pridejRadek(kolik - VYSKA);
            }
        }else{
            System.err.println("Vyska skladu musi byt vetsi nez 0!");
        }
    }
    
    @Override
    synchronized public void nastavHloubku(int kolik){
        if (kolik > 0){
            if (kolik < HLOUBKA){
                odeberHloubku(HLOUBKA - kolik);
            }else if (kolik > HLOUBKA){
                pridejHloubku(kolik - HLOUBKA);
            }
        }else{
            System.err.println("Vyska skladu musi byt vetsi nez 0!");
        }
    }
    
    synchronized private void pridejSloupec(int kolik){
        for (int i = 0; i < kolik; i++) {
            sloupce.vlozPosledni(new AbstractList());
            for (int j = 0; j < this.VYSKA; j++) {
                    sloupce.zpristupniPosledni().vlozPosledni(new AbstractList());
                    for (int k = 0; k < this.HLOUBKA; k++) {
                        a = sloupce.zpristupniPosledni();
                        a.zpristupniPosledni().vlozPosledni(null);
                    }
                }
            SIRKA++;
        }
    }
    
    synchronized private void pridejRadek(int kolik){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        for (int i = 0; i < kolik; i++) {
        while (sl.hasNext()) {
            sl.getNext().vlozPosledni(new AbstractList()); //vlozeni noveho radku uvnitr sloupce
            a = sl.zpristupniAktualni(); //do a se ulozi aktualni sloupec
                for (int k = 0; k < this.HLOUBKA; k++) {                    
                    a.zpristupniPosledni().vlozPosledni(null);
                }            
        }
        VYSKA++;
        }
    }
    
    synchronized private void pridejHloubku(int kolik){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        IIterator<AbstractList> ra;
        for (int i = 0; i < kolik; i++) {
        while (sl.hasNext()) {
            a = sl.getNext(); //dany sloupec
            ra = a.vytvorIterator(); //iterator prochazejici radky v danem sloupci
            while (ra.hasNext()) {
                ra.getNext().vlozPosledni(null);
            }
        }
        HLOUBKA++;
        }
    }
    
    synchronized private void odeberSloupec(int kolik){
        for (int i = 0; i < kolik; i++) {
            sloupce.odeberPosledni();
            SIRKA--;
        }
    }
    
    synchronized private void odeberRadek(int kolik){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        for (int i = 0; i < kolik; i++) {
            while (sl.hasNext()) {
                sl.getNext().odeberPosledni(); //odebrani posledniho z daneho sloupec
            }
            VYSKA--;
        }
    }
    
    synchronized private void odeberHloubku(int kolik){
        IIterator<AbstractList> sl = sloupce.vytvorIterator();
        IIterator<AbstractList> ra;
        for (int i = 0; i < kolik; i++) {
            while (sl.hasNext()) {
                a = sl.getNext(); //dany sloupec
                ra = a.vytvorIterator(); //iterator prochazejici radky v danem sloupci
                while (ra.hasNext()) {
                    ra.getNext().odeberPosledni();            
                }
            }
            HLOUBKA--;
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    synchronized public int getSIRKA() {
        return SIRKA;
    }
    
    /**
     *
     * @return
     */
    @Override
    synchronized public int getVYSKA() {
        return VYSKA;
    }
    
    /**
     *
     * @return
     */
    @Override
    synchronized public int getHLOUBKA() {
        return HLOUBKA;
    }
    
    
}
