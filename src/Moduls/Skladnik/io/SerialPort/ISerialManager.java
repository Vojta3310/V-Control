package Moduls.Skladnik.io.SerialPort;

/**
 *
 * @author Ondřej Bleha
 */
public interface ISerialManager {
    
    boolean send(String text);
    
    int connect(String portName);
    
}
