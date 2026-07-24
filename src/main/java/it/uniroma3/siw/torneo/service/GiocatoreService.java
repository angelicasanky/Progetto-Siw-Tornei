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

    @Transactional
    public Giocatore salvaGiocatore(Giocatore giocatore) {
        return this.giocatoreRepository.save(giocatore);
    }

    @Transactional
    public Giocatore trovaPerId(Long id) {
        return this.giocatoreRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Giocatore> trovaTutti() {
        return this.giocatoreRepository.findAll();
    }

    @Transactional
    public void cancellaGiocatore(Long id) {
        this.giocatoreRepository.deleteById(id);
    }

}
