package Moduls.Skladnik.DataStructure;

import Moduls.Skladnik.Enums.typOperace;


/**
 *
 * @author Ondřej Bleha
 */
public class Box {
    private int id; //ciselny identifikator boxu
    private int vytazeno; //kolikrat byl dany box vytazeny
    private String obsah; //jaky je obsah boxu
    private String pojmy; //synonyma obsahu
    private int kategorie; //do jake nejuzzi kategorie box spada
    private typOperace stav; //jestli je box ve fronte na vytazeni nebo vlozeni do skladu
    
    public Box(String obsah, int kategorie, String pojmy, int id, int vytazeno) {
        this.id = id;
        this.vytazeno = vytazeno;
        this.obsah = obsah;
        this.kategorie = kategorie;
        this.pojmy = pojmy;
        this.stav = typOperace.NIC;
    }
    
    public void zvysVytazeno(){
        vytazeno++;
    }
    
    public int getVytazeno(){
        return vytazeno;
    }
    
    public String getInfo() {
        return "|"+obsah+pojmy;
    }
        
    public int getID(){
        return id;
    }
    
    public int getKategorie() {
        return kategorie;
    }
    
    public String getPojmy() {
        return pojmy;
    }
    
    public String getObsah() {
        return obsah;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    public typOperace getStav(){
        return stav;
    }
    
    public void setStav(typOperace stav){
        this.stav = stav;
    }
    
    public void setKategorie(int kategorie) {
        this.kategorie = kategorie;
    }
    
    public void setPojmy(String pojmy) {
        this.pojmy = pojmy;
    }
    
    public void setObsah(String obsah) {
        this.obsah = obsah;
    }
    
    @Override
    public String toString() {
        //return String.format("<html><h3>%s</h3>ID: %s<html>", obsah, id);
        return String.format("<html><h3>%s</h3><html>", obsah);
    }

}
