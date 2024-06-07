package services;

import models.Maladie;
import models.Medecin;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaladieService implements IService<Maladie> {

    private Connection connection;

    public MaladieService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    public String getMedecinNomPrenomById(int medecinId) throws SQLException {
        String sql = "SELECT nom, prenom FROM medecin WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    return nom + " " + prenom;
                } else {
                    throw new SQLException("Doctor not found");
                }
            }
        }
    }




    public void ajouter(Maladie maladie) throws SQLException {
        String sql = "INSERT INTO maladie (nom , symptome ,type ,description ,id_medecin_id) VALUES (?,?,?,?,?) ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, maladie.getNom());
        preparedStatement.setString(2, maladie.getSymptome());
        preparedStatement.setString(3, maladie.getType());
        preparedStatement.setString(4, maladie.getDescription());
        preparedStatement.setInt(5, maladie.getIdMedecin());
        Statement statement = connection.createStatement();
        preparedStatement.executeUpdate();
    }
    public List<Maladie> recupererParMedecin(int medecinId) throws SQLException {
        String sql = "SELECT * FROM maladie WHERE id_medecin_id = ?";
        List<Maladie> maladies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, medecinId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Maladie maladie = new Maladie();
                    maladie.setId(rs.getInt("id"));
                    maladie.setNom(rs.getString("nom"));
                    maladie.setSymptome(rs.getString("symptome"));
                    maladie.setType(rs.getString("type"));
                    maladie.setDescription(rs.getString("description"));
                    maladie.setIdMedecin(rs.getInt("id_medecin_id"));
                    maladies.add(maladie);
                }
            }
        }
        return maladies;
    }


    //pour recuperer l'id de medecin selecté apres dans le controller
    public int getMedecinId(String nom, String prenom) throws SQLException {
        String sql = "SELECT id FROM medecin WHERE nom = ? AND prenom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Doctor not found");
                }
            }
        }
    }



    public void modifier(Maladie maladie) throws SQLException {
        String sql = "UPDATE maladie SET nom=?, symptome=?, type=?, description=?, id_medecin_id=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, maladie.getNom());
        preparedStatement.setString(2, maladie.getSymptome());
        preparedStatement.setString(3, maladie.getType());
        preparedStatement.setString(4, maladie.getDescription());
        preparedStatement.setInt(5, maladie.getIdMedecin());
        preparedStatement.setInt(6, maladie.getId());
        preparedStatement.executeUpdate();
    }


    public void supprimer(int id) throws SQLException {
        String sql = "delete from maladie where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }


    public List<Maladie> recuperer() throws SQLException {
        String sql = "SELECT maladie.*, medecin.nom AS med_nom, medecin.prenom AS med_prenom " +
                "FROM maladie " +
                "INNER JOIN medecin ON maladie.id_medecin_id = medecin.id";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Maladie> maladies = new ArrayList<>();
        while (rs.next()) {
            Maladie maladie = new Maladie();
            Medecin medecin = new Medecin();
            maladie.setId(rs.getInt("id"));
            maladie.setNom(rs.getString("nom"));
            maladie.setSymptome(rs.getString("symptome"));
            maladie.setType(rs.getString("type"));
            maladie.setDescription(rs.getString("description"));
            maladie.setIdMedecin(rs.getInt("id_medecin_id"));
            medecin.setNom(rs.getString("med_nom"));
            medecin.setPrenom(rs.getString("med_prenom"));
            maladie.setMedecin(medecin);
            maladies.add(maladie);
        }
        return maladies;
    }

    @Override
    public void add(Maladie maladie) {

    }

    @Override
    public void update(Maladie maladie, int id) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Maladie getById(int id) {
        return null;
    }

    @Override
    public List<Maladie> getAll() {
        return List.of();
    }

    public List<Maladie> recupererParNom(String nomMaladie) throws SQLException {
        String sql = "SELECT * FROM maladie WHERE nom = ?";
        List<Maladie> maladies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nomMaladie);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Maladie maladie = new Maladie();
                    maladie.setId(rs.getInt("id"));
                    maladie.setNom(rs.getString("nom"));
                    maladie.setSymptome(rs.getString("symptome"));
                    maladie.setType(rs.getString("type"));
                    maladie.setDescription(rs.getString("description"));
                    maladie.setIdMedecin(rs.getInt("id_medecin_id"));
                    maladies.add(maladie);
                }
            }
        }
        return maladies;
    }



}
