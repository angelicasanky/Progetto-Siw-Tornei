package it.uniroma3.siw.torneo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/tornei", "/torneo/**", "/squadre",
                                                                "/squadra/**",
                                                                "/giocatori", "/giocatore/**", "/arbitri",
                                                                "/arbitro/**",
                                                                "/partite", "/partita/{id:[0-9]+}", "/css/**", "/js/**",
                                                                "/uploads/**", "/login", "/registrazione")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/partita/*/commento/**").authenticated()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/tornei", true)
                                                .permitAll())
                                // AGGIUNTO IL BLOCCO PER IL LOGIN CON GOOGLE:
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/tornei", true))
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/tornei")
                                                .permitAll());

                return http.build();
        }
}