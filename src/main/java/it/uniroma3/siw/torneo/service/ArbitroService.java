package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Arbitro;
import it.uniroma3.siw.torneo.repository.ArbitroRepository;
import jakarta.transaction.Transactional;

@Service
public class ArbitroService {

    @Autowired
    private ArbitroRepository arbitroRepository;

    /**
     * Persiste un arbitro nel database (inserimento o aggiornamento).
     *
     * @param arbitro l'entità Arbitro da salvare
     * @return l'entità Arbitro salvata, con l'ID generato dal database
     */
    @Transactional
    public Arbitro salvaArbitro(Arbitro arbitro) {
        return this.arbitroRepository.save(arbitro);
    }

    /**
     * Recupera un arbitro dal database tramite il suo ID.
     *
     * @param id l'identificativo univoco dell'arbitro
     * @return l'arbitro trovato, oppure {@code null} se non esiste
     */
    @Transactional
    public Arbitro trovaPerId(Long id) {
        return this.arbitroRepository.findById(id).orElse(null);
    }

    /**
     * Recupera l'elenco completo di tutti gli arbitri presenti nel database.
     *
     * @return un {@link Iterable} contenente tutti gli arbitri
     */
    @Transactional
    public Iterable<Arbitro> trovaTutti() {
        return this.arbitroRepository.findAll();
    }

    /**
     * Elimina dal database l'arbitro con l'ID specificato.
     *
     * @param id l'identificativo univoco dell'arbitro da eliminare
     */
    @Transactional
    public void cancellaArbitro(Long id) {
        this.arbitroRepository.deleteById(id);
    }
}