package Moduls.Skladnik.io.SerialPort;

/**
 *
 * @author Ondřej Bleha
 */
public class FakeIO implements ISerialManager{
    private boolean locked;
    
    public FakeIO(){
        this.locked = false;
    }
    
    @Override
    synchronized public boolean send(String text){
        while(locked){
            cekej(100);
        }
        locked = true;
        System.out.println("Odesláno: "+text);
        cekej(500);
        System.out.println("Jakoby přijato OK");
        locked = false;
        return false;
    }
    
    private void cekej(int cas){
        try {
            Thread.sleep(cas); //reprezentuje odesílání příkazu (spí 0.1 sekundy)
        } catch (InterruptedException ex) {
            System.err.println("Nepovedlo se uspat vlákno!");
        }
    }

    @Override
    public int connect(String portName) {
        return 0;
    }
}
