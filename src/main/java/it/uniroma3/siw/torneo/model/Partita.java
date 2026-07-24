package it.uniroma3.siw.torneo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOra;
    private String luogo;
    private Integer goalsHome;
    private Integer goalsAway;

    @Enumerated(EnumType.STRING)
    private StatoPartita stato;

    @ManyToOne
    private Torneo torneo;

    @ManyToOne
    private Squadra squadraCasa;

    @ManyToOne
    private Squadra squadraOspite;

    @ManyToOne
    private Arbitro arbitro;

    public Partita() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataOra() {
        return this.dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getLuogo() {
        return this.luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getGoalsHome() {
        return this.goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public Integer getGoalsAway() {
        return this.goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public StatoPartita getStato() {
        return this.stato;
    }

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public Torneo getTorneo() {
        return this.torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Squadra getSquadraCasa() {
        return this.squadraCasa;
    }

    public void setSquadraCasa(Squadra squadraCasa) {
        this.squadraCasa = squadraCasa;
    }

    public Squadra getSquadraOspite() {
        return this.squadraOspite;
    }

    public void setSquadraOspite(Squadra squadraOspite) {
        this.squadraOspite = squadraOspite;
    }

    public Arbitro getArbitro() {
        return this.arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

}
