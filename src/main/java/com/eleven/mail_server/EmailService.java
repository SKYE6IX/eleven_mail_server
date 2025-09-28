package com.eleven.mail_server;

import org.springframework.mail.MailException;

public interface EmailService {
    void sendEmail(Email emailData) throws MailException;
}
