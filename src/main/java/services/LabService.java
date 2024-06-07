package services;

import models.Chef_Lab;
import models.Laboratoire;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabService implements IService<Laboratoire> {

    private Connection cnx;
    private PreparedStatement pst;
    private Chef_LabService chefLabService = new Chef_LabService();

    public LabService(){
        cnx= MyDataBase.getInstance().getConnection();
    }
    @Override
    public void add(Laboratoire lab) {
        String insert = "INSERT INTO lab(nom,adresse,ntel,hdebut,hfin) VALUES (?,?,?,?,?)";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            pst = cnx.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, lab.getNom());
            pst.setString(2, lab.getAdresse());
            pst.setInt(3, lab.getNtel());
            pst.setTime(4, lab.getHdebut());
            pst.setTime(5, lab.getHfin());

            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idLab = generatedKeys.getInt(1);
                int idChefLab = 2;
                Chef_Lab chefLab = chefLabService.getById(idChefLab);
                chefLab.setId_lab(idLab);
                chefLabService.update(chefLab);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Laboratoire lab, int id) {
        String updateQuery = "UPDATE lab SET nom = ?, adresse = ?, ntel = ?, hdebut = ?, hfin = ? WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            pst = cnx.prepareStatement(updateQuery);
            pst.setString(1, lab.getNom());
            pst.setString(2, lab.getAdresse());
            pst.setInt(3, lab.getNtel());
            pst.setTime(4, lab.getHdebut());
            pst.setTime(5, lab.getHfin());
            pst.setInt(6, id);
            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM lab WHERE id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Laboratoire getById(int id) {
        String sql = "SELECT * FROM lab WHERE id=?";
        Laboratoire lab = null;
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                int ntel = rs.getInt("ntel");
                Time heureDebut = rs.getTime("hdebut");
                Time heureFin = rs.getTime("hfin");

                lab = new Laboratoire(nom, adresse, ntel, heureDebut, heureFin);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lab;
    }

    @Override
    public List<Laboratoire> getAll() {
        List<Laboratoire> labs = new ArrayList<>();
        String sql = "SELECT * FROM lab";
        try {
            pst = cnx.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                int ntel = rs.getInt("ntel");
                Time heureDebut = rs.getTime("hdebut");
                Time heureFin = rs.getTime("hfin");
                Laboratoire lab = new Laboratoire(id, nom, adresse, ntel, heureDebut, heureFin);
                labs.add(lab);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return labs;
    }

    public int getLabIdByName(String nom) {
        String sql = "SELECT id FROM lab WHERE nom = ?";
        int idLab = 0;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                idLab = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return idLab;
    }
}
