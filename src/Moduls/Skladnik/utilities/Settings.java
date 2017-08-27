package utilities;

/**
 *
 * @author Ondřej Bleha  & Vojta3310
 */
public class Settings {
    //rozmery (roztece) boxu [mm]
    public static int Y_boxu = 145;
    public static int X_boxu = 100; //98;116
    public static int Z_boxu = 37;
    public static int R_navic = 5;
    //rozměry chapadel [mm]
    public static int pusteno = 123;  //tak aby box propadl
    public static int chyceno = 112;  //tak aby box drzel
    public static int zavreno = 106;  //zavřeno az ke koncaku
    //souradnice vydejniho mista
    public static int X_vydej = 1;
    public static int Y_vydej = 1;
    public static int Z_vydej = 3;
    //prepocty mm na kroky (pocet kroku na 1mm)
    public static double krokXY = 4608/477;
    public static double krokZ = 6400/167;
    public static double krokD = 2560/30.5;
    //odsazeni pocatku souradnic boxu od absolutniho pocatku [mm]
    public static int X_odsazeni_refBox = 10;
    public static int Y_odsazeni_refBox = 5;
    public static int Z_odsazeni_refBox = 1;
    //přejetí v ose Z aby krabicka dopravdy dosedla [mm]
    public static int Z_prejeti=9;
    //casova prodleva pred polozenim krabicky [ms]
    public static int cekani=800;
    //soubor skladu
    public static String file="home/vojta3310/Sklad.xml";
    //oznaceni comu  (COM1;/dev/ttyUSB0)
    public static String port="/dev/ttyUSB0";
    //ctít pořadí (true > když přerovnávám box který je v buferu hned ho vindám)
    public static boolean bufer_videj=true;
}
