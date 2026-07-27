package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.repository.UtenteRepository;
import jakarta.transaction.Transactional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Persiste un utente nel database (inserimento o aggiornamento).
     *
     * @param utente l'entità Utente da salvare
     * @return l'entità Utente salvata, con l'ID generato dal database
     */
    @Transactional
    public Utente salvaUtente(Utente utente) {
        return this.utenteRepository.save(utente);
    }

    /**
     * Recupera un utente dal database tramite il suo ID.
     *
     * @param id l'identificativo univoco dell'utente
     * @return l'utente trovato, oppure {@code null} se non esiste
     */
    @Transactional
    public Utente trovaPerId(Long id) {
        return this.utenteRepository.findById(id).orElse(null);
    }

    /**
     * Recupera un utente dal database tramite il suo username.
     * Utilizzato principalmente da Spring Security durante l'autenticazione.
     *
     * @param username lo username dell'utente da cercare
     * @return l'utente trovato, oppure {@code null} se non esiste
     */
    @Transactional
    public Utente trovaPerUsername(String username) {
        return this.utenteRepository.findByUsername(username);
    }

    /**
     * Recupera l'elenco completo di tutti gli utenti registrati nel sistema.
     *
     * @return un {@link Iterable} contenente tutti gli utenti
     */
    @Transactional
    public Iterable<Utente> trovaTutti() {
        return this.utenteRepository.findAll();
    }

    /**
     * Elimina dal database l'utente con l'ID specificato.
     *
     * @param id l'identificativo univoco dell'utente da eliminare
     */
    @Transactional
    public void cancellaUtente(Long id) {
        this.utenteRepository.deleteById(id);
    }
}
