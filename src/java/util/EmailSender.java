package util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private final String username = "cursoidealizar@gmail.com";
    private final String password = "idealizar";

    public boolean sendTo(String para, String assunto, String corpo) {

        Properties props = new Properties();
          
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(para));
            message.setSubject(assunto);
            message.setContent(corpo, "text/html; charset=utf-8");
            Transport.send(message);
            
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}