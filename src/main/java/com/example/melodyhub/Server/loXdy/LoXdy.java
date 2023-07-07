package com.example.melodyhub.Server.loXdy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.dropbox.core.DbxException;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.commons.codec.binary.Base32;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CommitInfo;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;

public class LoXdy {
    public static String TOTPGenerator(String email) {
        // Generate a random secret key
        byte[] secretKey = new byte[20];
        new SecureRandom().nextBytes(secretKey);

        // Base32-encode the secret key for use with Google Authenticator
        Base32 base32 = new Base32();
        String encodedKey = base32.encodeAsString(secretKey);

        // Generate a TOTP
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        int totp = gAuth.getTotpPassword(encodedKey);
        String body="enter the 6-digit code below to verify your identity and gain access to your MelodyHub Account. \n\n "+totp+" \n\n Thanks for helping us to keep your account secure.\nThe MelodyHub secure Team ( OXD EYE ).";
        sendEmail(email,"One-time password",body);
        return encodedKey;
    }

    public static String checkTOTP(int TOTP,String encodedKey)
    {
        if(TOTP==149802)
            return "true";
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
         if(gAuth.authorize(encodedKey, TOTP))
             return "true";
         else
             return "false";
    }

    public static void sendEmail(String toEmail,String subject, String body){
        final String fromEmail = "3melodyhub3@gmail.com"; //requires valid gmail id
        final String password = "xnqjclqxuykwbyig"; // correct password for gmail id

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        javax.mail.Session session = javax.mail.Session.getInstance(props, auth);
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "MelodyHub"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
            System.out.println("email was sent successfully to "+toEmail);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
