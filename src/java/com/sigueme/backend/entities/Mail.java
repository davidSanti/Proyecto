/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sigueme.backend.entities;

/**
 *
 * @author Santi
 */
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public final class Mail {

    public static void send(List<Usuario> para, String sujeto, String mensaje) throws UnsupportedEncodingException {
        final String user = "sistema.sigueme@gmail.com";
        final String pass = "S1guEMe123";
        System.out.println("va a enviar correo");
        //1st paso) Obtener el objeto de sesión
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.starttls.required", "false");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        //2nd paso)compose message
        try {
            /*BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource("C:\\Users\\Microsoft Windows 10\\Desktop\\File\\Archivo.txt")));
            adjunto.setFileName("Archivo.txt");
             */
            BodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);
            MimeMultipart multiparte = new MimeMultipart();
            multiparte.addBodyPart(texto);
            //multiparte.addBodyPart(adjunto);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "Sigueme"));
            //retornarDestinatarios(para);

            InternetAddress[] destinatarios = new InternetAddress[para.size()];

            for (int i = 0; i < para.size(); i++) {

                destinatarios[i] = new InternetAddress(para.get(i).getEmail());
            }

            message.addRecipients(Message.RecipientType.TO, destinatarios);
            message.setSubject(sujeto);
            message.setContent(multiparte, "text/html; charset=utf-8");
            //3rd paso)send message
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void send(String klzamud, String asunto, String su_clave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}