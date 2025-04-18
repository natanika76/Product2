package ru.natali.taskmanager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@SpringBootApplication
public class TaskmanagerApplication {
    //private static final Key SECRET_KEY= Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }
/*
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }
    public static String validateToken(String token){
        try{
            Claims claims= Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e){
            return null;
        }
    }
    public class JwtFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String authHeader=request.getHeader("Authorization");
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                String  token=authHeader.substring(7);
                String username= TaskmanagerApplication.validateToken(token);
                if(username!=null){
                    UserDetails userDetails=userDetailsService().loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication=
                            new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request,response);
        }
    }
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
                .addFilterBefore(new TaskmanagerApplication.JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @RestController
    public class AuthController {
        private final AuthenticationManager authenticationManager;

        public AuthController(AuthenticationManager authenticationManager) {
            this.authenticationManager = authenticationManager;
        }
        @PostMapping("/api/login")
        public ResponseEntity<?> login(@RequestBody TaskmanagerApplication.AuthRequest authRequest){
            try{
                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
                );
                String token=generateToken(authRequest.getUsername());
                return ResponseEntity.ok(new TaskmanagerApplication.AuthResponse(token));
            } catch (AuthenticationException ex){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        }
        @GetMapping("/hello")
        public ResponseEntity<?> hello(){
            return ResponseEntity.ok("Hello, you are authenticated!");
        }
    }
    public static class AuthRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    public static class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
 */
}
