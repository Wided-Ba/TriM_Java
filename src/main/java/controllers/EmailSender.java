package controllers;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail3(String recipient, String subject, String body,String nom) throws MessagingException, IOException {
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "trim.noreplay@gmail.com";
        String password = "hqatehdmmfmjavwb";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        String htmlContent = readHTMLFile("src/main/resources/html/email3.html");
        htmlContent = htmlContent.replace("{{code}}", String.valueOf(body));
        htmlContent = htmlContent.replace("{{ patient.nom }}", String.valueOf(nom));

        // Créer une partie du message pour le contenu HTML
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html"); // Définir le contenu HTML

        // Créer un multipart pour le message et ajouter la partie HTML
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);

        // Définir le contenu multipart comme contenu du message
        message.setContent(multipart);

        Transport.send(message);
        System.out.println("Email sent successfully!");
    }

    public static void sendEmail2(String recipient, String subject, String body, String nom, String code, String filePath) throws MessagingException, IOException {
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "trim.noreplay@gmail.com";
        String password = "hqatehdmmfmjavwb";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        String htmlContent = readHTMLFile("src/main/resources/html/email2.html");
        htmlContent = htmlContent.replace("{{ patient.nom }}", String.valueOf(body));
        htmlContent = htmlContent.replace("{{ medecin.nom }}", String.valueOf(nom));
        htmlContent = htmlContent.replace("{{ id }}", String.valueOf(code));

        // Créer une partie du message pour le contenu HTML
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html"); // Définir le contenu HTML

        // Créer un multipart pour le message et ajouter la partie HTML
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);

        // Vérifier si le chemin du fichier PDF est fourni
        if (filePath != null && !filePath.isEmpty()) {
            // Vérifier si le fichier existe
            File file = new File(filePath);
            if (file.exists()) {
                // Créer une partie pour le fichier PDF
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
                // Ajouter la partie du fichier PDF au multipart
                multipart.addBodyPart(attachmentPart);
            } else {
                System.err.println("File does not exist: " + filePath);
            }
        }

        // Définir le contenu multipart comme contenu du message
        message.setContent(multipart);

        // Envoyer le message
        Transport.send(message);
        System.out.println("Email sent successfully!");
    }


    public static void sendEmail(String recipient, String subject, String nom, String filePath) throws MessagingException, IOException {
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "trim.noreplay@gmail.com";
        String password = "hqatehdmmfmjavwb";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        String htmlContent = readHTMLFile("src/main/resources/html/email.html");
        htmlContent = htmlContent.replace("{{patient.nom}}", String.valueOf(nom));

        // Créer une partie du message pour le contenu HTML
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html"); // Définir le contenu HTML

        // Créer un multipart pour le message et ajouter la partie HTML
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);

        // Vérifier si le chemin du fichier PDF est fourni
        if (filePath != null && !filePath.isEmpty()) {
            // Vérifier si le fichier existe
            File file = new File(filePath);
            if (file.exists()) {
                // Créer une partie pour le fichier PDF
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(file);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(file.getName());
                // Ajouter la partie du fichier PDF au multipart
                multipart.addBodyPart(attachmentPart);
            } else {
                System.err.println("File does not exist: " + filePath);
            }
        }

        // Définir le contenu multipart comme contenu du message
        message.setContent(multipart);

        // Envoyer le message
        Transport.send(message);
        System.out.println("Email sent successfully!");
    }



    private static String readHTMLFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
            }
        }
        return contentBuilder.toString();
    }
}