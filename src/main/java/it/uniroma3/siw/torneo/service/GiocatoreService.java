package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Giocatore;
import it.uniroma3.siw.torneo.repository.GiocatoreRepository;
import jakarta.transaction.Transactional;

@Service
public class GiocatoreService {

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    /**
     * Persiste un giocatore nel database (inserimento o aggiornamento).
     *
     * @param giocatore l'entità Giocatore da salvare
     * @return l'entità Giocatore salvata, con l'ID generato dal database
     */
    @Transactional
    public Giocatore salvaGiocatore(Giocatore giocatore) {
        return this.giocatoreRepository.save(giocatore);
    }

    /**
     * Recupera un giocatore dal database tramite il suo ID.
     *
     * @param id l'identificativo univoco del giocatore
     * @return il giocatore trovato, oppure {@code null} se non esiste
     */
    @Transactional
    public Giocatore trovaPerId(Long id) {
        return this.giocatoreRepository.findById(id).orElse(null);
    }

    /**
     * Recupera l'elenco completo di tutti i giocatori presenti nel database.
     *
     * @return un {@link Iterable} contenente tutti i giocatori
     */
    @Transactional
    public Iterable<Giocatore> trovaTutti() {
        return this.giocatoreRepository.findAll();
    }

    /**
     * Elimina dal database il giocatore con l'ID specificato.
     *
     * @param id l'identificativo univoco del giocatore da eliminare
     */
    @Transactional
    public void cancellaGiocatore(Long id) {
        this.giocatoreRepository.deleteById(id);
    }

}
