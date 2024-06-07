package models;

public class Chef_Lab {

    private int id;

    private int id_lab;

    private String nom;

    private String prenom;

    private int ntel;

    private String email;

    private String password;

    private String genre;

    private String role;

    private Boolean is_blocked;

    public Chef_Lab(int id, int id_lab, String nom, String prenom, int ntel, String email, String password, String genre, String role, Boolean is_blocked) {
        this.id = id;
        this.id_lab = id_lab;
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.genre = genre;
        this.role = role;
        this.is_blocked = is_blocked;
    }

    public Chef_Lab(String nom, String prenom, int ntel, String email, String password, String genre, String role, Boolean is_blocked) {
        this.nom = nom;
        this.prenom = prenom;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.genre = genre;
        this.role = role;
        this.is_blocked = is_blocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_lab() {
        return id_lab;
    }

    public void setId_lab(int id_lab) {
        this.id_lab = id_lab;
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

    public int getNtel() {
        return ntel;
    }

    public void setNtel(int ntel) {
        this.ntel = ntel;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(Boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    @Override
    public String toString() {
        return "Chef_Lab{" +
                "id=" + id +
                ", id_lab=" + id_lab +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", ntel=" + ntel +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", genre='" + genre + '\'' +
                ", role='" + role + '\'' +
                ", is_blocked=" + is_blocked +
                '}';
    }
}
