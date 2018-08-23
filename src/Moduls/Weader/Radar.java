/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Weader;

import static Moduls.Weader.MapsProjection.latToYWorld;
import static Moduls.Weader.MapsProjection.lonToXWorld;
import static Moduls.Weader.MapsProjection.worldToPixel;
import VControl.UI.components.Stars;
import VControl.utiliti;
import static VControl.utiliti.getImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author vojta3310
 */
public class Radar extends JPanel {

  private Image map;
  private Image clouds;
  private BufferedImage cloudsOriginal;
  private Image ZoomIn;
  private Image ZoomOut;
  private double centerX = 13f;
  private double centerY = 49.9f;
  private double scale = 1;
  private int zoom = 11;

  int mX = 0;
  int mY = 0;
  boolean mD = false;

  public Radar() {
    ZoomIn = null;
    ZoomOut = null;
    try {
      ZoomIn = utiliti.resize(utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Weader/zoomin.png"))), 32, 32);
      ZoomOut = utiliti.resize(utiliti.toBufferedImage(ImageIO.read(getClass().getResourceAsStream("/icons/modules/Weader/zoomout.png"))), 32, 32);
    } catch (IOException ex) {
      Logger.getLogger(Stars.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    updateImages();
    updateMap();
    updateMeteo();

    addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        mX = e.getX();
        mY = e.getY();
        if (mX > 5 && mX < 5 + 32 + 1) {
          if (mY > 5 && mY < 5 + 32 + 1) {
            if (zoom < 11) {
              zoom++;
            }
            updateMap();
            updateMeteo();
            repaint();
            revalidate();
            return;
          } else if (mY > 5 + 32 && mY < 5 + 32 + 32 + 1) {
            if (zoom > 2) {
              zoom--;
            }
            updateMap();
            updateMeteo();
            repaint();
            revalidate();
            return;
          }
        }
        mD = true;
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (mD) {
          Point a = worldToPixel(lonToXWorld(14.0), latToYWorld(50.0), zoom);
          Point b = worldToPixel(lonToXWorld(18.0), latToYWorld(49.0), zoom);
          double sx = ((double) (b.x - a.x) / (double) (481 - 195)) * scale;
          double sy = ((double) (b.y - a.y) / (double) (357 - 246)) * scale;

          centerX += (double) (mX - e.getX()) / (sx * 68.147);
          centerY += (double) (e.getY() - mY) / (sy * 110.574);

          updateMap();
          updateMeteo();
        }
        repaint();
        revalidate();
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });

    repaint();
    revalidate();
  }

  @Override
  protected void paintComponent(Graphics gr) {
    Graphics2D g = (Graphics2D) gr;
    super.paintComponent(g);
    Point p = getMeteoPosition();
    g.drawImage(map, 0, 0, this);
    g.drawImage(clouds, p.x, p.y, this);
    g.drawImage(ZoomIn, 5, 5, this);
    g.drawImage(ZoomOut, 5, 5 + 32, this);

//    g.fillOval(p.x-3, p.y-3, 6, 6);
//    System.out.println(p.x);
//    System.out.println(p.y);
  }

  public final void updateMap() {
    StringBuilder url = new StringBuilder();
    url.append("https://maps.googleapis.com/maps/api/staticmap?center=");
    url.append(Double.toString(centerY)).append(",").append(Double.toString(centerX));
    url.append("&zoom=").append(Integer.toString(zoom));
    url.append("&scale=").append(Double.toString(scale));
    url.append("&size=").append(getWidth()).append("x").append(getHeight());
    url.append("&maptype=terrain&format=png&key=AIzaSyDtQgHRpKBnlCfmlFyIKhliytfCb9tHgJY&language=cs");
    System.out.println(url.toString());
    map = getImage(url.toString());
  }

  public final void updateImages() {
    StringBuilder url = new StringBuilder();
    url.append("http://portal.chmi.cz/files/portal/docs/meteo/rad/inca-cz/data/czrad-z_max3d/pacz2gmaps3.z_max3d.20180701.2200.0.png");
//    url.append(Double.toString(centerY)).append(",").append(Double.toString(centerX));
//    url.append("&zoom=").append(Integer.toString(zoom));
//    url.append("&size=").append(getWidth()).append("x").append(getHeight());
//    url.append("&maptype=terrain&format=png&visual_refresh=true");
//    System.out.println(url.toString());
    cloudsOriginal = getImage(url.toString());
  }

  public final void updateMeteo() {
    Point p = new Point();
    Point a = worldToPixel(lonToXWorld(14.0), latToYWorld(50.0), zoom);
    Point b = worldToPixel(lonToXWorld(18.0), latToYWorld(49.0), zoom);
    Point c = worldToPixel(lonToXWorld(centerX), latToYWorld(centerY), zoom);
    double sx = ((double) (b.x - a.x) / (double) (481 - 195)) * scale;
    double sy = ((double) (b.y - a.y) / (double) (357 - 246)) * scale;
    p.x = a.x - c.x + getWidth() / 2 - (int) Math.round(195.0 * sx);
    p.y = a.y - c.y + getHeight() / 2 - (int) Math.round(246.0 * sy);

    int k = Math.min(Math.max(-p.x, 0), cloudsOriginal.getWidth());
    int l = Math.min(Math.max(-p.y, 0), cloudsOriginal.getHeight());
    int m = Math.min((int) (getWidth() / sx)-k, cloudsOriginal.getWidth() - k);
    int n = Math.min((int) (getHeight() / sy)-l, cloudsOriginal.getHeight() - l);

    if (m * n == 0) {
      return;
    }

    BufferedImage img = cloudsOriginal.getSubimage(k, l, m, n);
    clouds = utiliti.resize(img, getWidth(), getHeight());

  }

  public Point getMeteoPosition() {
    Point p = new Point();
    Point c = worldToPixel(lonToXWorld(centerX), latToYWorld(centerY), zoom);
    Point a = worldToPixel(lonToXWorld(14.0), latToYWorld(50.0), zoom);
    Point b = worldToPixel(lonToXWorld(18.0), latToYWorld(49.0), zoom);
    double sx = ((double) (b.x - a.x) / (double) (481 - 195)) * scale;
    double sy = ((double) (b.y - a.y) / (double) (357 - 246)) * scale;

    p.x = Math.max(0, a.x - c.x + getWidth() / 2 - (int) Math.round(195.0 * sx));
    p.y = Math.max(0, a.y - c.y + getHeight() / 2 - (int) Math.round(246.0 * sy));

    return p;
  }

}
