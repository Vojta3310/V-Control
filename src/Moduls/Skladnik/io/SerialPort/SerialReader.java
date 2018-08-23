package Moduls.Skladnik.io.SerialPort;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ondřej Bleha & Vojta3310
 */
public class SerialReader implements SerialPortEventListener{
    private final InputStream in;
    private final int[] buffer;
    private boolean lock;

    public SerialReader(InputStream in){           
        this.buffer = new int[1024];
        this.lock = false;
        this.in = in;           
    }
    
    public void lock(){
        this.lock = true;
    }
    
    public boolean getLock(){
        return lock;
    }

    /** 
     * znaky v hodnotě 00 jako konec bloku.
     * @param ev
     */
    @Override    
    public void serialEvent(SerialPortEvent ev){
        int data;
        try{
            int len = 0;
            buffer[0]=255; 
            while ( (data = in.read()) > -1){
                len++;
                buffer[len] =  data;
                //System.out.println(data);
                if ((buffer[len-1]==0)&&(buffer[len]==0)){
                    String prijato = new String(buffer, 1, len-2);
                    System.out.println("Přijato: " + prijato);
                    len = 0;
                    if (prijato.equals("OK")){
                        this.lock = false;
                        break;
                    }
                }
            }

        }
        catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            //System.exit(-1);
        }           
    }       
}
