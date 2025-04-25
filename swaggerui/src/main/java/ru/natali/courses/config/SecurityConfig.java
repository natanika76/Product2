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
//http://localhost:8080/swagger-ui/index.html

/**
 * Конфигурационный класс для настройки безопасности приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;

    /**
     * Конструктор с внедрением зависимости DataSource.
     * @param dataSource источник данных для аутентификации
     */
    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Настраивает цепочку фильтров безопасности.
     * @param http объект HttpSecurity для настройки
     * @return сконфигурированная цепочка фильтров безопасности
     * @throws Exception если произошла ошибка при настройке
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/", "/login","/swagger-ui/**", "/v3/**",  "/register", "/registration-success", "/dashboard", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true))
                .csrf((csrf) -> csrf.disable());

        return http.build();
    }

    /**
     * Создает кодировщик паролей с делегированием.
     * @return экземпляр PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Настраивает префикс для ролей.
     * @return экземпляр GrantedAuthorityDefaults с пустым префиксом
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
