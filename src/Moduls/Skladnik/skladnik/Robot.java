package Moduls.Skladnik.skladnik;

import Moduls.Modul;
import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.Buffer;
import Moduls.Skladnik.DataStructure.ISklad;
import Moduls.Skladnik.DataStructure.IVzdalenost;
import Moduls.Skladnik.DataStructure.ModelService;
import Moduls.Skladnik.Enums.typOperace;
import Moduls.Skladnik.io.Scanner;
import Moduls.Skladnik.io.SerialPort.RXTX;
import Moduls.Skladnik.io.xml.XML;
import Moduls.Skladnik.ui.ModulUI.MGUI;
import Moduls.Skladnik.utilities.Settings;
import VControl.Command;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author Ondřej Bleha
 */
public class Robot extends Thread {

  private int x = 1;
  private int y = 1;
  private final ISklad sklad;
  private final ModelService vyndane;
  private final RXTX rxtx;
  private final JTextPane console;
  private MGUI gui;
  private final Buffer<Box> buffer;
  private Box box;
  private Box vyndany = null;
  private final XML xml;

  private boolean pracuje;
  private boolean sleep = false;
  private boolean reffered = false;
  private boolean badbox = false;
  private boolean buferVindej = Settings.bufer_videj;
  private int SleepAfter = Settings.SleepAfter;
  private final Scanner sc;
  private Modul m;

  public Robot(ISklad sklad, RXTX rxtx, XML xml) {
    this.sklad = sklad;
    this.rxtx = rxtx;
    this.xml = xml;
    this.pracuje = false;

    this.sc = new Scanner(sklad);
    this.console = new JTextPane();
    this.console.setFocusable(false);
    this.console.setEditable(false);
    this.console.setBackground(new Color(42, 42, 42));

    this.vyndane = sklad.getVyndane();
    this.buffer = new Buffer();
    m = null;
  }

  public Robot(ISklad sklad, RXTX rxtx, XML xml, boolean bufVindej, int sleepafter, Modul mod) {
    this(sklad, rxtx, xml);
    buferVindej = bufVindej;
    SleepAfter = sleepafter;
    m = mod;
  }

  @Override
  public void run() {
    this.sc.start();
    while (true) {
      if (buffer.jePrazdny()) {
        pracuje = false;
        if (rxtx.Ping()) {
          rxtx.setGled(true);
          rxtx.setRled(false);
        }
      }
      int s = 0;
      boolean u = maUklidit();
      while ((buffer.jePrazdny() && !u) || !rxtx.Ping()) {
        spi(1000);
        if (s >= SleepAfter && !sleep) {
          pracuje = true;
          reffered = false;
          if (rxtx.Ping()) {
            rxtx.EnableMot();
            sleep = true;
            posunNa(1, 1);
            rxtx.ref("Z");
            rxtx.DisableMot();
            rxtx.setGled(true);
            rxtx.setRled(false);
          }
          pracuje = false;
        }
        if (sleep && !rxtx.Ping()) {
          sleep = false;
        }
        s++;
        u = maUklidit();
      }
      pracuje = true;
      if (sleep) {
        rxtx.EnableMot();
        reference();
        reffered = true;
        sleep = false;
      }
      if (!reffered) {
        rxtx.EnableMot();
        reference();
        reffered = true;
      }

      if (u) {
        Box b = sc.ScanBox();
        if (b != null && b.getStav() == typOperace.NIC) {
          b.setStav(typOperace.VLOZ);
          vloz(b);
        }
      } else if (buffer.Zpristupni().getStav() == typOperace.PODEJ) {
        podej(buffer.Odeber());
//        System.out.println("+++++++++++++++++++++++++++++");
      } else if (buffer.Zpristupni().getStav() == typOperace.VLOZ) {
        vloz(buffer.Odeber());
      } else {
        buffer.Odeber();
      }
      gui.nacti();

      if (buffer.jePrazdny()) {
        try {
          xml.stow(sklad);
        } catch (IOException | XMLStreamException ex) {
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  public void reference() {
    rxtx.EnableMot();
    rxtx.refAll();
    x = 1;
    y = 1;
  }

  private boolean maUklidit() {
    Box namiste = sc.ScanBox();
    if (namiste == null) {
      if (sc.isBox()) {
        rxtx.setGled(true);
        rxtx.setRled(true);
        badbox = true;
      } else if (badbox) {
        badbox = false;
        rxtx.setGled(true);
        rxtx.setRled(false);
      }
      vyndany = null;
      return false;
    } else {
      if (namiste.equals(vyndany)) {
        rxtx.setGled(true);
        rxtx.setRled(false);
        return false;
      } else {
        rxtx.setGled(false);
        rxtx.setRled(true);
        return true;
      }
    }
  }

  public void setGui(MGUI gui) {
    this.gui = gui;
    this.gui.nacti();
  }

  public JTextPane getConsole() {
    return console;
  }

  public DefaultListModel<Box> getBufferListModel() {
    return buffer.getListModel();
  }

  synchronized public void addToBuffer(Box box) {
    if (!buffer.jeVeFronte(box)) {
      buffer.Vloz(box);
    }
  }

  public void posunNa(int x, int y) {
    int navic = 0;
    if (((x == 1) && (this.x > 1)) || ((this.x == 1) && (x > 1))) {
      navic = Settings.R_navic;
    }
    rxtx.pojedXY((this.x - x) * (-1), (this.y - y) * (-1), navic);
    if (x == 1) {
      rxtx.ref("X");
    }
    if (y == 1) {
      rxtx.ref("Y");
    }
    this.y += (this.y - y) * (-1);
    this.x += (this.x - x) * (-1);
  }

  private void chytni(int vyska) {
    if (box == null) {
      rxtx.close();
      rxtx.pojedZ(vyska, 0);
      rxtx.pust();
      rxtx.pojedZ(1, 2);
      spi(Settings.cekani);
      rxtx.pojedZ(0, 3);
      rxtx.chytni();
      rxtx.pojedZ(-vyska - 1, 2);
      rxtx.ref("Z");
      box = sklad.odeberBox(x, y);
    } else {
      System.err.println("Zásobník robota je plný!");
      System.exit(0);
    }
  }

  private void pust(int vyska) {
    rxtx.pojedZ(vyska + 1, 2);
    spi(Settings.cekani);
    rxtx.pojedZ(0, 3);
    rxtx.pust();
    rxtx.pojedZ(-1, 0);
    rxtx.close();
    rxtx.pojedZ(-vyska, 2);
    rxtx.ref("Z");
    sklad.pridejBox(x, y, box);
    box = null;
  }

  private void pustVyndane() {
    int i = 0;
    while (sc.isBox()) {
      rxtx.setRled(true);
      spi(500);
      rxtx.setRled(false);
      spi(500);
      i += 1;
      if (i >= 10) {
        i = 0;
        if (m != null) {
          m.getCommander().Execute(new Command("Say", "Prosím odeberte box.", "Speaker", m.GetModulName()));
        }
      }
    }
    rxtx.pojedZ(Settings.Z_vydej + 1, 2);
    spi(Settings.cekani);
    rxtx.pojedZ(0, 3);
    rxtx.pust();
    rxtx.pojedZ(-1, 0);
    rxtx.close();
    rxtx.pojedZ(-Settings.Z_vydej, 2);
    rxtx.ref("Z");
    vyndane.vlozBox(box);
    vyndany = box;
    box = null;
  }

  private void chytniVyndane(Box box) {
    checkBox(box);
    rxtx.close();
    rxtx.pojedZ(Settings.Z_vydej, 0);
    rxtx.pust();
    rxtx.pojedZ(1, 2);
    spi(Settings.cekani);
    checkBox(box);
    rxtx.pojedZ(0, 3);
    rxtx.chytni();
    rxtx.pojedZ(-Settings.Z_vydej - 1, 2);
    rxtx.ref("Z");
    this.box = vyndane.vyndejBox(box);
  }

  private void checkBox(Box b) {
    if (sc.isCam()) {
      rxtx.setGled(false);
      int i = 0;
      while (sc.ScanBox() != b) {
        rxtx.setRled(true);
        spi(500);
        rxtx.setRled(false);
        spi(500);
        i += 1;
        if (i >= 10) {
          i = 0;

          if (m != null) {
            m.getCommander().Execute(new Command("Say", "Prosím vložte správný box.", "Speaker", m.GetModulName()));
          }
        }
      }
      rxtx.setRled(true);
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getPosition() {
    return ((y - 1) * sklad.getSIRKA()) + x - 1;
  }

  public Box zpristupniBox() {
    return box;
  }

  public void vloz(Box bx) {
    rxtx.setGled(false);
    rxtx.setRled(true);
    gui.nacti();
    console.setText("");
    if (x != Settings.X_vydej || y != Settings.Y_vydej) {
      println("Robot jede na domovské souradnice. ", Color.WHITE);
      this.posunNa(Settings.X_vydej, Settings.Y_vydej);
    }
    this.chytniVyndane(bx);
    rxtx.setGled(true);
    rxtx.setRled(false);
    println("Krabicka chytnuta na domovských souřadnicích.", Color.WHITE);
    print("Obsah robota je ", Color.WHITE);
    println(box.getObsah(), Color.YELLOW);

    IVzdalenost kam = sklad.nejblizsiVolne(this.getX(), this.getY());

    print("Robot jede na nejblizsi souradnice (", Color.WHITE);
    print("x=" + kam.getSloupec() + "; ", Color.GREEN);
    print("y=" + kam.getRadek(), new Color(115, 164, 255));
    println(")", Color.WHITE);

    this.posunNa(kam.getSloupec(), kam.getRadek());
    box.setStav(typOperace.NIC);
    this.pust(sklad.getHLOUBKA() - sklad.getZaplneno(kam.getSloupec(), kam.getRadek()));

    print("krabicka pustena na: \n", Color.WHITE);
    print("x=" + x + "; ", Color.GREEN);
    println("y=" + y, new Color(115, 164, 255));

//        println("Robot jede na domovské souradnice. ", Color.WHITE);
//        this.posunNa(Settings.X_vydej , Settings.Y_vydej );
//        
//        print("Robot je doma na souradnicích ", Color.WHITE);
//        print("x="+x+"; ", Color.GREEN);
//        println("y="+y, new Color(115,164,255));
//        
//        println("\nOperace úspěšně dokončena.", Color.GREEN);
  }

  public void podej(Box bx) {
    rxtx.setGled(false);
    rxtx.setRled(true);
    gui.nacti();
    int[] souradnice = sklad.najdiBox(bx);
    //souradnice[0] XXX
    //souradnice[1] YYY
    //souradnice[2] ZZZ
    //this.console.setText("");
    print("Podávám ", Color.WHITE);
    print(bx.getObsah(), Color.YELLOW);
    println("...", Color.WHITE);

    print("Box nalezen na souřadnicích: ", Color.WHITE);
    print("x=" + souradnice[0] + "; ", Color.RED);
    print("y=" + souradnice[1] + "; ", Color.GREEN);
    println("z=" + souradnice[2] + "\n", new Color(115, 164, 255));

    print("Robot jede na souradnice boxu s obsahem: ", Color.WHITE);
    println(bx.getObsah(), Color.YELLOW);

    this.posunNa(souradnice[0], souradnice[1]);
    while (souradnice[2] != 1) {
      souradnice[2]--;
      this.chytni(1 + sklad.getHLOUBKA() - sklad.getZaplneno(souradnice[0], souradnice[1]));

      print("krabicka " + box.getObsah() + " chytnuta na: \n", Color.WHITE);
      print("x=" + x + "; ", Color.GREEN);
      println("y=" + y, new Color(115, 164, 255));

      if (buferVindej && this.buffer.jeVeFronte(box)) {
        println("Robot jede na domovské souradnice. ", Color.WHITE);
        this.posunNa(Settings.X_vydej, Settings.Y_vydej);

        print("Robot je doma na souradnicích ", Color.WHITE);
        print("x=" + x + "; ", Color.GREEN);
        println("y=" + y, new Color(115, 164, 255));

        print("Obsah robota je ", Color.WHITE);
        println(box.getObsah(), Color.YELLOW);
        box.zvysVytazeno();
        this.buffer.OdeberPrvek(box);
        box.setStav(typOperace.NIC);
        this.pustVyndane();

      } else {
        IVzdalenost kam = sklad.nejblizsiVolne(x, y);

        print("Robot jede na nejblizsi souradnice (", Color.WHITE);
        print("x=" + kam.getSloupec() + "; ", Color.GREEN);
        print("y=" + kam.getRadek(), new Color(115, 164, 255));
        println(")", Color.WHITE);

        this.posunNa(kam.getSloupec(), kam.getRadek());
        this.pust(sklad.getHLOUBKA() - sklad.getZaplneno(kam.getSloupec(), kam.getRadek()));
      }

      print("krabicka pustena na: \n", Color.WHITE);
      print("x=" + x + "; ", Color.GREEN);
      println("y=" + y, new Color(115, 164, 255));

      print("Robot jede na souradnice boxu s obsahem: ", Color.WHITE);
      println(bx.getObsah(), Color.YELLOW);

      this.posunNa(souradnice[0], souradnice[1]);
    }
    print("Souradnice robota jsou nyní: ", Color.WHITE);
    print("x=" + x + "; ", Color.GREEN);
    println("y=" + y, new Color(115, 164, 255));

    this.chytni(1 + sklad.getHLOUBKA() - sklad.getZaplneno(souradnice[0], souradnice[1]));
    box.setStav(typOperace.NIC);

    print("krabicka chytnuta na: \n", Color.WHITE);
    print("x=" + x + "; ", Color.GREEN);
    println("y=" + y, new Color(115, 164, 255));

    println("Robot jede na domovské souradnice. ", Color.WHITE);
    this.posunNa(Settings.X_vydej, Settings.Y_vydej);

    print("Robot je doma na souradnicích ", Color.WHITE);
    print("x=" + x + "; ", Color.GREEN);
    println("y=" + y, new Color(115, 164, 255));

    print("Obsah robota je ", Color.WHITE);
    println(box.getObsah(), Color.YELLOW);

    this.pustVyndane();
    bx.zvysVytazeno();
    println("\nOperace úspěšně dokončena.", Color.GREEN);
    rxtx.setGled(true);
    rxtx.setRled(false);
    if (m != null) {
      m.getCommander().Execute(new Command("Say", "Box připraven.", "Speaker", m.GetModulName()));
    }
  }

  public void print(String text, Color barva) {
    StyledDocument doc = console.getStyledDocument();
    Style style = console.addStyle("I'm a Style", null);
    StyleConstants.setForeground(style, barva);
    try {
      doc.insertString(doc.getLength(), text, style);

    } catch (BadLocationException ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "nepodarilo se vypsat text!");
    }

  }

  public void println(String text, Color barva) {
    print(text + "\n", barva);
  }

  private void spi(int time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException ex) {
      System.err.println("Nepovedlo se uspat robota!");
    }
  }

  public boolean isPracuje() {
    return pracuje;
  }
}
