package models;

public class Pharmacie {
    private int id;
    private String nom;
    private int ntel;
    private String adresse;
    private String type;
    private String loc;

    public Pharmacie(String nom, int ntel, String type, String adresse, String loc) {
        this.nom = nom;
        this.ntel = ntel;
        this.type = type;
        this.adresse = adresse;
        this.loc = loc;
    }

    public Pharmacie() {

    }

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

    public int getNtel() {
        return ntel;
    }

    public void setNtel(int ntel) {
        this.ntel = ntel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
