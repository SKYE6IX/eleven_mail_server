package com.eleven.mail_server;

import java.util.Optional;

public record Email(String from, String to,
                    String subject, String name,
                    String email, String phone,
                    String message, String leadSource) {
}
