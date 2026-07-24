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

    @Transactional
    public Arbitro salvaArbitro(Arbitro arbitro) {
        return this.arbitroRepository.save(arbitro);
    }

    @Transactional
    public Arbitro trovaPerId(Long id) {
        return this.arbitroRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Arbitro> trovaTutti() {
        return this.arbitroRepository.findAll();
    }

    @Transactional
    public void cancellaArbitro(Long id) {
        this.arbitroRepository.deleteById(id);
    }
}