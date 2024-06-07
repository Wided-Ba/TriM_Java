package services;

import models.Analyse;
import models.Laboratoire;
import utils.MyDataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnalyseService implements IService<Analyse>{

    private Connection cnx;
    private PreparedStatement pst;

    public AnalyseService(){
        cnx= MyDataBase.getInstance().getConnection();
    }

    public void addAnalyse(Analyse analyse, int id) {
        String sql="insert into analyse(nom,type,outillage,conseils,id_lab_id) values (?,?,?,?,?)";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1,analyse.getNom());
            pst.setString(2,analyse.getType());
            pst.setString(3,analyse.getOutillage());
            pst.setString(4,analyse.getConseils());
            pst.setInt(5,id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void add(Analyse analyse) {

    }

    @Override
    public void update(Analyse analyse, int id) {
        String sql = "UPDATE analyse SET nom=?, type=?, outillage=?, conseils=? WHERE id=?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, analyse.getNom());
            pst.setString(2, analyse.getType());
            pst.setString(3, analyse.getOutillage());
            pst.setString(4, analyse.getConseils());
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int compterAnalyses(int idLab) throws SQLException {
        String sql = "SELECT * FROM analyse WHERE id_lab_id = ?";
        pst = cnx.prepareStatement(sql);
        pst.setInt(1, idLab);
        ResultSet rs = pst.executeQuery();

        int totalAnalyses = 0;
        while (rs.next()) {
            totalAnalyses++;
        }

        return totalAnalyses;
    }



    @Override
    public void delete(int id) {
        String sql = "DELETE FROM analyse WHERE id=?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Analyse getById(int id) {
        String sql = "SELECT * FROM analyse WHERE id=?";
        Analyse analyse = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String type = rs.getString("type");
                String outillage = rs.getString("outillage");
                String conseils = rs.getString("conseils");
                int idLab = rs.getInt("id_lab_id");

                LabService labService = new LabService();
                Laboratoire lab = labService.getById(idLab);

                // Création d'une instance d'Analyse avec les données récupérées
                analyse = new Analyse(nom, type, outillage, conseils, lab);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return analyse;
    }


    @Override
    public List<Analyse> getAll() {
        List<Analyse> analyses = new ArrayList<>();
        String sql = "SELECT * FROM analyse";
        try {
            pst = cnx.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String type = rs.getString("type");
                String outillage = rs.getString("outillage");
                String conseils = rs.getString("conseils");
                int idLab = rs.getInt("id_lab_id");
                LabService labService = new LabService();
                Laboratoire lab = labService.getById(idLab);
                Analyse analyse = new Analyse(id,nom, type, outillage, conseils, lab);
                analyses.add(analyse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return analyses;
    }

    public List<Analyse> getByIdLab(int idLab) {
        List<Analyse> analyses = new ArrayList<>();
        String sql = "SELECT * FROM analyse WHERE id_lab_id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, idLab);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String type = rs.getString("type");
                String outillage = rs.getString("outillage");
                String conseils = rs.getString("conseils");

                LabService labService = new LabService();
                Laboratoire lab = labService.getById(idLab);
                Analyse analyse = new Analyse(id, nom, type, outillage, conseils, lab);
                analyses.add(analyse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return analyses;
    }

}
