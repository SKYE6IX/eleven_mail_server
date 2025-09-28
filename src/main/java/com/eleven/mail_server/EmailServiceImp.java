package com.eleven.mail_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImp implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImp.class);

    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImp(){}

    @Override
    public void sendEmail(Email emailData) throws MailException {
        log.info("Started sending email");
        SimpleMailMessage message = new SimpleMailMessage();
        String emailTemplate = """
                Name: %s
                
                Email: %s
                
                Phone: %s
                
                Source: %s
                
                Message: %s
                """;
        message.setFrom(emailData.from());
        message.setTo(emailData.to());
        message.setSubject(emailData.subject());

        message.setText(String.format(emailTemplate,
                emailData.name(),emailData.email(),
                emailData.phone(), emailData.leadSource(),
                emailData.message()));

        try{
            emailSender.send(message);
            log.info("Successfully send email!");
        } catch (MailException e) {
            log.error("Failed to send email.\n Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
