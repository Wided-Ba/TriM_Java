package models;

public class Patient {
    private int id;
    private Admin admin;
    private String nom;
    private String prenom;
    private int age;
    private int ntel;
    private String email;
    private String password;
    private String adresse;
    private String role;
    private String genre;
    private boolean is_blocked;

    public Patient(){};

    public Patient(Admin admin, String nom, String prenom, int age, int ntel, String email, String password, String adresse, String role, String genre, boolean is_blocked) {
        this.admin = admin;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.role = role;
        this.genre = genre;
        this.is_blocked = is_blocked;
    }
    public Patient(String nom, String prenom, int age, int ntel, String email, String password, String adresse, String role, String genre, boolean is_blocked) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.ntel = ntel;
        this.email = email;
        this.password = password;
        this.adresse = adresse;
        this.role = role;
        this.genre = genre;
        this.is_blocked = is_blocked;
    }

    public Patient(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }
    public Patient(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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
}
