package com.eleven.mail_server;

public record Email(String from, String to, String subject, String text) {
}
