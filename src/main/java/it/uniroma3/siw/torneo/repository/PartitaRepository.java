package it.uniroma3.siw.torneo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.StatoPartita;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    List<Partita> findByTorneoIdAndStato(Long torneoId, StatoPartita stato);
}
