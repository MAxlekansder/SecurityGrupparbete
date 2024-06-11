package org.example.securitygrupparbete.Service;


public class MaskingService {

    public static final String maskEmail(String email) { //Oskar
        return email.replaceAll("(?<=.{2}).(?=.*@)", "*");
    }
}
