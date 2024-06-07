package models;
import java.util.Date;

public class Reclamation {
    private int id;
    private Date daterec;

    public Reclamation(int id, Date daterec, Patient patient, String description, String status) {
        this.id = id;
        this.daterec = daterec;
        this.patient = patient;
        this.description = description;
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    private Patient patient;
    private String description;
    private String status;

    public Reclamation(int id, Date daterec, String description, String status) {
        this.id = id;
        this.daterec = daterec;
        this.description = description;
        this.status = status;
    }

    public Reclamation(Date daterec, String description, String status) {
        this.daterec = daterec;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDaterec() {
        return daterec;
    }

    public void setDaterec(Date daterec) {
        this.daterec = daterec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", daterec=" + daterec +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
