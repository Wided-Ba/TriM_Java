package services;

import java.sql.Connection;
import java.sql.PreparedStatement;

import models.Chef_Lab;
import models.Patient;
import utils.MyDataBase;

import java.util.List;
import java.sql.*;

public class PatientService implements IService<Patient> {

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    public PatientService() {
        cnx= MyDataBase.getInstance().getConnection();
    }
    public Patient getById2(int id) {
        String sql = "SELECT * FROM patient WHERE id=?";
        Patient patient = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int age = rs.getInt("age");
                int ntel = rs.getInt("ntel");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String adresse = rs.getString("adresse");
                String role = rs.getString("role");
                String genre = rs.getString("genre");
                Boolean is_blocked = rs.getBoolean("is_blocked");
                patient = new Patient(nom, prenom, age, ntel, email, password, adresse, role, genre, is_blocked);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patient;
    }

    @Override
    public void add(Patient patient) {

    }

    @Override
    public void update(Patient patient, int id) {

    }

    @Override
    public void delete(int id) {

    }

    public Patient getById(int id) {
        String sql = "SELECT id,nom FROM patient WHERE id=?";
        Patient patient = null;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int idPatient = rs.getInt("id");
                String nom = rs.getString("nom");
                PatientService patientService = new PatientService();
                patient = new Patient(id,nom);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  patient;
    }

    @Override
    public List<Patient> getAll() {
        return null;
    }

    public boolean verifierUtilisateurPat(String email, String mdp) {
        boolean utilisateurValide = false;

        String sql = "SELECT * FROM patient WHERE email = ?";
        try{

            pst = cnx.prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String mdpStocke = rs.getString("password");
                if (mdpStocke.equals(mdp)) {
                    utilisateurValide = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }
        return utilisateurValide;
    }
}
