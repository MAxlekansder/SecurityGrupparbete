package org.example.securitygrupparbete.Model;


import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class UserModel { //Alex
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private int age;
    
    
    public String getFirstName() {
        return firstName;
    }
    
    
    public Long getId() {
        return id;
    }
    
    
    public UserModel setId(Long id) {
        this.id = id;
        return this;
    }
    
    
    public String getUsername() {
        return username;
    }
    
    
    public UserModel setUsername(String username) {
        this.username = username;
        return this;
    }
    
    
    public String getPassword() {
        return password;
    }
    
    
    public UserModel setPassword(String password) {
        this.password = password;
        return this;
    }
    
    
    public String getEmail() {
        return email;
    }
    
    
    public UserModel setEmail(String email) {
        this.email = email;
        return this;
    }
    
    
    public String getRole() {
        return role;
    }
    
    
    public UserModel setRole(String role) {
        this.role = role;
        return this;
    }
    
    
    public UserModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    
    public String getLastName() {
        return lastName;
    }
    
    
    public UserModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    
    public int getAge() {
        return age;
    }
    
    
    public UserModel setAge(int age) {
        this.age = age;
        return this;
    }
    
    
}
