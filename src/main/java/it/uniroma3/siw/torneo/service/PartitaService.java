package it.uniroma3.siw.torneo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.repository.PartitaRepository;
import jakarta.transaction.Transactional;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;

    /**
     * Persiste una partita nel database (inserimento o aggiornamento).
     *
     * @param partita l'entità Partita da salvare
     * @return l'entità Partita salvata, con l'ID generato dal database
     */
    @Transactional
    public Partita salvaPartita(Partita partita) {
        return this.partitaRepository.save(partita);
    }

    /**
     * Recupera una partita dal database tramite il suo ID.
     *
     * @param id l'identificativo univoco della partita
     * @return la partita trovata, oppure {@code null} se non esiste
     */
    @Transactional
    public Partita trovaPerId(Long id) {
        return this.partitaRepository.findById(id).orElse(null);
    }

    /**
     * Recupera l'elenco completo di tutte le partite presenti nel database.
     *
     * @return un {@link Iterable} contenente tutte le partite
     */
    @Transactional
    public Iterable<Partita> trovaTutte() {
        return this.partitaRepository.findAll();
    }

    /**
     * Elimina dal database la partita con l'ID specificato.
     *
     * @param id l'identificativo univoco della partita da eliminare
     */
    @Transactional
    public void eliminaPartita(Long id) {
        this.partitaRepository.deleteById(id);
    }

    /**
     * Recupera tutte le partite che appartengono a un determinato torneo.
     *
     * @param torneoId l'identificativo univoco del torneo
     * @return la lista delle partite associate al torneo indicato
     */
    @Transactional
    public List<Partita> trovaPerTorneo(Long torneoId) {
        return this.partitaRepository.findByTorneoId(torneoId);
    }
}
