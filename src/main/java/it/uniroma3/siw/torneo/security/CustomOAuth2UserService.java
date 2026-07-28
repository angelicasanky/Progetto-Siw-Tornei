package it.uniroma3.siw.torneo.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Ruolo;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.UtenteService;

/**
 * Servizio OAuth2 personalizzato che, al login con Google, collega l'utente
 * autenticato a un'entità {@link Utente} del database applicativo.
 * Se è il primo accesso con quell'email, viene creato un nuovo Utente
 * con ruolo USER; se esiste già, viene semplicemente riutilizzato.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UtenteService utenteService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributi = oauth2User.getAttributes();
        String email = (String) attributi.get("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Impossibile ottenere l'email dall'account Google");
        }

        Utente utente = this.utenteService.trovaPerUsername(email);

        if (utente == null) {
            utente = new Utente();
            utente.setUsername(email);
            utente.setRuolo(Ruolo.USER);
            // niente password: questo utente si autentica solo tramite Google
            this.utenteService.salvaUtente(utente);
        }

        return oauth2User;
    }
}