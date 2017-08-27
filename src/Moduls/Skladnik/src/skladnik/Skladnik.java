package skladnik;

import utilities.Settings;
import io.SerialPort.RXTX;
import ui.CMD.CMD;
import ui.graphics.GUI;
import DataStructure.ISklad;
import io.xml.XML;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import ui.graphics.Splash;

/**
 *
 * @author Ondřej Bleha
 */
public class Skladnik {
    private static Splash splash;
    private static RXTX rxtx;
    private static Robot rob;
    private static ISklad sklad;
    private static XML xml;
    
    /**
     * @param args the command line arguments
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws javax.swing.UnsupportedLookAndFeelException
     */
    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("com.sun.java.swing.plaf.gtk.GTKLookAndFeel".equals(info.getClassName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            } 
        }
        
        splash = new Splash();
        
        splash.setStatus("Načítám XML...");
        xml = new XML(Settings.file);        
        sklad = xml.read();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Skladnik.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rxtx = new RXTX();
        if(rxtx.connect() == -1){
            splash.error("Nebyl nalezen seriový port!");
        }
        rob = new Robot(sklad, rxtx);
        
        splash.setStatus("Provádím referenci skladu...");
        rob.reference();
        
        splash.dispose();
        if (args.length == 1) {
            if (args[0].contains("nogui")) {
                CMD cmd = new CMD(sklad, rob);
            }
        }else{
            GUI gui = new GUI(sklad, rob, xml);
        }
        rob.setDaemon(true);
        rob.start();
       
    }
}
