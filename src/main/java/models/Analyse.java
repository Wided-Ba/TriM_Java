package models;

public class Analyse {

    private int id;

    private String nom;

    private String type;

    private String outillage;

    private String conseils;

    private Laboratoire id_lab;
    private Laboratoire laboratoire;

    public Analyse(String nom, String type, String outillage, String conseils, Laboratoire id_lab) {
        this.nom = nom;
        this.type = type;
        this.outillage = outillage;
        this.conseils = conseils;
        this.id_lab = id_lab;
    }


    public Analyse(String nom, String type, String outillage, String conseils) {
        this.nom = nom;
        this.type = type;
        this.outillage = outillage;
        this.conseils = conseils;
    }

    public Analyse(int id,String nom, String type, String outillage, String conseils, Laboratoire id_lab) {
        this.id =id;
        this.nom = nom;
        this.type = type;
        this.outillage = outillage;
        this.conseils = conseils;
        this.id_lab = id_lab;
    }
    public Analyse(){
        this.id_lab = new Laboratoire();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutillage() {
        return outillage;
    }

    public void setOutillage(String outillage) {
        this.outillage = outillage;
    }

    public String getConseils() {
        return conseils;
    }

    public void setConseils(String conseils) {
        this.conseils = conseils;
    }

    public Laboratoire getId_lab() {
        return id_lab;
    }


    public void setId_lab(Laboratoire id_lab) {
        this.id_lab = id_lab;
    }



    @Override
    public String toString() {
        return "Analyse{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", outillage='" + outillage + '\'' +
                ", conseils='" + conseils + '\'' +
                ", id_lab=" + id_lab +
                '}';
    }
}