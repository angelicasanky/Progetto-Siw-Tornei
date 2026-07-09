package it.uniroma3.siw.torneo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.torneo.model.Torneo;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

}

/* Con extends... gli sto dicendo che voglio gestire l'entita Torneo, la cui chiave primaria
 * è Id di tipo Long*/
