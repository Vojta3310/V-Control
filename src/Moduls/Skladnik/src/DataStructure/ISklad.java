package DataStructure;

//import AbstractList.IAbstractList;

import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 *
 * @author Ondřej Bleha
 */
public interface ISklad {
    
    DefaultTreeModel getTreeModel();
    
    DefaultMutableTreeNode getTreeRoot();
    
    Kategorie getKategorie();
    
    ModelService getVyndane();
    
    DefaultListModel<Box> getModel();

    Box getBox(int sloupec, int radek, int hloubka);
    
    void setBox(int sloupec, int radek, int hloubka, Box box);
    
    //IAbstractList<Box> vratList();
    
    int[] najdiBox(Box box);
    
    Box odeberBox(int sloupec, int radek);
    
    void pridejBox(int sloupec, int radek, Box box);
    
    IVzdalenost nejblizsiVolne(int sloupec, int radek);
    
    int getZaplneno(int sloupec, int radek);    
    
    void nastavSirku(int kolik);
    
    void nastavVysku(int kolik);
            
    void nastavHloubku(int kolik);
    
    int getSIRKA();
    
    int getVYSKA();
    
    int getHLOUBKA();
}
