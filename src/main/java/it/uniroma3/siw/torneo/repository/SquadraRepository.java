package it.uniroma3.siw.torneo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.uniroma3.siw.torneo.model.Squadra;

public interface SquadraRepository extends JpaRepository<Squadra, Long> {

    /**
     * Recupera tutte le squadre insieme ai loro giocatori in un'unica query SQL tramite JOIN FETCH.
     * Risolve il problema N+1 che si verificherebbe con il caricamento LAZY predefinito:
     * invece di emettere una query aggiuntiva per ogni squadra, ne viene eseguita una sola
     * con un LEFT JOIN FETCH sulla collezione {@code giocatori}.
     * La parola chiave {@code DISTINCT} evita duplicati causati dal JOIN.
     *
     * @return la lista di tutte le squadre con i giocatori già caricati in memoria
     */
    @Query("SELECT DISTINCT s FROM Squadra s LEFT JOIN FETCH s.giocatori")
    List<Squadra> trovaTutteConGiocatoriJoinFetch();
}
