package it.uniroma3.siw.torneo.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.torneo.model.Commento;
import java.util.List;

public interface CommentoRepository extends CrudRepository<Commento, Long> {

    List<Commento> findByPartitaId(Long partitaId);
}
