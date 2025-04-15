package ru.natali.courses.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/**").permitAll()              // Доступ к API без аутентификации
                        .requestMatchers("/", "/login", "/register", "/registration-success", "/dashboard", "/css/**", "/js/**").permitAll()  // Страницы входа и ресурсы CSS/JS общедоступны
                        .anyRequest().authenticated())                      // Все остальные запросы требуют аутентификацию
                .formLogin((form) -> form
                        .loginPage("/login")                                // Путь страницы авторизации
                        .defaultSuccessUrl("/dashboard", true))             // После успешного входа отправляем на /dashboard
                .csrf((csrf) -> csrf.disable());                             // Отключение защиты CSRF (только для теста)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Делегируем выбор энкодера на основе префикса ({noop}, {bcrypt} и т. д.)
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Убирает требование ROLE_
    }
}