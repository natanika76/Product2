package ru.natali.medregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/* http://localhost:8081/login  - сначала идем туда, там набираем admin и admin
или user и user и попадаем на dashboard */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF для простоты (для продакшена лучше включить)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",                     // Разрешаем свободный доступ к странице входа
                                "/api/**",            // Разрешаем свободный доступ ко всему /api
                                "/css/**",                    // Статические ресурсы разрешены всем
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()             // Все остальные запросы требуют аутентификацию
                )
                .formLogin(form -> form
                        .loginPage("/login")          // Страница входа
                        .loginProcessingUrl("/login") // URL для обработки формы (должен совпадать с th:action)
                        .defaultSuccessUrl("/dashboard") // Перенаправление после успешного входа
                        .failureUrl("/login?error")    // При ошибке аутентификации
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")         // URL для выхода
                        .logoutSuccessUrl("/login?logout") // Перенаправление после выхода
                        .invalidateHttpSession(true)   // Очистка сессии
                        .deleteCookies("JSESSIONID")   // Удаление cookies
                );

        return http.build();
    }

}