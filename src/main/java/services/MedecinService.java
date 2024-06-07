package services;

import models.Medecin;
import models.Patient;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedecinService implements IService<Medecin> {
    private Connection cnx;
    public MedecinService(){
        cnx= MyDataBase.getInstance().getConnection();
    }


    @Override
    public void add(Medecin medecin) {

    }

    @Override
    public void update(Medecin medecin, int id) {

    }

    @Override
    public void delete(int id) {

    }
    @Override
    public Medecin getById(int id) {
        String req = "SELECT * FROM medecin WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Supposons que id_muni est le nom de la colonne contenant l'ID de Muni dans la table end_user
                int id_muni = rs.getInt("id_muni");
                // Vous devez récupérer les détails de Muni en utilisant son ID ici
                return new Medecin(id);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Medecin> getAll() {
        return null;
    }


    public Medecin getMedecinById2(int id) {
        Medecin medecin = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les informations du médecin depuis le résultat de la requête
                String nom = resultSet.getString("nom");
                String email = resultSet.getString("email");

                // Créer une instance de l'entité Medecin
                medecin = new Medecin(nom, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement, etc.)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecin;
    }

    public Medecin getMedecinById(int id) {
        Medecin medecin = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les informations du médecin depuis le résultat de la requête
                int medecinId = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String specialite = resultSet.getString("specialite");

                // Créer une instance de l'entité Medecin
                medecin = new Medecin(medecinId, nom, specialite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement, etc.)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecin;
    }

    // Méthode pour récupérer les noms des médecins par spécialité depuis la base de données
    public List<String> getMedecinsBySpecialite(String specialite) {
        List<String> medecins = new ArrayList<>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Requête SQL pour récupérer les noms des médecins par spécialité
        String query = "SELECT nom FROM medecin WHERE specialite = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setString(1, specialite);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nomMedecin = resultSet.getString("nom");
                medecins.add(nomMedecin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources (ResultSet et PreparedStatement seulement)
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return medecins;
    }

    public int getIdMedecinByNom(String nom) {
        int idMedecin = -1; // Valeur par défaut si le médecin n'est pas trouvé

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Requête SQL pour récupérer l'ID du médecin par son nom
        String query = "SELECT id FROM medecin WHERE nom = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setString(1, nom);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                idMedecin = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return idMedecin;
    }

    public String getMedecinNomById(int id) {
        String nom = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String query = "SELECT nom FROM medecin WHERE id = ?";

        try {
            statement = cnx.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                // Récupérer le nom du médecin depuis le résultat de la requête
                nom = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermer les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // Ne pas fermer la connexion ici pour éviter l'erreur "Connection closed"
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return nom;
    }

    public boolean verifierUtilisateurMed(String email, String mdp) {
        boolean utilisateurValide = false;

        String sql = "SELECT * FROM medecin WHERE email = ?";
        PreparedStatement statement = null;
        try{

            statement = cnx.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
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