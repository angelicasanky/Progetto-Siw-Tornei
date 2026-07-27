package it.uniroma3.siw.torneo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Commento;
import it.uniroma3.siw.torneo.repository.CommentoRepository;
import jakarta.transaction.Transactional;

@Service
public class CommentoService {

    @Autowired
    private CommentoRepository commentoRepository;

    /**
     * Persiste un commento nel database (inserimento o aggiornamento).
     *
     * @param commento l'entità Commento da salvare
     * @return l'entità Commento salvata, con l'ID generato dal database
     */
    @Transactional
    public Commento salvaCommento(Commento commento) {
        return this.commentoRepository.save(commento);
    }

    /**
     * Recupera un commento dal database tramite il suo ID.
     *
     * @param id l'identificativo univoco del commento
     * @return il commento trovato, oppure {@code null} se non esiste
     */
    @Transactional
    public Commento trovaPerId(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }

    /**
     * Recupera tutti i commenti associati a una specifica partita.
     *
     * @param partitaId l'identificativo univoco della partita
     * @return la lista dei commenti relativi alla partita indicata
     */
    @Transactional
    public List<Commento> trovaPerPartita(Long partitaId) {
        return this.commentoRepository.findByPartitaId(partitaId);
    }

    /**
     * Elimina dal database il commento con l'ID specificato.
     *
     * @param id l'identificativo univoco del commento da eliminare
     */
    @Transactional
    public void cancellaCommento(Long id) {
        this.commentoRepository.deleteById(id);
    }

}
