package models;
import java.util.Date;
import java.util.Objects;

import java.sql.Time;

public class Medecin {
    private int id;
    private Admin admin;
    private String nom;
    private String prenom;
    private int n_tel;
    private String email;
    private String password;
    private String adresse;
    private String specialite;
    private Time hdebut;
    private Time hfin;
    private String role;
    private String genre;
    private boolean is_blocked;


    public Medecin(){};
    public Medecin(Admin admin,String nom, String prenom, int n_tel, String email, String password, String adresse, String specialite, Time hdebut, Time hfin, String role, String genre, boolean is_blocked) {
        this.admin = admin;
        this.nom = nom;
        this.prenom = prenom;
        this.n_tel = n_tel;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.specialite = specialite;
        this.hdebut = hdebut;
        this.hfin = hfin;
        this.role = role;
        this.genre = genre;
        this.is_blocked = is_blocked;
    }

    public Medecin(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }
    public Medecin(int id) {
        this.id = id;
    }
    public Medecin(int id, String nom, String prenom, int n_tel, String email, String password, String adresse, String specialite, String role, String genre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.n_tel = n_tel;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.specialite = specialite;
        this.role = role;
        this.genre = genre;
    }
    public Medecin(int id, String nom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getN_tel() {
        return n_tel;
    }

    public void setN_tel(int n_tel) {
        this.n_tel = n_tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public int getAdminId() {
        return admin.getId();
    }
    public void setAdminId(int adminId) {
        admin.setId(adminId);
    }
    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", n_tel=" + n_tel +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", adresse='" + adresse + '\'' +
                ", specialite='" + specialite + '\'' +
                ", hdebut=" + hdebut +
                ", hfin=" + hfin +
                ", role='" + role + '\'' +
                ", genre='" + genre + '\'' +
                ", is_blocked=" + is_blocked +
                '}';
    }
}
