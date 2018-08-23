/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Skladnik.io;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Vojta3310
 */
public class QRC {

  private String charset = "UTF-8"; // or "ISO-8859-1"
  private Webcam webcam = Webcam.getDefault();
  private int attemps = 5;

  public QRC(boolean open) {
    if (open && (webcam != null) && (!webcam.isOpen())) {
      webcam.setViewSize(WebcamResolution.QVGA.getSize());
      webcam.open();
    }
  }

  public String Scan() {
    boolean c = false;
    String out = "";
    webcam = Webcam.getDefault();
    if (webcam != null) {
//      if (!webcam.isOpen()) {
//        webcam.setViewSize(WebcamResolution.QVGA.getSize());
//        webcam.open();
//        c = true;
//      }
      int i = 0;
      while (i < attemps && out.equals("")) {
        out = read(webcam);
        i++;
      }
//      if (c) {
//        webcam.close();
//      }
    }
    return out;
  }

  public void closeCam() {
    if (webcam != null && webcam.isOpen()) {
      webcam.close();
    }
  }

  public void createPage(List<String> Texts, String file, int sX, int sY) {
    int x = sX;
    int y = sY;
    int page = 0;
    //2480 X 3508
    //7-330 X 15-220
    BufferedImage p = new BufferedImage(2310, 3400, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = p.createGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, p.getWidth(), p.getHeight());
    for (int i = 0; i < Texts.size(); i++) {
      String Text = Texts.get(i);
      if (x > 6) {
        x = 0;
        y++;
        if (y > 14) {
          y = 0;
          savePage(page, p, file);
          g.setColor(Color.white);
          g.fillRect(0, 0, p.getWidth(), p.getHeight());
          page++;
        }
      }
      Text += "#";
      BufferedImage l = createLabel(Text.split("#")[0], "#" + Text.split("#")[1]);
      AffineTransform at = new AffineTransform();
      at.translate(55 + x * 330, -55 + y * 220);
      at.rotate(Math.PI / 2, l.getWidth() / 2, l.getHeight() / 2);
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage(l, at, null);

      x++;
    }
    savePage(page, p, file);
  }

  private void savePage(int p, BufferedImage i, String file) {
    Graphics2D g2d = i.createGraphics();
    g2d.setColor(Color.black);
    g2d.fillRect(30, i.getHeight() - 80, i.getWidth() - 60, 2);
    Font font = new Font("Monospaced", 0, 30);
    FontMetrics metrics = g2d.getFontMetrics(font);
    g2d.setFont(font);
    int x = 100;
    int y = i.getHeight() - 50 - metrics.getHeight() / 2 + metrics.getAscent();
    Date dNow = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy ' ' hh:mm:ss");
    g2d.drawString(ft.format(dNow), x, y);
    x = i.getWidth() - 100 - metrics.stringWidth(Integer.toHexString(p)) / 2;
    g2d.drawString(Integer.toString(p), x, y);
    File outputfile = new File(file + "[" + Integer.toString(p) + "].png");
    try {
      ImageIO.write(i, "png", outputfile);
    } catch (IOException ex) {
      Logger.getLogger(QRC.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public BufferedImage createLabel(String Text1, String Text2) {
    BufferedImage im = new BufferedImage(220, 330, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g2d = im.createGraphics();
    g2d.setColor(Color.white);
    g2d.fillRect(0, 0, 220, 330);

    g2d.drawImage(createQRCode(Text1 + Text2, charset, 164, 164), 28, 28, null);
    g2d.setColor(Color.black);
    g2d.fillRect(18, 18, 50, 5);
    g2d.fillRect(18, 18, 5, 50);

    g2d.fillRect(152, 18, 50, 5);
    g2d.fillRect(197, 18, 5, 50);

    g2d.fillRect(152, 197, 50, 5);
    g2d.fillRect(197, 152, 5, 50);

    g2d.fillRect(18, 197, 50, 5);
    g2d.fillRect(18, 152, 5, 50);

    Font font = new Font("Monospaced", 0, 30);
    FontMetrics metrics = g2d.getFontMetrics(font);
    g2d.setFont(font);
    int x = 00 + (220 - metrics.stringWidth(Text1)) / 2;
    int y = 220 + ((110 - metrics.getHeight()) / 2) + metrics.getAscent() - metrics.getHeight() / 2;
    g2d.drawString(Text1, x, y);
    x = 00 + (220 - metrics.stringWidth(Text2)) / 2;
    y = 220 + ((110 - metrics.getHeight()) / 2) + metrics.getAscent() + metrics.getHeight() / 2;
    g2d.drawString(Text2, x, y);

    g2d.fillRect(0, 0, 50, 2);
    g2d.fillRect(0, 0, 2, 50);

    g2d.fillRect(170, 0, 50, 2);
    g2d.fillRect(218, 0, 2, 50);

    g2d.fillRect(0, 328, 50, 2);
    g2d.fillRect(0, 280, 2, 50);

    g2d.fillRect(170, 328, 50, 2);
    g2d.fillRect(219, 280, 2, 50);

    return im;
  }

  public static BufferedImage createQRCode(String qrCodeData,
    String charset, int qrCodeheight, int qrCodewidth) {
    try {
      Map hintMap = new HashMap();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      BitMatrix matrix = new MultiFormatWriter().encode(
        new String(qrCodeData.getBytes(charset), charset),
        BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
      return MatrixToImageWriter.toBufferedImage(matrix);
    } catch (WriterException | UnsupportedEncodingException ex) {
      Logger.getLogger(QRC.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new BufferedImage(qrCodewidth, qrCodeheight, BufferedImage.TYPE_3BYTE_BGR);
  }

  public static String readQRCode(String filePath, String charset, Map hintMap)
    throws FileNotFoundException, IOException, NotFoundException {
    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
      new BufferedImageLuminanceSource(
        ImageIO.read(new FileInputStream(filePath)))));
    Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
      hintMap);
    return qrCodeResult.getText();
  }

  private static BinaryBitmap toBinaryBitmap(BufferedImage image) {
    return new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
  }

  public static String read(Webcam webcam) {
    BufferedImage image;
    Result result;

    if (!webcam.isOpen()) {
      return "";
    }
    if ((image = webcam.getImage()) == null) {
      return "";
    }

    try {
      result = new MultiFormatReader().decode(toBinaryBitmap(image));
    } catch (NotFoundException ex) {
      return "";
    }

    if (result != null) {
      return result.getText();
    }
    return "";
  }

  public List<Webcam> getWebcams() {
    return Webcam.getWebcams();
  }

  public Webcam getWebcam() {
    return webcam;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public void setWebcam(Webcam webcam) {
    this.webcam = webcam;
  }

  public void setAttempts(int attempts) {
    this.attemps = attempts;
  }

  public boolean isCam() {
    return webcam != null;
  }
}
