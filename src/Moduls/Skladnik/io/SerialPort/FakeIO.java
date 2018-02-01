package Moduls.Skladnik.io.SerialPort;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ondřej Bleha
 */
public class FakeIO implements ISerialManager {

  private boolean locked;

  public FakeIO() {
    this.locked = false;
  }

  @Override
  public boolean Ping() {
    Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Like Ping");
    return true;
  }

  @Override
  synchronized public void send(String text) {
    while (locked) {
      cekej(100);
    }
    locked = true;
    Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Jakoby odesláno: " + text);
    cekej(500);
    Logger.getLogger(this.getClass().getName()).log(Level.FINE, "Jakoby přijato OK");
    locked = false;
  }

  private void cekej(int cas) {
    try {
      Thread.sleep(cas); //reprezentuje odesílání příkazu (spí 0.1 sekundy)
    } catch (InterruptedException ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Nepovedlo se uspat vlákno!");
    }
  }

  @Override
  public int connect(String portName) {
    return 0;
  }
}
