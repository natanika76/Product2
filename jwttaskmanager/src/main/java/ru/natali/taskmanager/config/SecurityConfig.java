package ru.natali.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //http://localhost:8080/login.html вход в систему

    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager= new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("admin")
                        .password("123")
                        .roles("ADMIN")
                        .build()
        );
        return manager;
    }

    @Bean
    @SuppressWarnings("deprecation")
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.
                        requestMatchers("/","/login","login.html","/api/login","/css/**","/js/**","/images/**").permitAll()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception->
                        exception.authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login.html");
                        })
                )
                .formLogin(form->form.disable())
                .addFilterBefore(new JwtFilter(userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
