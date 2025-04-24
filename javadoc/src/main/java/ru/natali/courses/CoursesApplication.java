package ru.natali.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Основной класс приложения.
 */
@SpringBootApplication
@EnableScheduling
public class CoursesApplication {
    /**
     * Точка входа в приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(CoursesApplication.class, args);
    }
}
/*
-encoding UTF-8 -docencoding UTF-8 -charset UTF-8
 */