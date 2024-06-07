package models;

import java.sql.Time;
import java.util.Date;

public class RendezVous {
    private int id;
    private Date date;
    private Time heure;
    private String motif;
    private String status;
    private Patient patient;
    private Medecin medecin;

    public RendezVous(){
        this.medecin=new Medecin();
        this.patient=new Patient();
    }

    public RendezVous(Date date, Time heure, String motif, String status, Patient patient, Medecin medecin) {
        this.date = date;
        this.heure = heure;
        this.motif = motif;
        this.status = status;
        this.patient = patient;
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

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    public int getPatientId() {
        if (patient != null) {
            return patient.getId();
        }
        return -1;
    }

    public int getMedecinId() {
        if (medecin != null) {
            return medecin.getId();
        }
        return -1;
    }
}
