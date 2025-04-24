package ru.natali.courses.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурационный класс для веб-клиента.
 */
@Configuration
public class WebConfig {
    /**
     * Создает экземпляр WebClient.
     * @return экземпляр WebClient
     */
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
