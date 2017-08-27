package ui.CMD;

import DataStructure.ISklad;
import DataStructure.ModelService;
import skladnik.Robot;

/**
 *
 * @author Ondřej Bleha
 */
public class CMD {
    private final ISklad sklad;
    private final ModelService vyndane;
    private final Robot rob;
    
    public CMD(ISklad sklad, Robot rob){
        this.sklad = sklad;
        this.vyndane = sklad.getVyndane();
        this.rob = rob;
        
        System.out.println("Tato funkce není dostupná.");
    }
}
