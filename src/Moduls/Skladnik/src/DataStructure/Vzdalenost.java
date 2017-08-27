package DataStructure;

import DataStructure.IVzdalenost;

/**
 *
 * @author Ondřej Bleha
 */
public class Vzdalenost implements IVzdalenost{
    private final int sloupec;
    private final int radek;
    private final int vzdalenost;
    
    public Vzdalenost(int sloupec, int radek, int do_s, int do_r){
        this.sloupec = sloupec;
        this.radek = radek;
        this.vzdalenost = vypoctiVzdalenost(do_s, do_r);
    }
    
    private int vypoctiVzdalenost(int sloupec, int radek){
        int vzdalenost1 = this.sloupec - sloupec;
        int vzdalenost2 = this.radek - radek;
        if (vzdalenost1 < 0){
            vzdalenost1 *= -1;
        }
        if (vzdalenost2 < 0){
            vzdalenost2 *= -1;
        }
        return vzdalenost1 + vzdalenost2; //((this.sloupec-sloupec)*(-1))+((this.radek-radek)*(-1));
    }
    
    @Override
    public int getSloupec(){
        return sloupec;
    }
    
    @Override
    public int getRadek(){
        return radek;
    }
    
    @Override
    public int getVzdalenost(){
        return vzdalenost;
    }
}
