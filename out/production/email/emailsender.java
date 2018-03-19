import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import org.apache.commons.mail.*;

public class emailsender {

    Properties properties = System.getProperties();
    Session session;
    String Password = "";
    String User = "";

    public emailsender(String server, String port, String user, String password) {

        properties.put("mail.smtp.host", server);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.from", user);
        User=user;
        Password=password;

        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

    }

    public void send(String to,String subject, String text) {
        try {
            Calendar calendar = Calendar.getInstance();
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setSubject((calendar.get(Calendar.MONTH) + 1) + "月薪水條");
            message.setSubject("test");
            message.setText(text);
            message.saveChanges();
            System.out.println("Message sending....");
            Transport.send(message);
            System.out.println("Successful");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}