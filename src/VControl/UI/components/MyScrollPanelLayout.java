/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package VControl.UI.components;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneLayout;

/**
 *
 * @author vojta3310
 */
public class MyScrollPanelLayout extends ScrollPaneLayout {
  
  @Override
  public void layoutContainer(Container parent) {
    super.layoutContainer(parent);
    hsb.setOrientation(JScrollBar.HORIZONTAL);
    hsb.setLocation(0, 0); //hsb je horizontální scrollbar, tady ho umístíme nahoru, defaultně se zobrazuje pokud je potřeba
    if (hsb.isVisible()) { //pokud je hsb viditelný tak musíme posunout viewport o výšku hsb dolů
      viewport.setLocation(0, hsb.getHeight()); //viewport je ta část kde se zobrazuje to co chceme scrollovat
    } else { //jinak bude viewport úplně nahoře
      viewport.setLocation(0, 0);
    }
  }
}