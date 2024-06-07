package services;

import models.Medecin;
import models.Patient;

import models.Rendez_Vous;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RendezVousService implements IService<Rendez_Vous>{

    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    public RendezVousService(){
        cnx= MyDataBase.getInstance().getConnection();
    }
    private MedecinService medecinService = new MedecinService();
    @Override
    public void add(Rendez_Vous r) {
        String sql = "INSERT INTO rendez_vous (date,heure, motif,status,id_patients_id,id_medecins_id) VALUES (?, ?, ?,?,?,?)";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(r.getDate().getTime()));
            pst.setTime(2, new java.sql.Time(r.getDate().getTime())); // Utiliser setTime pour une colonne de type heure (time)
            pst.setString(3, r.getMotif());
            pst.setString(4, "En attente");
            pst.setInt(5, r.getPatient().getId());
            pst.setInt(6, r.getMedecin().getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Rendez_Vous rendezVous, int id) {

    }

    public List<LocalDate> getDatesDejaChoisies() {
        List<LocalDate> datesDejaChoisies = new ArrayList<>();
        String query = "SELECT DISTINCT date FROM rendez_vous";
        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Parcourir les résultats et ajouter chaque date à la liste
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("date").toLocalDate();
                datesDejaChoisies.add(date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datesDejaChoisies;
    }


    public void update(Rendez_Vous r) {
        String sql = "UPDATE rendez_vous SET date=?, heure=?, motif=?, status=? ,id_patients_id=? , id_medecins_id=? WHERE id=?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(r.getDate().getTime())); // Si votre colonne date est de type date
            pst.setTime(2, new java.sql.Time(r.getDate().getTime())); // Utiliser setTime pour une colonne de type heure (time)
            pst.setString(3, r.getMotif());
            pst.setString(4, r.getStatus());
            pst.setInt(5, r.getPatient().getId());
            pst.setInt(6, r.getMedecin().getId());
            pst.setInt(7, r.getId()); // Remplacer "id" par l'identifiant de la réclamation à mettre à jour

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM rendez_vous WHERE id=?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Rendez_Vous> getAll() {
        List<Rendez_Vous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous"; // Requête SQL pour récupérer tous les rendez-vous

        try {
            ste=cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("date");
                Time heure = rs.getTime("heure");
                String motif = rs.getString("motif");
                String status = rs.getString("status");
                int id_med = rs.getInt("id_medecins_id");
                Medecin medecinNom= medecinService.getMedecinById(id_med);

                // Création d'une instance de Rendez_Vous et ajout à la liste
                list.add(new Rendez_Vous(id, heure,date, motif, status , medecinNom));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Rendez_Vous> getRDVDataAgenda(int idPatient) {
        List<Rendez_Vous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE id_patients_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idPatient);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int idRendezVous = rs.getInt("id");
                    Date date = rs.getDate("date");
                    Time heure = rs.getTime("heure");
                    String motif = rs.getString("motif");
                    String status = rs.getString("status");

                    int idPatientFromDB = rs.getInt("id_patients_id");
                    PatientService patientService = new PatientService();
                    Patient patient = patientService.getById(idPatientFromDB);

                    Rendez_Vous rendezVous = new Rendez_Vous(idRendezVous, date, heure, motif, status, patient, 5);
                    list.add(rendezVous);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // ou autre traitement de l'exception
        }
        return list;
    }

    public List<String> getMotif() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT motif FROM rendez_vous"; // Requête SQL pour récupérer tous les rendez-vous

        try {
            ste=cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);

            while (rs.next()) {

                String motif = rs.getString("motif");
                // Création d'une instance de Rendez_Vous et ajout à la liste
                list.add(motif);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Integer> getRendezVousMedecin() {
        List<Integer> rendezVousCounts = new ArrayList<>();
        String sql = "SELECT  id_medecins_id FROM rendez_vous";

        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);

            while (rs.next()) {
                int rendezVousCount = rs.getInt("id_medecins_id");
                rendezVousCounts.add(rendezVousCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rendezVousCounts;
    }


    @Override
    public Rendez_Vous getById(int id) {
        String sql = "SELECT date, heure, motif, status FROM rendez_vous WHERE id=?";
        Rendez_Vous rendezVous = null;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Date date = rs.getDate("date");
                Time heure = rs.getTime("heure");
                String motif = rs.getString("motif");
                String status = rs.getString("status");
                int id_patient = rs.getInt("id_patients_id");
                PatientService patientService = new PatientService();
                Patient patient = patientService.getById(id_patient);
                rendezVous = new Rendez_Vous(id,heure,date, motif, status,patient);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rendezVous;
    }
    public int countRdvByPatientId(int medecinId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM rendez_vous WHERE id_patients_id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                } else {
                    throw new SQLException("Error while counting rdv for medecin ID: " + medecinId);
                }
            }
        }
    }
}