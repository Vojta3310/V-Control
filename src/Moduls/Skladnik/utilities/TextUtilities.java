package utilities;

import java.text.Normalizer;

/**
 *
 * @author Ondřej Bleha
 */
public class TextUtilities {

    /**
     * Zbavi text vsech zbytecnosti, jako hacky, carky a velka pismena.
     * 
     * @param text
     * @return
     * 
     */
    public static String normalize(String text) {
    return text == null ? null :
    Normalizer.normalize(text, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "")
        .toLowerCase();
    }
    
}
