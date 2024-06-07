package models;

import java.sql.Time;

public class Laboratoire {

    private int id;

    private String nom;

    private String adresse;

    private int ntel;

    private Time hdebut;

    private Time hfin;

    public Laboratoire(int id, String nom, String adresse, int ntel, Time hdebut, Time hfin) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.ntel = ntel;
        this.hdebut = hdebut;
        this.hfin = hfin;
    }

    public Laboratoire(String nom, String adresse, int ntel, Time hdebut, Time hfin) {
        this.nom = nom;
        this.adresse = adresse;
        this.ntel = ntel;
        this.hdebut = hdebut;
        this.hfin = hfin;
    }

    public Laboratoire() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getNtel() {
        return ntel;
    }

    public void setNtel(int ntel) {
        this.ntel = ntel;
    }

    public Time getHdebut() {
        return hdebut;
    }

    public void setHdebut(Time hdebut) {
        this.hdebut = hdebut;
    }

    public Time getHfin() {
        return hfin;
    }

    public void setHfin(Time hfin) {
        this.hfin = hfin;
    }

    @Override
    public String toString() {
        return "Lab{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", ntel=" + ntel +
                ", hdebut=" + hdebut +
                ", hfin=" + hfin +
                '}';
    }
}
