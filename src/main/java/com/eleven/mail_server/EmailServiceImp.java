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
    public void sendEmail(String from,String to, String subject, String text) throws MailException {
        log.info("Started sending email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try{
            emailSender.send(message);
            log.info("Successfully send email!");
        } catch (MailException e) {
            log.error("Failed to send email.\n Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
