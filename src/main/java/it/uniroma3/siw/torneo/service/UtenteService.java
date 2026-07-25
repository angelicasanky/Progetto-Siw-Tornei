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

    @Transactional
    public Utente salvaUtente(Utente utente) {
        return this.utenteRepository.save(utente);
    }

    @Transactional
    public Utente trovaPerId(Long id) {
        return this.utenteRepository.findById(id).orElse(null);
    }

    @Transactional
    public Utente trovaPerUsername(String username) {
        return this.utenteRepository.findByUsername(username);
    }

    @Transactional
    public Iterable<Utente> trovaTutti() {
        return this.utenteRepository.findAll();
    }

    @Transactional
    public void cancellaUtente(Long id) {
        this.utenteRepository.deleteById(id);
    }
}
