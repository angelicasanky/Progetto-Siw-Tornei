package it.uniroma3.siw.torneo.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.uniroma3.siw.torneo.model.Utente;

public class UtenteDettagli implements UserDetails {

    private Utente utente;

    public UtenteDettagli(Utente utente) {
        this.utente = utente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security si aspetta i ruoli con prefisso "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.utente.getRuolo().name()));
    }

    @Override
    public String getPassword() {
        return this.utente.getPassword();
    }

    @Override
    public String getUsername() {
        return this.utente.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Utente getUtente() {
        return this.utente;
    }
}