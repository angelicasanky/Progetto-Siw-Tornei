package it.uniroma3.siw.torneo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // Bean per cifrare le password: non salviamo mai password in chiaro
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // funzionalità pubbliche: chiunque può accedere senza login
                                                .requestMatchers("/", "/tornei", "/torneo/**", "/squadre",
                                                                "/squadra/**",
                                                                "/giocatori", "/giocatore/**", "/arbitri",
                                                                "/arbitro/**",
                                                                "/partite", "/partita/{id:[0-9]+}", "/css/**", "/js/**",
                                                                "/uploads/**", "/login", "/registrazione")
                                                .permitAll()
                                                // funzionalità admin: solo utenti con ruolo ADMIN
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                // aggiungere/modificare commenti: richiede login (qualsiasi ruolo
                                                // autenticato)
                                                .requestMatchers("/partita/*/commento/**").authenticated()
                                                // tutto il resto richiede almeno il login
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/tornei", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/tornei")
                                                .permitAll());

                return http.build();
        }
}