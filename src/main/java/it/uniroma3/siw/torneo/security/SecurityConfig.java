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

        /**
         * Registra un {@link PasswordEncoder} basato su BCrypt nel contesto Spring.
         * BCrypt applica automaticamente un salt casuale, rendendo sicuro lo storage
         * delle password.
         *
         * @return un'istanza di {@link BCryptPasswordEncoder}
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Configura la catena di filtri di sicurezza di Spring Security.
         * <p>
         * Le regole di autorizzazione definite sono:
         * <ul>
         * <li>Accesso pubblico (anche non autenticati) per homepage, dettagli di
         * tornei,
         * squadre, giocatori, arbitri, partite, file statici, login e
         * registrazione.</li>
         * <li>Solo utenti con ruolo {@code ADMIN} possono accedere ai percorsi
         * {@code /admin/**}.</li>
         * <li>Solo utenti autenticati possono accedere ai percorsi
         * {@code /partita/&#42;/commento/&#42;&#42;}.</li>
         * <li>Qualsiasi altra richiesta richiede autenticazione.</li>
         * </ul>
         * Vengono abilitati sia il login tramite form che il login OAuth2 (es. Google),
         * entrambi con redirect su {@code /tornei} dopo il successo.
         * Il logout reindirizza anch'esso su {@code /tornei}.
         * </p>
         *
         * @param http il builder per la configurazione HTTP di Spring Security
         * @return la {@link SecurityFilterChain} costruita
         * @throws Exception se si verifica un errore durante la configurazione
         */
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