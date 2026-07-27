package it.uniroma3.siw.torneo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.uniroma3.siw.torneo.model.Utente;

public class UtenteDettagli implements UserDetails {

    private Utente utente;

    /**
     * Costruisce un'istanza di {@code UtenteDettagli} che avvolge l'entità {@link Utente}.
     *
     * @param utente l'utente del dominio applicativo da adattare per Spring Security
     */
    public UtenteDettagli(Utente utente) {
        this.utente = utente;
    }

    /**
     * Restituisce i ruoli (autorità) dell'utente nel formato atteso da Spring Security.
     * Il ruolo viene prefissato con {@code ROLE_} (es. {@code ROLE_ADMIN}, {@code ROLE_USER}).
     *
     * @return una collezione contenente l'autorità dell'utente
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security si aspetta i ruoli con prefisso "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.utente.getRuolo().name()));
    }

    /**
     * Restituisce la password dell'utente (già codificata con BCrypt).
     *
     * @return la password cifrata dell'utente
     */
    @Override
    public String getPassword() {
        return this.utente.getPassword();
    }

    /**
     * Restituisce lo username dell'utente, usato da Spring Security come identificativo di autenticazione.
     *
     * @return lo username dell'utente
     */
    @Override
    public String getUsername() {
        return this.utente.getUsername();
    }

    /**
     * Indica se l'account dell'utente non è scaduto.
     *
     * @return {@code true} sempre (nessuna logica di scadenza account implementata)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica se l'account dell'utente non è bloccato.
     *
     * @return {@code true} sempre (nessuna logica di blocco account implementata)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica se le credenziali (password) dell'utente non sono scadute.
     *
     * @return {@code true} sempre (nessuna logica di scadenza credenziali implementata)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica se l'account è abilitato.
     *
     * @return {@code true} sempre (tutti gli utenti registrati sono abilitati)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Restituisce l'entità {@link Utente} del dominio applicativo avvolta in questo oggetto.
     * Utile per accedere ai campi specifici dell'utente non previsti dall'interfaccia {@link UserDetails}.
     *
     * @return l'entità {@link Utente} associata
     */
    public Utente getUtente() {
        return this.utente;
    }
}