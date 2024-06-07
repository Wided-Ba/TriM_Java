package services;

import enums.EtatOrd;
import enums.TypeOrd;
import models.Ordonnance;
import models.Patient;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdonnanceService implements IService<Ordonnance>{

    private Connection connection;

    public OrdonnanceService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public List<Integer> getRendezVousIds() throws SQLException {
        List<Integer> rendezVousIds = new ArrayList<>();
        String sql = "SELECT id FROM rendez_vous";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                rendezVousIds.add(resultSet.getInt("id"));
            }
        }
        return rendezVousIds;
    }
    public List<Ordonnance> getOrdonnancesByMedecinId(int medecinId) throws SQLException {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM ordonnance WHERE id_medecins_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Ordonnance ordonnance = new Ordonnance();
                    ordonnance.setId(resultSet.getInt("id"));
                    ordonnance.setType(TypeOrd.valueOf(resultSet.getString("type")));
                    ordonnance.setEtat(EtatOrd.valueOf(resultSet.getString("etat")));

                    LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    ordonnance.setDate(dateTime);
                    ordonnance.setCode(resultSet.getString("code"));
                    ordonnance.setDescription(resultSet.getString("description"));
                    ordonnance.setIdRendezVous(resultSet.getInt("id_rendez_vous_id"));
                    ordonnance.setIdMedecin(resultSet.getInt("id_medecins_id"));
                    ordonnance.setIdPatient(resultSet.getInt("id_patients_id"));
                    ordonnance.setIdLaboratoire(resultSet.getInt("id_labs_id"));
                    ordonnance.setIdAnalyse(resultSet.getInt("analyse_list_id"));

                    ordonnances.add(ordonnance);
                }
            }
        }
        return ordonnances;
    }


    public int getMedecinId(String rendezVousId) throws SQLException {
        String sql = "SELECT id_medecins_id FROM rendez_vous WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rendezVousId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_medecins_id");
                } else {
                    throw new SQLException("Medecin not found for rendezvous ID: " + rendezVousId);
                }
            }
        }
    }

    public int getPatientId(String rendezVousId) throws SQLException {
        String sql = "SELECT id_patients_id FROM rendez_vous WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rendezVousId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_patients_id");
                } else {
                    throw new SQLException("Patient not found for rendezvous ID: " + rendezVousId);
                }
            }
        }
    }

    public Patient getPatientDetails(String rendezVousId) throws SQLException {
        String sql = "SELECT p.nom, p.prenom, p.age, p.genre, p.adresse, p.ntel " +
                "FROM patient p " +
                "INNER JOIN rendez_vous rv ON p.id = rv.id_patients_id " +
                "WHERE rv.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rendezVousId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setNom(resultSet.getString("nom"));
                    patient.setPrenom(resultSet.getString("prenom"));
                    patient.setAge(resultSet.getInt("age"));
                    patient.setGenre(resultSet.getString("genre"));
                    patient.setAdresse(resultSet.getString("adresse"));
                    patient.setNtel(Integer.parseInt(resultSet.getString("ntel")));
                    return patient;
                } else {
                    return null;
                }
            }
        }
    }
    public String getPatientNomPrenomByRendezVousId(String rendezVousId) throws SQLException {
        String nomPrenom = null;
        String sql = "SELECT CONCAT(nom, ' ', prenom) AS nom_prenom " +
                "FROM patient p " +
                "INNER JOIN rendez_vous rv ON p.id = rv.id_patients_id " +
                "WHERE rv.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rendezVousId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    nomPrenom = resultSet.getString("nom_prenom");
                }
            }
        }
        return nomPrenom;
    }




    public void ajouter(Ordonnance ordonnance) throws SQLException {
        String sql = "INSERT INTO ordonnance (type, description, date, code, etat, " +
                "id_rendez_vous_id, id_medecins_id, id_patients_id, id_labs_id,analyse_list_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(ordonnance.getType()));
            preparedStatement.setString(2, ordonnance.getDescription());
            LocalDateTime currentDate = LocalDateTime.now();
            preparedStatement.setString(3, String.valueOf(currentDate));
            //preparedStatement.setString(3, String.valueOf(ordonnance.getDate()));
            preparedStatement.setString(4, ordonnance.getCode());
            preparedStatement.setString(5, String.valueOf(ordonnance.getEtat()));
            preparedStatement.setInt(6, ordonnance.getIdRendezVous());
            preparedStatement.setInt(7, ordonnance.getIdMedecin());
            preparedStatement.setInt(8, ordonnance.getIdPatient());
            preparedStatement.setInt(9, ordonnance.getIdLaboratoire());
            preparedStatement.setInt(10, ordonnance.getIdAnalyse());
            preparedStatement.executeUpdate();
        }
    }


    public boolean ordonnanceExistsForRendezVous(String rendezVousId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM ordonnance WHERE id_rendez_vous_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, rendezVousId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                } else {
                    throw new SQLException("Error while checking for existing ordonnance for rendezvous ID: " + rendezVousId);
                }
            }
        }
    }

    public void ajouterPharmacie(Ordonnance ordonnance) throws SQLException {
        String sql = "INSERT INTO ordonnance (type, description, date, code, etat, " +
                "id_rendez_vous_id, id_medecins_id, id_patients_id, id_pharmacies_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(ordonnance.getType()));
            preparedStatement.setString(2, ordonnance.getDescription());
            preparedStatement.setString(3, String.valueOf(ordonnance.getDate()));
            preparedStatement.setString(4, ordonnance.getCode());
            preparedStatement.setString(5, String.valueOf(ordonnance.getEtat()));
            preparedStatement.setInt(6, ordonnance.getIdRendezVous());
            preparedStatement.setInt(7, ordonnance.getIdMedecin());
            preparedStatement.setInt(8, ordonnance.getIdPatient());
            preparedStatement.setInt(9, ordonnance.getIdPharmacie());
            preparedStatement.executeUpdate();
        }
    }


    public int getLabId(String nom) throws SQLException {
        String sql = "SELECT id FROM lab where nom = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, nom);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Could not find");
                }
            }
        }
    }

    public int getPharmacieId(String nom) throws SQLException {
        String sql = "SELECT id FROM pharmacie where nom = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, nom);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Could not find");
                }
            }
        }
    }

    public List<String> getAnalyseTypesByLabId(int labId) throws SQLException {
        List<String> analyseTypes = new ArrayList<>();
        String sql = "SELECT type FROM analyse WHERE id_lab_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, labId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    analyseTypes.add(resultSet.getString("type"));
                }
            }
        }
        return analyseTypes;
    }
    public int getAnalyseId(String analyseType) throws SQLException {
        String sql = "SELECT id FROM analyse WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, analyseType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Analysis type not found: " + analyseType);
                }
            }
        }
    }

    public List<String> getAllLaboratoireNames() throws SQLException {
        List<String> laboratoireNames = new ArrayList<>();
        String sql = "SELECT nom FROM lab";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                laboratoireNames.add(resultSet.getString("nom"));
            }
        }
        return laboratoireNames;
    }

    public List<String> getAllPharmacieNames() throws SQLException {
        List<String> pharmacieNames = new ArrayList<>();
        String sql = "SELECT nom FROM pharmacie";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                pharmacieNames.add(resultSet.getString("nom"));
            }
        }
        return pharmacieNames;
    }


    public void modifier(Ordonnance ordonnance) throws SQLException {
        String sql = "UPDATE ordonnance SET description=?, type=?, etat=? ,code =? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, ordonnance.getDescription());
        preparedStatement.setString(2, String.valueOf(ordonnance.getType()));
        preparedStatement.setString(3, String.valueOf(ordonnance.getEtat()));
        preparedStatement.setString(4, ordonnance.getCode());
        preparedStatement.setInt(5, ordonnance.getId());
        preparedStatement.executeUpdate();
    }


    public void supprimer(int id) throws SQLException {
        String sql = "delete from ordonnance where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


    public List<Ordonnance> recuperer() throws SQLException {
        String sql = "SELECT * FROM ordonnance";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Ordonnance> ordonnances = new ArrayList<>();
        while (rs.next()) {
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setId(rs.getInt("id"));
            ordonnance.setType(TypeOrd.valueOf(rs.getString("type")));
            ordonnance.setEtat(EtatOrd.valueOf(rs.getString("etat")));

            LocalDateTime dateTime = LocalDateTime.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ordonnance.setDate(dateTime);
            ordonnance.setCode(rs.getString("code"));
            ordonnance.setDescription(rs.getString("description"));
            ordonnances.add(ordonnance);
        }
        return ordonnances;
    }

    public int countOrdonnancesByMedecinId(int medecinId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM ordonnance WHERE id_medecins_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                } else {
                    throw new SQLException("Error while counting ordonnances for medecin ID: " + medecinId);
                }
            }
        }
    }
    public int countMaladiesByMedecinId(int medecinId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM maladie WHERE id_medecin_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                } else {
                    throw new SQLException("Error while counting maladies for medecin ID: " + medecinId);
                }
            }
        }
    }

    public int countRdvByMedecinId(int medecinId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM rendez_vous WHERE id_medecins_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
    public int countOrdonnancesByEtat(EtatOrd etat) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM ordonnance WHERE etat = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, etat.toString()); // Use enum's toString() method to get the correct string representation
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                } else {
                    throw new SQLException("Error while counting ordonnances for etat: " + etat);
                }
            }
        }
    }
    public List<Integer> getRendezVousIdsByMedecinId(int medecinId) throws SQLException {
        List<Integer> rendezVousIds = new ArrayList<>();
        String sql = "SELECT id FROM rendez_vous WHERE id_medecins_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rendezVousIds.add(resultSet.getInt("id"));
                }
            }
        }
        return rendezVousIds;
    }


    public List<Ordonnance> recupererParIdLab(int idLab) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id_labs_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idLab);
        ResultSet rs = statement.executeQuery();
        List<Ordonnance> ordonnances = new ArrayList<>();
        while (rs.next()) {
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setId(rs.getInt("id"));
            ordonnance.setType(TypeOrd.valueOf(rs.getString("type")));
            ordonnance.setEtat(EtatOrd.valueOf(rs.getString("etat")));
            ordonnance.setDate(rs.getTimestamp("date").toLocalDateTime());
            ordonnance.setCode(rs.getString("code"));
            ordonnance.setDescription(rs.getString("description"));
            ordonnance.setIdLaboratoire(rs.getInt("id_labs_id"));
            ordonnance.setIdAnalyse(rs.getInt("analyse_list_id"));
            ordonnance.setIdPatient(rs.getInt("id_patients_id"));
            ordonnances.add(ordonnance);
        }
        return ordonnances;
    }

    public Map<Month, Integer> compterOrdonnancesParMois(int idLab) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id_labs_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idLab);
        ResultSet rs = statement.executeQuery();

        Map<Month, Integer> ordonnancesParMois = new HashMap<>();
        while (rs.next()) {
            LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();
            Month mois = date.getMonth();

            // Vérifier si le mois existe déjà dans la Map, sinon l'initialiser à 0
            ordonnancesParMois.putIfAbsent(mois, 0);

            // Incrémenter le compteur du mois correspondant
            ordonnancesParMois.put(mois, ordonnancesParMois.get(mois) + 1);
        }

        return ordonnancesParMois;
    }
    public int compterOrdonnances(int idLab) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id_labs_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idLab);
        ResultSet rs = statement.executeQuery();

        int totalOrdonnances = 0;
        while (rs.next()) {
            totalOrdonnances++;
        }

        return totalOrdonnances;
    }


    public Ordonnance recupererParId(int id) throws SQLException {
        String sql = "SELECT * FROM ordonnance WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setId(rs.getInt("id"));
            ordonnance.setType(TypeOrd.valueOf(rs.getString("type")));
            ordonnance.setEtat(EtatOrd.valueOf(rs.getString("etat")));
            LocalDateTime dateTime = LocalDateTime.parse(rs.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ordonnance.setDate(dateTime);
            ordonnance.setCode(rs.getString("code"));
            ordonnance.setDescription(rs.getString("description"));
            ordonnance.setIdLaboratoire(rs.getInt("id_labs_id"));
            ordonnance.setIdAnalyse(rs.getInt("analyse_list_id"));
            ordonnance.setIdPatient(rs.getInt("id_patients_id"));
            ordonnance.setIdMedecin(rs.getInt("id_medecins_id"));
            return ordonnance;
        }
        return null; // Si aucune ordonnance n'est trouvée avec l'ID spécifié
    }


    public void updateOrdonnanceEtat(int id, EtatOrd etat) throws SQLException {
        String sql = "UPDATE ordonnance SET etat=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, String.valueOf(etat));
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
    }


    @Override
    public void add(Ordonnance ordonnance) {

    }

    @Override
    public void update(Ordonnance ordonnance, int id) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Ordonnance getById(int id) {
        return null;
    }

    @Override
    public List<Ordonnance> getAll() {
        return null;
    }
}