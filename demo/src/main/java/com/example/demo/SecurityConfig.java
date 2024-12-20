package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
//active la configuration de la sécurité web dans une application
// utilisée avec une classe de configuration de sécurité personnalisée pour définir les règles de sécurité de l'application
@EnableWebSecurity
//activer la sécurité au niveau des méthodes dans l'application
// active spécifiquement l'annotation @Secured, permettant de sécuriser les méthodes en fonction des rôles (ex. @Secured("ROLE_ADMIN")).
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
// Crée un encoder pour encoder les mots de passe avec l'algorithme BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//Crée un service d'utilisateurs en mémoire avec des utilisateurs et leurs rôles
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build());

        userDetailsManager.createUser(User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build());

        return userDetailsManager;
    }
// Crée un AuthenticationManager pour gérer l'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
// Configure les règles de sécurité et d'accès pour les requêtes HTTP

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/*", "/js/", "/images/*").permitAll()  // Autorise l'accès aux ressources statiques
                        .requestMatchers("/", "/login", "/register").permitAll()  // Autorise l'accès à certaines pages publiques
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Restreint l'accès à /admin aux utilisateurs avec le rôle ADMIN
                        .anyRequest().authenticated()  // Authentifie toutes les autres requêtes
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Spécifie la page de login personnalisée
                        .loginProcessingUrl("/login")  // Spécifie l'URL de traitement de la connexion
                        .defaultSuccessUrl("/home", true)  // Redirige vers /home après une connexion réussie
                        .failureUrl("/login?error=true")  // Redirige vers /login en cas d'échec de connexion
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Spécifie l'URL de déconnexion
                        .logoutSuccessUrl("/login?logout=true")  // Redirige vers /login après déconnexion
                        .invalidateHttpSession(true)  // Invalide la session lors de la déconnexion
                        .deleteCookies("JSESSIONID")  // Supprime le cookie de session
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());  // Désactive CSRF (uniquement pour le développement)

        return http.build();
}}