import com.sun.mail.util.MailConnectException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class Emailsender {

    Properties properties = System.getProperties();
    Session session;
    String Password = "";
    String User = "";
    Transport transport;
    public static boolean SSLEnable = false;
    public static boolean TLSEnable = true;

    public Emailsender(String server, String port, String user, String password) {

        properties.put("mail.smtp.host", server);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.from", user);
        if (SSLEnable) properties.put("mail.smtp.ssl.enable", "true");
        else properties.put("mail.smtp.ssl.enable", "false");
        if (TLSEnable) properties.put("mail.smtp.starttls.enable", "true");
        else properties.put("mail.smtp.starttls.enable", "false");
        User = user;
        Password = password;
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
        try {
            transport = session.getTransport("smtp");
            Core.gui.setEnterButton("connecting.......");
            transport.connect();
            Core.gui.setEnterButton("connected");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Core.indicator.setVisible(true);
                }
            }, "IndicatorThread");
            thread.start();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MailConnectException e) {
            Core.gui.setEnterButton("smtp server settings incorrect");
            Timer timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Core.gui.setEnterButton("Enter");
                    Core.gui.setEnterButtonEnable(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
            return;
        } catch (AuthenticationFailedException e) {
            Core.gui.setEnterButton("username or password incorrect");
            Timer timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Core.gui.setEnterButton("Enter");
                    Core.gui.setEnterButtonEnable(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
            System.out.println(e.getLocalizedMessage());
            return;
        } catch (MessagingException e) {
            Core.gui.setEnterButton("unknown error");
            Timer timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Core.gui.setEnterButton("Enter");
                    Core.gui.setEnterButtonEnable(true);
                }
            });
            timer.setRepeats(false);
            timer.start();
            e.printStackTrace();
            return;
        }
    }

    public void send(String to, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(User));
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(content, "text/html; charset = UTF-8");
            message.saveChanges();
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}