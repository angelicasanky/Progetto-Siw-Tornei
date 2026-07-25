package it.uniroma3.siw.torneo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.torneo.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
    Utente findByUsername(String username);

}
