package ui.graphics;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import skladnik.Robot;

/**
 *
 * @author Ondřej Bleha
 */
public class RobPosUpdater implements Runnable{
    private final JProgressBar progressBar;
    private final JLabel label;
    private final Robot rob;
    
    public RobPosUpdater(JProgressBar progressBar, JLabel label, Robot rob){
        this.progressBar = progressBar;
        this.label = label;
        this.rob = rob;
        
        //spusteni noveho Threadu
	Thread th = new Thread(this);
	th.start();
    }

    @Override
    public void run() {
        while(true){
            this.label.setText("X: "+rob.getX()+" | Y: "+rob.getY());
            progressBar.setIndeterminate(rob.isPracuje());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(RobPosUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
