package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.repository.PartitaRepository;
import jakarta.transaction.Transactional;

@Service
public class PartitaService {

    @Autowired
    private PartitaRepository partitaRepository;

    @Transactional
    public Partita salvaPartita(Partita partita) {
        return this.partitaRepository.save(partita);
    }

    @Transactional
    public Partita trovaPerId(Long id) {
        return this.partitaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Partita> trovaTutte() {
        return this.partitaRepository.findAll();
    }

    @Transactional
    public void eliminaPartita(Long id) {
        this.partitaRepository.deleteById(id);
    }
}
