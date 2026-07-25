package it.uniroma3.siw.torneo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.StatoPartita;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    List<Partita> findByTorneoIdAndStato(Long torneoId, StatoPartita stato);

    List<Partita> findByTorneoId(Long torneoId);

    @Query("SELECT p FROM Partita p WHERE p.squadraCasa = :squadra OR p.squadraOspite = :squadra")
    List<Partita> findPartiteBySquadra(@Param("squadra") Squadra squadra);
}
