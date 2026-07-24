package it.uniroma3.siw.torneo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.torneo.model.Giocatore;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {
}
