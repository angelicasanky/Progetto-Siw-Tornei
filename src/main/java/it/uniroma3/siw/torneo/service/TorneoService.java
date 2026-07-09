package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.repository.TorneoRepository;
import jakarta.transaction.Transactional;

/*@Service segnala a Spring che questa classe fa parte del Service Layer*/
@Service
public class TorneoService {
	
	@Autowired
	private TorneoRepository torneoRepository;
	
	@Transactional
	public Torneo salvaTorneo(Torneo torneo) {
		return this.torneoRepository.save(torneo);
	}
	
	@Transactional
	public Torneo trovaPerId(Long id) {
		return this.torneoRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public Iterable<Torneo> trovaTutti(){
		return this.torneoRepository.findAll();
	}
	
	@Transactional
	public void cancellaTorneo(Long id) {
		this.torneoRepository.deleteById(id);
	}
	

}
