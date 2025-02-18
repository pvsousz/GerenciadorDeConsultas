package com.gestao.consultas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin@IFCE#2022"))
                .roles("ADMIN")
                .build();

        UserDetails medico = User.withUsername("medico")
                .password(passwordEncoder().encode("medico123"))
                .roles("MEDICO")
                .build();

        UserDetails paciente = User.withUsername("paciente")
                .password(passwordEncoder().encode("paciente123"))
                .roles("PACIENTE")
                .build();

        return new InMemoryUserDetailsManager(admin, medico, paciente);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}