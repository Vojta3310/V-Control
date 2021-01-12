/*
 * Created by Vojta3310
 * If you want to use mi code, keep here this header, pleas!
 * Thanks Vojta3310.
 */
package Moduls.Mailer;

import Moduls.*;
import VControl.Command;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

/**
 *
 * @author vojta3310
 */
public class Mailer extends Modul {

  public Mailer(VControl.Commander Commander) {
    super(Commander);
  }

  @Override
  public String GetModulName() {
    return "Mailer";
  }

  @Override
  public boolean HaveGUI() {
    return false;
  }

  @Override
  public void getDefaultSettings(Properties p) {
    p.setProperty("Modul_" + this.GetModulName() + "_Host", "imap.seznam.cz");
    p.setProperty("Modul_" + this.GetModulName() + "_Email", "***@seznam.cz");
    p.setProperty("Modul_" + this.GetModulName() + "_Password", "******");
  }

  @Override
  public void Execute(Command co) {
    if (co.GetCommand().equals("SMail")) {
      try {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        Store store = session.getStore("imaps");
        store.connect(SgetString("Host"), SgetString("Email"), SgetString("Password"));
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);
        // search for all "unseen" messages
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        Message messages[] = inbox.search(unseenFlagTerm);
        int num=messages.length;
        inbox.close(true);
        store.close();
        //say number of messages
        if (num == 0) {
          getCommander().Execute(new Command("Say", "Nemáš žádný nepřečtený ímejl.", "Speaker", GetModulName()));
        }else if (num < 5) {
          getCommander().Execute(new Command("Say", "Máš "+num+" nepřečtené ímely.", "Speaker", GetModulName()));
        }else{
          getCommander().Execute(new Command("Say", "Máš "+num+" nepřečtených ímelů.", "Speaker", GetModulName()));
        }
        //c.vaitForDone();
      } catch (NoSuchProviderException ex) {
        Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
      } catch (MessagingException ex) {
        Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void StartModule() {
    try {
      MailService mm = new MailService();
      mm.login(SgetString("Host"), SgetString("Email"), SgetString("Password"));
      int lastMessages = mm.getMessageCount();
      while (mm.isLoggedIn()) {
        if (lastMessages != mm.getMessageCount()) {
          getCommander().Execute(new Command("Say", "Přišel ti ímejl.", "Speaker", GetModulName()));
          lastMessages = mm.getMessageCount();
        }
        Thread.sleep(10000);
      }
    } catch (Exception ex) {
      Logger.getLogger(Mailer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
