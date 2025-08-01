package com.eleven.mail_server;

import org.springframework.mail.MailException;

public interface EmailService {
    void sendEmail(String from, String to, String subject, String text) throws MailException;
}
