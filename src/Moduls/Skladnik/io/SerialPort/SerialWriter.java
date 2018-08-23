package Moduls.Skladnik.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Ondřej Bleha & Vojta3310
 */
public class SerialWriter {
    OutputStream out;

    public SerialWriter(OutputStream out){
        this.out = out;
    }
    
    public void write(String text){
        System.out.println("Odesílám: " + text);
        try{
            for (int i = 0; i < text.length(); i++) {
                this.out.write(text.charAt(i));
            }
            this.out.write((char)0);
            this.out.write((char)0);
            this.out.flush();
        System.out.println("Odesláno: " + text);
        }
        catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
//            System.exit(-1);
        }
    }
    
}
