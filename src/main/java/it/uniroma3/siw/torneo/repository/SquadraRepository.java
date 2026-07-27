package it.uniroma3.siw.torneo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.torneo.model.Squadra;

public interface SquadraRepository extends JpaRepository<Squadra, Long> {

    @Query("SELECT DISTINCT s FROM Squadra s LEFT JOIN FETCH s.giocatori")
    List<Squadra> trovaTutteConGiocatoriJoinFetch();
}
