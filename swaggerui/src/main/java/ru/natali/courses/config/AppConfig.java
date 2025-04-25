package ru.natali.courses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Конфигурационный класс для включения AspectJ автоматического проксирования.
 */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
}
