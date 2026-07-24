package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.repository.SquadraRepository;
import jakarta.transaction.Transactional;

@Service
public class SquadraService {
	
	@Autowired 
	private SquadraRepository squadraRepository;
	
	//salva per ID
	@Transactional
	public Squadra salvaSquadra(Squadra squadra) {
		return this.squadraRepository.save(squadra);
	}
	
	//trova per ID
	@Transactional
	public Squadra trovaPerId(Long id) {
		return this.squadraRepository.findById(id).orElse(null);
	}
	
	
	//trovaTutti
	@Transactional
	public Iterable<Squadra> trovaTutti() {
		return this.squadraRepository.findAll();
	}
	
	//Cancella
	@Transactional
	public void cancellaSquadra(Long id) {
		this.squadraRepository.deleteById(id);
	}
	

}
