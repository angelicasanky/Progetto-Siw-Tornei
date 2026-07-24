package it.uniroma3.siw.torneo.model;

public class RigaClassifica {
    private Squadra squadra;
    private int punti;
    private int partiteGiocate;
    private int vittorie;
    private int pareggi;
    private int sconfitte;
    private int golFatti;
    private int golSubiti;

    public RigaClassifica(Squadra squadra) {
        this.squadra = squadra;
        this.punti = 0;
        this.partiteGiocate = 0;
        this.vittorie = 0;
        this.pareggi = 0;
        this.sconfitte = 0;
        this.golFatti = 0;
        this.golSubiti = 0;
    }

    public int getDifferenzaReti() {
        return this.golFatti - this.golSubiti;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public void setPartiteGiocate(int partiteGiocate) {
        this.partiteGiocate = partiteGiocate;
    }

    public int getVittorie() {
        return vittorie;
    }

    public void setVittorie(int vittorie) {
        this.vittorie = vittorie;
    }

    public int getPareggi() {
        return pareggi;
    }

    public void setPareggi(int pareggi) {
        this.pareggi = pareggi;
    }

    public int getSconfitte() {
        return sconfitte;
    }

    public void setSconfitte(int sconfitte) {
        this.sconfitte = sconfitte;
    }

    public int getGolFatti() {
        return golFatti;
    }

    public void setGolFatti(int golFatti) {
        this.golFatti = golFatti;
    }

    public int getGolSubiti() {
        return golSubiti;
    }

    public void setGolSubiti(int golSubiti) {
        this.golSubiti = golSubiti;
    }

}
