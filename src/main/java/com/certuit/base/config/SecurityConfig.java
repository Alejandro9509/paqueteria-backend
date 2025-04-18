package com.certuit.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration

public class SecurityConfig {

    public static final String AUTHENTICATION_URL = "/auth/login";
    public static final String REFRESH_TOKEN_URL = "/auth/token";
    public static final String SIGNUP_URL = "/auth/signup";
    public static final String API_ROOT_URL = "/api/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return  http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Desactivar CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .formLogin(AbstractHttpConfigurer::disable) // Desactivar login con formulario
                .httpBasic(AbstractHttpConfigurer::disable) // Desactivar HTTP Basic
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        new AntPathRequestMatcher("/"),
                                        new AntPathRequestMatcher("/error"),
                                        new AntPathRequestMatcher("/favicon.ico"),
                                        new AntPathRequestMatcher("/**/*.png"),
                                        new AntPathRequestMatcher("/**/*.gif"),
                                        new AntPathRequestMatcher("/**/*.svg"),
                                        new AntPathRequestMatcher("/**/*.pdf"),
                                        new AntPathRequestMatcher("/**/*.jpg"),
                                        new AntPathRequestMatcher("/**/*.scss"),
                                        new AntPathRequestMatcher("/**/*.css"),
                                        new AntPathRequestMatcher("/**/*.js"),
                                        new AntPathRequestMatcher("/**/*.html"),
                                        new AntPathRequestMatcher(API_ROOT_URL),
                                        new AntPathRequestMatcher(AUTHENTICATION_URL),
                                        new AntPathRequestMatcher(REFRESH_TOKEN_URL),
                                        new AntPathRequestMatcher(SIGNUP_URL),
                                        new AntPathRequestMatcher("/file/**")
                                ).permitAll()
                        .anyRequest().authenticated()
                ).build();
    }
}