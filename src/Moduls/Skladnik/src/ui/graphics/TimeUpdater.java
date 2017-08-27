package ui.graphics;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author Ondřej Bleha
 */
public class TimeUpdater implements Runnable{
    
    //private JLabel, ve kterem se bude zobrazovat cas
    private final JLabel now;

    //konstruktor
    public TimeUpdater(JLabel label) {
        now = label;
        
        //spusteni noveho Threadu
	Thread th = new Thread(this);
	th.start();
    }

    //implementovana trida run
    @Override
    public void run() {
            //nekonecny cyklus pro neustalou aktualizaci casu
            while( true ){
                    //vytvoreni masky pro cas 
                    //zobrazuje se v titulku okna
                    SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
                    //vytvoreni masky pro datum a cas 
                    //zobrazuje se v panelu
                    SimpleDateFormat dates = new SimpleDateFormat("dd.M.yyyy HH:mm:ss");
                    //new date
                    Date date = new Date();
                    //v JLabelu zmenime datum a cas
                    now.setText(dateformat.format(date));
                    try {
                            //cekame sekundu
                            Thread.sleep(1000);
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
            }
    }

}
