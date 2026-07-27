package it.uniroma3.siw.torneo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.UtenteService;

@Service
public class UtenteDettagliService implements UserDetailsService {

    @Autowired
    private UtenteService utenteService;

    /**
     * Carica un utente dal database tramite il suo username per l'autenticazione di Spring Security.
     * Avvolge l'entità {@link it.uniroma3.siw.torneo.model.Utente} in un oggetto {@link UtenteDettagli}
     * compatibile con Spring Security.
     *
     * @param username lo username fornito durante il tentativo di login
     * @return un oggetto {@link UserDetails} contenente le informazioni dell'utente
     * @throws UsernameNotFoundException se nessun utente con il dato username è presente nel database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = this.utenteService.trovaPerUsername(username);
        if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }
        return new UtenteDettagli(utente);
    }
}