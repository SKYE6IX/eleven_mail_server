package com.eleven.mail_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<EmailResponse> sendMail(@RequestBody Email email){
        emailService.sendEmail(
                email.from(), email.to(), email.subject(), email.text()
        );
        return ResponseEntity.ok(new EmailResponse("ok","Message sent"));
    }
}