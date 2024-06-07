package models;

import enums.EtatOrd;
import enums.TypeOrd;

import java.time.LocalDateTime;

public class Ordonnance {
    private int id;
    private TypeOrd type;
    private String description;
    private LocalDateTime date;
    private String code;
    private EtatOrd etat;
    private Medecin medecin;
    private Patient patient;
    private RendezVous rendezVous;
    private Laboratoire laboratoire;
    private Pharmacie pharmacie;

    private Analyse analyse;

    public Ordonnance(TypeOrd type, String description, LocalDateTime date, String code, EtatOrd etat, Medecin medecin,
                      Patient patient, RendezVous rendezVous, Laboratoire laboratoire , Pharmacie pharmacie,Analyse analyse) {
        this.type = type;
        this.description = description;
        this.date = date;
        this.code = code;
        this.etat = etat;
        this.medecin = medecin;
        this.patient = patient;
        this.rendezVous = rendezVous;
        this.laboratoire = laboratoire;
        this.pharmacie=pharmacie;
        this.analyse=analyse;
    }

    public Ordonnance(String code, EtatOrd etat,String description, TypeOrd type,LocalDateTime date,Medecin medecin,
                      Patient patient, RendezVous rendezVous, Laboratoire laboratoire , Pharmacie pharmacie,Analyse analyse){
        this.type = type;
        this.description = description;
        this.date = date;
        this.code = code;
        this.etat = etat;
        this.medecin = medecin;
        this.patient = patient;
        this.rendezVous = rendezVous;
        this.laboratoire = laboratoire;
        this.pharmacie=pharmacie;
        this.analyse=analyse;
    }

    public Ordonnance(){
        this.rendezVous = new RendezVous();
        this.medecin = new Medecin();
        this.patient = new Patient();
        this.laboratoire = new Laboratoire();
        this.pharmacie = new Pharmacie();
        this.analyse=new Analyse();
    }


    public Laboratoire getLaboratoire() {
        return laboratoire;
    }

    public void setLaboratoire(Laboratoire laboratoire) {
        this.laboratoire = laboratoire;
    }

    public Pharmacie getPharmacie() {
        return pharmacie;
    }

    public void setPharmacie(Pharmacie pharmacie) {
        this.pharmacie = pharmacie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOrd getType() {
        return type;
    }

    public void setType(TypeOrd type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EtatOrd getEtat() {
        return etat;
    }

    public void setEtat(EtatOrd etat) {
        this.etat = etat;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public RendezVous getRendezVous() {
        return rendezVous;
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.rendezVous = rendezVous;
    }

    public int getIdRendezVous() {
        return rendezVous.getId();
    }

    public void setIdRendezVous(int idRendezVous){
        rendezVous.setId(idRendezVous);
    }

    public int getIdPatient() {
        return patient.getId();
    }
    public void setIdPatient(int idPatient){
        patient.setId(idPatient);
    }
    public int getIdMedecin() {
        return medecin.getId();
    }
    public void setIdMedecin(int idMedecin){
        medecin.setId(idMedecin);
    }

    public int getIdLaboratoire() {
        return laboratoire.getId();
    }

    public void setIdLaboratoire(int idLaboratoire) {
        laboratoire.setId(idLaboratoire);
    }
    public int getIdPharmacie() {
        return pharmacie.getId();
    }

    public void setIdPharmacie(int idPharmacie) {

        pharmacie.setId(idPharmacie);
    }

    public int getIdAnalyse() {
        return analyse.getId();
    }
    public void setIdAnalyse(int idAnalyse) {
        analyse.setId(idAnalyse);
    }

    @Override
    public String toString() {
        return type + description + date + code +
                 etat + medecin + patient + rendezVous;
    }
}
