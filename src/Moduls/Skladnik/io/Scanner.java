/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik.io;

import Moduls.Skladnik.DataStructure.Box;
import Moduls.Skladnik.DataStructure.ISklad;

/**
 *
 * @author vojta3310
 */
public class Scanner extends Thread {

  private final ISklad s;
  private boolean isbox;
  private Box box;
  private QRC q;

  public Scanner(ISklad s) {
    this.s = s;
  }

  @Override
  public void run() {
    q = new QRC(true);
    while (true) {
      String b = q.Scan();
      isbox = !b.equals("");
      if (isbox) {
        int id = Integer.parseInt(b.substring(b.indexOf(":") + 1, b.length()));
        System.out.println("id" + id);
        box = s.getVyndane().getByID(id);
      } else {
        box = null;
      }
    }
  }

  public boolean isBox() {
    return isbox;
  }

  public Box ScanBox() {
    return box;
  }

  public boolean isCam() {
    return q.isCam();
  }
}
