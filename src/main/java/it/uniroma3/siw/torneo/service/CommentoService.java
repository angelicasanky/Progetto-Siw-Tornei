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

    @Transactional
    public Commento salvaCommento(Commento commento) {
        return this.commentoRepository.save(commento);
    }

    @Transactional
    public Commento trovaPerId(Long id) {
        return this.commentoRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Commento> trovaPerPartita(Long partitaId) {
        return this.commentoRepository.findByPartitaId(partitaId);
    }

    @Transactional
    public void cancellaCommento(Long id) {
        this.commentoRepository.deleteById(id);
    }

}
