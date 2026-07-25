package it.uniroma3.siw.torneo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.torneo.model.RigaClassifica;
import it.uniroma3.siw.torneo.service.ClassificaService;

// @RestController (non @Controller): ogni metodo restituisce direttamente dati
// (convertiti automaticamente in JSON), non il nome di una pagina HTML
@RestController
public class ClassificaRestController {

    @Autowired
    private ClassificaService classificaService;

    @GetMapping("/api/torneo/{id}/classifica")
    public List<RigaClassifica> getClassificaJson(@PathVariable("id") Long id) {
        return this.classificaService.calcolaClassifica(id);
    }

}