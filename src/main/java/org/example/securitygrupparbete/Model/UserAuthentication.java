package org.example.securitygrupparbete.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class UserAuthentication {

    @Id private Long id;
    private String username;
    private String password;
    private String email;

}
