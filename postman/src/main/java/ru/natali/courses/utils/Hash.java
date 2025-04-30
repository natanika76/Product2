package ru.natali.courses.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Hash {
    public static void main(String[] args) {
        String password = "1111";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password  : " + hashedPassword);
    }
}
