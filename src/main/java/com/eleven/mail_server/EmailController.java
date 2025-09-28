package com.eleven.mail_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-mail")
    public ResponseEntity<EmailResponse> sendMail(@RequestBody Email emailData){
        emailService.sendEmail(emailData);
        return ResponseEntity.ok(new EmailResponse("ok","Message sent"));
    }
}