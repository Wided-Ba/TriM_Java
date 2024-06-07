package models;

import java.sql.Time;
import java.util.Date;

public class Rendez_Vous {
    private int id;
    private Date date;
    private Time heure;
    private int id_medecin;
    private String motif;
    private String status;
    private Patient patient;

    private Medecin medecin;

    public Rendez_Vous(int id, Date date,Time heure, String motif, String status,Patient patient, int id_medecin) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
        this.id_medecin = id_medecin;
    }

    public Rendez_Vous(Time heure,Date date, String motif, String status, Patient patient, Medecin medecin) {
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
        this.medecin=medecin;
    }  public Rendez_Vous(Time heure,Date date, String motif, Patient patient, Medecin medecin) {
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
        this.medecin=medecin;
    }
    public String getNomMedecin(){
        return medecin.getNom();
    }

    public Rendez_Vous(int id,Time heure, Date date, String motif, String status,String nom_med) {
        this.id = id;
        this.heure=heure;
        this.date = date;
        this.motif = motif;
        this.status = status;
        this.medecin.setNom(nom_med);
    }
    public Rendez_Vous(int id,Time heure, Date date, String motif, String status) {
        this.id = id;
        this.heure=heure;
        this.date = date;
        this.motif = motif;
        this.status = status;

    }
    public Rendez_Vous(int id,Time heure, Date date, String motif, String status,Medecin medecin) {
        this.id = id;
        this.heure=heure;
        this.date = date;
        this.motif = motif;
        this.status = status;
        this.medecin=medecin;
    }
    public Rendez_Vous(int id,Time heure, Date date, String motif, String status,int id_med) {
        this.id = id;
        this.heure=heure;
        this.date = date;
        this.motif = motif;
        this.status = status;
        this.medecin.setId(id_med);
    }
    public Rendez_Vous(int id,Time heure, Date date,String motif, String status, Patient patient) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
    }
    public Rendez_Vous(Time heure, Date date,String motif, String status, Patient patient,int idMed) {

        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;

    }
    public Rendez_Vous(Time heure, Date date,String motif, String status, Patient patient) {

        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }
    public int getIdMedecin()
    {
        return medecin.getId();
    }
    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getId_medecin() {
        return id_medecin;
    }

    public void setId_medecin(int id_medecin) {
        this.id_medecin = id_medecin;
    }

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Rendez_Vous{" +
                "id=" + id +
                ", date=" + date +
                ", heure=" + heure +
                ", motif='" + motif + '\'' +
                ", status='" + status + '\'' +
                ", patient=" + patient +
                ", id_medecin=" + id_medecin +
                '}';
    }
}
