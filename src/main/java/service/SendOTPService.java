package service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public class SendOTPService {
    public static void sendOTP(String email, String genOTP) {
        try {
            // Load mail properties from config file
            Properties props = new Properties();
            InputStream input = SendOTPService.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(input);

            String host = props.getProperty("mail.smtp.host");
            String port = props.getProperty("mail.smtp.port");
            String ssl = props.getProperty("mail.smtp.ssl.enable");
            String auth = props.getProperty("mail.smtp.auth");
            String from = props.getProperty("mail.username");
            String password = props.getProperty("mail.password");

            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.ssl.enable", ssl);
            properties.put("mail.smtp.auth", auth);

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            session.setDebug(true);

            // Create message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("File Enc ka OTP");
            message.setText("Your One time Password for File Enc app is " + genOTP);

            // Send
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
