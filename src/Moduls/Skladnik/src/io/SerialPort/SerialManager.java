package io.SerialPort;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 *
 * @author Ondřej Bleha & Vojta3310
 */
public class SerialManager implements ISerialManager{
    private SerialPort serialPort;
    private SerialWriter writer;
    private SerialReader reader;
    
    
    public SerialManager(){
        
    }
    
    @Override
    public void send(String text){
        reader.lock();
        writer.write(text);
        while(reader.getLock()){
            try {
                Thread.sleep(100); //reprezentuje odesílání příkazu (spí 0.1 sekundy)
            } catch (InterruptedException ex) {
                System.err.println("Nepovedlo se uspat vlákno!");
            }
        }
    }
    
    @Override
    public int connect(String portName){
        CommPortIdentifier portIdentifier;

        try{
            portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            if (portIdentifier.isCurrentlyOwned()){
                System.err.println("error: port is currently in use");
                return -1;
            }

            serialPort = (SerialPort) portIdentifier.open(portName, 3000);
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream in = serialPort.getInputStream();
            OutputStream out = serialPort.getOutputStream();         

            writer = new SerialWriter(out);
            reader = new SerialReader(in);

            serialPort.addEventListener(reader);
            serialPort.notifyOnDataAvailable(true);
        }catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | IOException | TooManyListenersException e){
            return -1;
        }
        System.out.println("Serial port successfully connected!");
        return 0;       
    }
    
}
