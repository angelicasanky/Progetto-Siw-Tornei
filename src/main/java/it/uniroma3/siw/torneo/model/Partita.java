package it.uniroma3.siw.torneo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Entità JPA che rappresenta una partita all'interno di un torneo.
 * Contiene riferimenti al torneo, alle due squadre, all'arbitro e alla lista dei commenti.
 * Il risultato è espresso tramite i campi {@code goalsHome} e {@code goalsAway};
 * lo {@link StatoPartita} indica se la partita è pianificata, in corso o già giocata.
 */
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

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commento> commenti = new ArrayList<>();

    /** Costruttore vuoto richiesto da JPA per istanziare l'entità durante la lettura dal database. */
    public Partita() {
    }

    /**
     * Restituisce l'identificativo univoco della partita.
     *
     * @return l'ID generato dal database
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Imposta l'identificativo univoco della partita.
     *
     * @param id l'ID da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce la data e l'ora in cui si disputa la partita.
     *
     * @return la data e ora della partita
     */
    public LocalDateTime getDataOra() {
        return this.dataOra;
    }

    /**
     * Imposta la data e l'ora in cui si disputa la partita.
     *
     * @param dataOra la data e ora da assegnare
     */
    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    /**
     * Restituisce il luogo (stadio o città) in cui si disputa la partita.
     *
     * @return il luogo della partita
     */
    public String getLuogo() {
        return this.luogo;
    }

    /**
     * Imposta il luogo in cui si disputa la partita.
     *
     * @param luogo il luogo da assegnare
     */
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    /**
     * Restituisce il numero di gol segnati dalla squadra di casa.
     *
     * @return i gol della squadra di casa, oppure {@code null} se la partita non è ancora giocata
     */
    public Integer getGoalsHome() {
        return this.goalsHome;
    }

    /**
     * Imposta il numero di gol segnati dalla squadra di casa.
     *
     * @param goalsHome i gol della squadra di casa
     */
    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    /**
     * Restituisce il numero di gol segnati dalla squadra ospite.
     *
     * @return i gol della squadra ospite, oppure {@code null} se la partita non è ancora giocata
     */
    public Integer getGoalsAway() {
        return this.goalsAway;
    }

    /**
     * Imposta il numero di gol segnati dalla squadra ospite.
     *
     * @param goalsAway i gol della squadra ospite
     */
    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    /**
     * Restituisce lo stato corrente della partita (es. {@code SCHEDULED}, {@code PLAYED}).
     *
     * @return lo stato della partita
     */
    public StatoPartita getStato() {
        return this.stato;
    }

    /**
     * Imposta lo stato corrente della partita.
     *
     * @param stato lo stato da assegnare
     */
    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    /**
     * Restituisce il torneo a cui appartiene la partita.
     *
     * @return il torneo associato
     */
    public Torneo getTorneo() {
        return this.torneo;
    }

    /**
     * Imposta il torneo a cui appartiene la partita.
     *
     * @param torneo il torneo da associare
     */
    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    /**
     * Restituisce la squadra che gioca in casa.
     *
     * @return la squadra di casa
     */
    public Squadra getSquadraCasa() {
        return this.squadraCasa;
    }

    /**
     * Imposta la squadra che gioca in casa.
     *
     * @param squadraCasa la squadra di casa da associare
     */
    public void setSquadraCasa(Squadra squadraCasa) {
        this.squadraCasa = squadraCasa;
    }

    /**
     * Restituisce la squadra ospite.
     *
     * @return la squadra ospite
     */
    public Squadra getSquadraOspite() {
        return this.squadraOspite;
    }

    /**
     * Imposta la squadra ospite.
     *
     * @param squadraOspite la squadra ospite da associare
     */
    public void setSquadraOspite(Squadra squadraOspite) {
        this.squadraOspite = squadraOspite;
    }

    /**
     * Restituisce l'arbitro designato per la partita.
     *
     * @return l'arbitro della partita
     */
    public Arbitro getArbitro() {
        return this.arbitro;
    }

    /**
     * Imposta l'arbitro designato per la partita.
     *
     * @param arbitro l'arbitro da associare
     */
    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    /**
     * Restituisce la lista dei commenti lasciati dagli utenti per questa partita.
     *
     * @return la lista dei commenti
     */
    public List<Commento> getCommenti() {
        return commenti;
    }

    /**
     * Imposta la lista dei commenti associati alla partita.
     *
     * @param commenti la lista di commenti da assegnare
     */
    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

}
