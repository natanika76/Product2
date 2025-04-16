package ru.natali.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)  // <-- важно!
public class SecurityConfig{

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/tasks/add-task").hasAnyRole("USER", "ADMIN") // Ограничиваем доступ к форме добавления задачи
                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/tasks", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL для выхода
                        .logoutSuccessUrl("/login?logout")  // Перенаправление после выхода
                        .invalidateHttpSession(true)  // Очистка сессии
                        .deleteCookies("JSESSIONID")  // Удаление куки
                        .permitAll()
                );
                //.csrf(csrf -> csrf.disable()); // (Лучше включить CSRF в продакшене)

        return http.build();
    }

    // Создание пользователей в памяти
    @Bean
    UserDetailsService users() {
        var userViewer = User.withDefaultPasswordEncoder()
                .username("viewer")
                .password("viewerpass")
                .roles("VIEWER")
                .build();

        var userUser = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userpass")
                .roles("USER")
                .build();

        var userAdmin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminpass")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userViewer, userUser, userAdmin);
    }
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Убирает требование ROLE_
    }
}