package com.gestao.consultas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class AutenticaService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Scanner scanner = new Scanner(System.in);

    public boolean autenticarUsuario() {
        while (true) {
            System.out.println("=== AUTENTICAÇÃO ===");
            System.out.print("Digite 'sair' para cancelar\nusuário: ");
            String username = scanner.nextLine();

            if (username.equalsIgnoreCase("sair")) {
                System.out.println("Autenticação cancelada.");
                System.exit(0);
            }

            System.out.print("Senha: ");
            String password = scanner.nextLine();

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (passwordEncoder.matches(password, userDetails.getPassword())) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return true;
                } else {
                    System.out.println("Senha incorreta. Tente novamente.");
                }
            } catch (UsernameNotFoundException e) {
                System.out.println("Usuário não encontrado. Tente novamente.");
            }
        }
    }
}