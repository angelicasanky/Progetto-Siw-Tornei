package it.uniroma3.siw.torneo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.torneo.model.Torneo;

public interface TorneoRepository extends JpaRepository<Torneo, Long> {

}

/* Con extends... gli sto dicendo che voglio gestire l'entita Torneo, la cui chiave primaria
 * è Id di tipo Long*/
