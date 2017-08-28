package Moduls.Skladnik.io.SerialPort;

/**
 *
 * @author Ondřej Bleha
 */
public interface ISerialManager {

  void send(String text);

  int connect(String portName);

  public boolean Ping();

  }
