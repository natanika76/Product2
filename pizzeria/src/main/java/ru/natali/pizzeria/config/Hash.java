package ru.natali.pizzeria.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hash {
    public static void main(String[] args) {
        String password = "admin";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password  : " + hashedPassword);
    }
}
/* Какие есть пары логин - пароль:
login    password
admin    admin
user     user
customer customer
 */