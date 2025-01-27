package ru.natali.notes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*
Запустить в командной строке mvn spring-boot:run -D spring-boot.run.profiles=debug
                             mvn spring-boot:run -D spring-boot.run.profiles=production
 */
@SpringBootApplication
public class NotesApplication implements CommandLineRunner{

    @Value("${custom.message}")
    private String customMessage;

    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Custom message: " + customMessage);
    }
}
