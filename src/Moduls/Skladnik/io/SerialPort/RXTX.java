package Moduls.Skladnik.io.SerialPort;

import Moduls.Skladnik.utilities.Settings;

/**
 *
 * @author Ondřej Bleha
 */
public class RXTX {

  private ISerialManager port;
  private int d;

  public RXTX() {
    //port.connect("/dev/ttyUSB0");
  }

  public int connect() {
//    port = new SerialManager();
//    if (port.connect(Settings.port) == -1) {
      port = new FakeIO();
      return -1;
//    }
//    return 0;
  }

  public boolean Ping() {
    return port.Ping();
  }

  public void DisableMot() {
    port.send("MD");
  }

  public void EnableMot() {
    port.send("ME");
  }

  public void pojedXY(int X, int Y, int roztec) {
    char zapX = '+';
    char zapY = '+';
    String x;
    String y;
    int jeduX;
    int jeduY;

    //System.out.println("v boxech x:"+X+" y: "+Y);
    if (X < 0) {
      zapX = '-';
      X = -X;
    }
    if (Y < 0) {
      zapY = '-';
      Y = -Y;
    }
    X = (int) ((X * Settings.X_boxu + roztec) * Settings.krokXY);
    Y = (int) (Y * Settings.Y_boxu * Settings.krokXY);
    System.out.println("vkrocixch x:" + X + " y: " + Y);

    while ((X > 0) || (Y > 0)) {
      if (X > Short.MAX_VALUE) {
        jeduX = Short.MAX_VALUE;
        X -= Short.MAX_VALUE;
      } else {
        jeduX = X;
        X = 0;
      }
      if (Y > Short.MAX_VALUE) {
        jeduY = Short.MAX_VALUE;
        Y -= Short.MAX_VALUE;
      } else {
        jeduY = Y;
        Y = 0;
      }
      System.out.println("vkrocixch po castech x:" + jeduX + " y: " + jeduY);
      x = toascii(jeduX);
      y = toascii(jeduY);
      System.out.println("po castech x:" + x + " y: " + y);
      if (jeduX == 0) {
        port.send("Y" + zapY + y);
      } else if (jeduY == 0) {
        port.send("X" + zapX + x);
      } else {
        port.send("X" + zapX + x + "Y" + zapY + y);
      }
    }
  }

  public void pojedZ(int Z, int jak) {
    char zapZ = '+';
    String z;
    int jeduZ;

    Z = (int) (Z * Settings.Z_boxu);

    switch (jak) {
      case 1:
        Z += Settings.Z_prejeti;
        break;
      case 2:
        Z -= Settings.Z_prejeti;
        break;
      case 3:
        Z += 2 * Settings.Z_prejeti;
        break;
    }

    Z = (int) (Z * Settings.krokZ);

    if (Z < 0) {
      zapZ = '-';
      Z = -Z;
    }

    while (Z > 0) {
      if (Z > Short.MAX_VALUE) {
        jeduZ = Short.MAX_VALUE;
        Z -= Short.MAX_VALUE;
      } else {
        jeduZ = Z;
        Z = 0;
      }
      //System.out.println("vkrocixch po castech z:"+ jeduZ);
      z = toascii(jeduZ);
      port.send("Z" + zapZ + z);
    }
  }

  public void pojedD(int D) {
    D = D - d;
    d = d + D;

    char zapD = '+';
    String dout;
    int jeduD;

    if (D < 0) {
      zapD = '-';
      D = -D;
    }
    D = (int) (D * Settings.krokD);
    while (D > 0) {
      if (D > Short.MAX_VALUE) {
        jeduD = Short.MAX_VALUE;
        D -= Short.MAX_VALUE;
      } else {
        jeduD = D;
        D = 0;
      }
      //  System.out.println("v krocich, po castech d:"+ jeduD);
      dout = toascii(jeduD);
      port.send("D" + zapD + dout);
    }
  }

  public void chytni() {
    pojedD(Settings.chyceno);
  }

  public void pust() {
    pojedD(Settings.pusteno);
  }

  public void close() {
    pojedD(Settings.zavreno + 1);
    port.send("KD");
    d = Settings.zavreno;
  }

  public void refAll() {
    port.send("KZ");
    System.out.println("Z zreferováno");
    port.send("KX");
    System.out.println("X zreferováno");
    port.send("KY");
    System.out.println("Y zreferováno");
    d = Settings.chyceno;
    pust();
    port.send("KD");
    System.out.println("Y zreferováno");
    d = Settings.zavreno;
    port.send("X+" + toascii((int) (Settings.X_odsazeni_refBox * Settings.krokXY)) + "Y+" + toascii((int) (Settings.Y_odsazeni_refBox * Settings.krokXY)));
    port.send("Z+" + toascii((int) (Settings.Z_odsazeni_refBox * Settings.krokZ)));

  }

  public void ref(String osa) {
    port.send("K" + osa);
    if (osa.equals("X")) {
      port.send("X+" + toascii((int) (Settings.X_odsazeni_refBox * Settings.krokXY)));
    }
    if (osa.equals("Y")) {
      port.send("Y+" + toascii((int) (Settings.Y_odsazeni_refBox * Settings.krokXY)));
    }
    if (osa.equals("Z")) {
      port.send("Z+" + toascii((int) (Settings.X_odsazeni_refBox * Settings.krokZ)));
    }
    System.out.println(osa + " zreferováno");
  }

  private String toascii(int cislo) { //cislo je pocet kroku o kolik ma robot popojet
    String str = "";
    str += (char) Math.floor(cislo / 255);
    str += (char) (cislo % 255);
//        String pom_vstup = String.valueOf(cislo);
//        short vstup = Short.valueOf(pom_vstup); //-32 768 až 32 767
//        
//        String s = Integer.toString(vstup,2);
//        String str = "";
//        
//        while(s.length()<16){
//            s="0"+s;
//        }
//        
//            short a = Short.parseShort(s.substring(0,8),2);
//            str += (char)(a);
//            a = Short.parseShort(s.substring(9,16),2);
//            str += (char)(a);
//        
    return str;
  }

}
