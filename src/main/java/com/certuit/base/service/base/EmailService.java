package com.certuit.base.service.base;

import com.certuit.base.util.UtilFuctions;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class EmailService  {
    private JavaMailSender mailSender;

    @Getter
    @Setter
    private MimeMessage message;

    private String fromAddress;

    public EmailService(Connection jdbcConnection) throws Exception {
        mailSender = getJavaMailSender(jdbcConnection);
    }

    public JavaMailSender getJavaMailSender(Connection jdbcConnection) throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String query = "select * from SisCuentasCorreoPQ where TipoCuenta = 2";
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        JSONObject jsonObject = UtilFuctions.convertObject(rs);
        mailSender.setHost(jsonObject.getString("Servidor"));
        mailSender.setPort(jsonObject.getInt("Puerto"));
        fromAddress = jsonObject.getString("Usuario");
        mailSender.setUsername(jsonObject.getString("Usuario"));
        mailSender.setPassword(jsonObject.getString("Contrasenia"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", jsonObject.getInt("TipoCifrado") == 2 ? "true" : "false");
        props.put("mail.smtp.ssl.enable", jsonObject.getInt("TipoCifrado") == 1 ? "true" : "false");
        props.put("mail.debug", "true");

        return mailSender;
    }

    public void sendMail(final String messageBody, final String subject,
                         final String toAddress, String[] ccAddressString) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    message = mailSender.createMimeMessage();
                    InternetAddress[] ccAddress = new InternetAddress[ccAddressString.length];
                    for( int i = 0; i < ccAddressString.length; i++ ) {
                        ccAddress[i] = new InternetAddress(ccAddressString[i]);
                        message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
                    }
                    MimeMessageHelper helper = createMimeMessage(messageBody, subject, toAddress);

                } catch (MessagingException me) {
                    me.printStackTrace();
                }
                try {
                    mailSender.send(message);
                } catch (MailAuthenticationException ex) {
                    ex.printStackTrace();

                } catch (MailException me) {
                    me.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMail(final String messageBody, final String subject,
                         final String[] toAddresses, byte[] rawImage) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //printAddress(toAddresses);
                    message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = createMimeMessage(messageBody, subject, toAddresses);
                    if (rawImage.length != 0) {
                        MimeBodyPart htmlPart = new MimeBodyPart();
                        htmlPart.setContent(messageBody, "text/html; charset=UTF-8");
                        Multipart multiPart = new MimeMultipart("alternative");
                        BodyPart imagePart = new MimeBodyPart();
                        ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage,"image/png");

                        imagePart.setDataHandler(new DataHandler(imageDataSource));
                        imagePart.setHeader("Content-ID", "<logoImage>");
                        imagePart.setFileName("logo.png");

                        multiPart.addBodyPart(imagePart);
                        multiPart.addBodyPart(htmlPart);
                        message.setContent(multiPart);
                    }
                } catch (MessagingException me) {
                    me.printStackTrace();
                }
                try {
                    mailSender.send(message);
                } catch (MailAuthenticationException ex) {
                    ex.printStackTrace();

                } catch (MailException me) {
                    me.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMail(final String messageBody, final String subject,
                         final String[] toAddresses, List<File> attachments) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //printAddress(toAddresses);
                    message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = createMimeMessage(messageBody, subject, toAddresses);
                    for (File attachment : attachments) {
                        helper.addAttachment(attachment.getName(), attachment);
                        System.err.println(attachment.getName() + "sending...");

                    }
                } catch (MessagingException me) {
                    me.printStackTrace();
                }
                try {
                    mailSender.send(message);
                } catch (MailAuthenticationException ex) {
                    ex.printStackTrace();

                } catch (MailException me) {
                    me.printStackTrace();
                }
            }
        }).start();
    }

    public void printAddress(final String[] toAddresses) {
        for (String toAddress : toAddresses) {
            System.out.println("enviara correo a: " + toAddress);
        }
    }

    public MimeMessageHelper createMimeMessage(final String messageBody, final String subject,
                                               final String[] toAddresses) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromAddress);
        helper.setSubject(subject);
        helper.setTo(toAddresses);
        helper.setText(messageBody, true);
        return  helper;
    }

    public MimeMessageHelper createMimeMessage(final String messageBody, final String subject,
                                               final String toAddress) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromAddress);
        helper.setSubject(subject);
        helper.setTo(toAddress);
        helper.setText(messageBody, true);
        return  helper;
    }

    public void sendMail(final String messageBody, final String subject,
                         final String toAddress, List<File> attachments) {
        {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = createMimeMessage(messageBody, subject, toAddress);
                        // let's include the infamous windows Sample file (this time copied to c:/)
                        for (File attachment : attachments) {
                            helper.addAttachment(attachment.getName(), attachment);
                            System.err.println(attachment.getName() + "sending...");

                        }
                    } catch (MessagingException me) {
                        me.printStackTrace();
                    }
                    try {
                        mailSender.send(message);
                    } catch (MailAuthenticationException ex) {
                        ex.printStackTrace();

                    } catch (MailException me) {
                        me.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public  void sendMail(final String messageBody, final String subject,
                         final String toAddress, File attachment) {
        {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        message = mailSender.createMimeMessage();
                        MimeMessageHelper helper = createMimeMessage(messageBody, subject, toAddress);
                        // let's include the infamous windows Sample file (this time copied to c:/)
//                       FileSystemResource file = new FileSystemResource(attachment);
                        helper.addAttachment(attachment.getName(), attachment);
                        System.err.println(attachment.getName());
                    } catch (MessagingException me) {
                        me.printStackTrace();
                    }
                    try {
                        mailSender.send(message);
                    } catch (MailAuthenticationException ex) {
                        ex.printStackTrace();

                    } catch (MailException me) {
                        me.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
