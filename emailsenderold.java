import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class emailsender {
    String To = "";
    String From = "stevenfrida@gmail.com";
    Properties properties = System.getProperties();
    Session session;
    MimeMessage message;

    public emailsender(String a) {
        To = a;
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.from", From);
        //properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("s041327@gmail.com", "kieann4280");
                    }
                });

        message = new MimeMessage(session);

    }

    public void content(String text) {
        try {
            Calendar calendar = Calendar.getInstance();
            //message.setFrom(new InternetAddress(From));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(To));
            message.setSubject((calendar.get(Calendar.MONTH) + 1) + "月薪水條");
            message.setText(text);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


    }

    public void send() {
        try {
            System.out.println("Message sending....");
            Transport.send(message);
            System.out.println("Successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
