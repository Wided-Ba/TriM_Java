package models;

public class Maladie {
    private int id;
    private String nom;
    private String symptome;
    private String type;
    private String description;
    private Medecin medecin;

    public Maladie() {
        this.medecin = new Medecin();
    }

    public Maladie(int id, String nom, String symptome, String type, String description, Medecin medecin) {
        this.id = id;
        this.nom = nom;
        this.symptome = symptome;
        this.type = type;
        this.description = description;
        this.medecin = medecin;
    }

    public Maladie(String nom, String symptome, String type, String description) {
        this.nom = nom;
        this.symptome = symptome;
        this.type = type;
        this.description = description;
        this.medecin = new Medecin();
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

    public String getSymptome() {
        return symptome;
    }

    public void setSymptome(String symptome) {
        this.symptome = symptome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public int getIdMedecin() {
        return medecin.getId();
    }
    public void setIdMedecin(int idMedecin) {
        medecin.setId(idMedecin);
    }

    @Override
    public String toString() {
        return
                nom + " " + symptome + " " + type + " " + description;
    }
}
