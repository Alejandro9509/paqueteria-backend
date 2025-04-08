package com.certuit.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

@Configuration

public class SecurityConfig {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_URL = "/auth/login";
    public static final String REFRESH_TOKEN_URL = "/auth/token";
    public static final String SIGNUP_URL = "/auth/signup";
    public static final String API_ROOT_URL = "/api/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desactivar la gestión de sesiones
                .and()
                .csrf().disable() // Desactivar CSRF (importante en APIs REST)
                .formLogin().disable() // Desactivar el login con formulario
                .httpBasic().disable().exceptionHandling().and()
                .authorizeRequests()
                // Permitir el acceso a rutas estáticas
                .requestMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg",
                        "/**/*.pdf", "/**/*.jpg", "/**/*.scss", "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                // Permitir el acceso a las URLs específicas
                .requestMatchers(API_ROOT_URL).permitAll()
                .requestMatchers("/file/**").permitAll()
                // Permitir el acceso a los endpoints de autenticación
                .requestMatchers(AUTHENTICATION_URL, REFRESH_TOKEN_URL, SIGNUP_URL).permitAll()
                // Cualquier otra solicitud necesita autenticación
                .anyRequest().authenticated();

        return http.build();
    }
}