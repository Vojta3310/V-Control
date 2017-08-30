package Moduls.Skladnik.DataStructure;

import javax.swing.DefaultListModel;

/**
 *
 * @author Ondřej Bleha
 */
public class ModelService {
    private final DefaultListModel<Box> model;
    
    public ModelService(){
        this.model = new DefaultListModel();
    }
    
    public void vlozBox(Box data){
      model.addElement(data);
    }
    
    public Box vyndejID(int id){
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).getID() == id){
                return model.remove(i);
            }
        }
        return null;
    }
    
    public Box vyndejBox(Box box){
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i) == box){
                return model.remove(i);
            }
        }
        return null;
    }
    
    public boolean containBox(Box box){
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i) == box){
                return true;
            }
        }
        return false;
    }
    
    public Box vyndejObsah(String obsah){
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).getObsah().equals(obsah)){
                return model.remove(i);
            }
        }
        return null;
    }
    
    public DefaultListModel<Box> getListModel(){
        return model;
    }
    
    public void reModel(String text){
        
    }
    
}
