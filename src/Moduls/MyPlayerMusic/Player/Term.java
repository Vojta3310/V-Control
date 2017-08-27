/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.MyPlayerMusic.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vojta3310
 */
public class Term {

  private String value = "";
  private String Type = "...";
  private String Comparation = "Odpovídá";
  private Float val = 0.0F;

  public boolean ValidSong(Skladba s) {
    if (Type.equals("...") || (value == null)) {
      return true;
    }
    String porovnani = "";
    switch (Type) {
      case "Název":
        porovnani = s.getTitle();
        break;
      case "Autor":
        porovnani = s.getAutor();
        break;
      case "Album":
        porovnani = s.getAlbum();
        break;
      case "Jazyk":
        porovnani = s.getLangue();
        break;
      case "Tagy":
        porovnani = s.getTags();
        break;
      case "STag":
        boolean ok = true;
        ArrayList<String> t = new ArrayList<>(Arrays.asList(value.split(" ")));
        for (String aSpecialTag : s.getASpecialTags()) {
          ok &= t.contains(aSpecialTag);
        }
        return true;
      case "Stars":
        return val <= s.getOblibenost();
    }
    switch (Comparation) {
      case "Odpovídá":
        return porovnani.equals(value) || value.equals("") || value.equals("...");
      case "Neodpovídá":
        return !porovnani.equals(value) || value.equals("") || value.equals("...");
      case "Obsahuje":
        return porovnani.contains(value) || value.equals("") || value.equals("...");
      case "Neobsahuje":
        return !porovnani.contains(value) || value.equals("") || value.equals("...");
      case "Začíná":
        return porovnani.startsWith(value) || value.equals("") || value.equals("...");
      case "Končí":
        return porovnani.endsWith(value) || value.equals("") || value.equals("...");
    }
    return false;
  }

  public void setVal(Float val) {
    this.val = val;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setType(String Type) {
    this.Type = Type;
  }

  public String getLabel() {
    if (Type.equals("Stars") || Type.equals("STag") || Type.equals("...")
      || value.equals("") || value.equals("...")) {
      return null;
    }
    return Type.substring(0, 3) + ".-" + value;
  }

  public void setComparation(String Comparation) {
    this.Comparation = Comparation;
  }

  public String getValue() {
    return value;
  }

  public String getType() {
    return Type;
  }

  public String getComparation() {
    return Comparation;
  }

  public Float getVal() {
    return val;
  }

}
