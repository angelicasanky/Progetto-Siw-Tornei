package it.uniroma3.siw.torneo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.StatoPartita;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    /**
     * Recupera tutte le partite di un torneo filtrate per stato.
     * Utilizzato principalmente da {@link it.uniroma3.siw.torneo.service.ClassificaService}
     * per selezionare solo le partite già giocate ({@link it.uniroma3.siw.torneo.model.StatoPartita#PLAYED}).
     *
     * @param torneoId l'identificativo del torneo
     * @param stato    lo stato della partita ({@code PLAYED}, {@code SCHEDULED}, ecc.)
     * @return la lista delle partite del torneo con lo stato specificato
     */
    List<Partita> findByTorneoIdAndStato(Long torneoId, StatoPartita stato);

    /**
     * Recupera tutte le partite appartenenti a un determinato torneo, indipendentemente dal loro stato.
     *
     * @param torneoId l'identificativo del torneo
     * @return la lista di tutte le partite associate al torneo
     */
    List<Partita> findByTorneoId(Long torneoId);

    /**
     * Recupera tutte le partite in cui una determinata squadra ha partecipato,
     * sia come squadra di casa che come squadra ospite.
     *
     * @param squadra l'entità Squadra di cui cercare le partite
     * @return la lista delle partite in cui la squadra ha giocato
     */
    @Query("SELECT p FROM Partita p WHERE p.squadraCasa = :squadra OR p.squadraOspite = :squadra")
    List<Partita> findPartiteBySquadra(@Param("squadra") Squadra squadra);
}
