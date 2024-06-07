package services;

import models.Reclamation;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IService<Reclamation>{
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    public ReclamationService(){
        cnx= MyDataBase.getInstance().getConnection();
    }
public void add(Reclamation r)
{
    String sql="insert into reclamation(daterec,description,status) value ('"+new java.sql.Date(r.getDaterec().getTime())+"','"+r.getDescription()+"','"+r.getStatus()+"')";
    try {
        ste=cnx.createStatement();
        ste.executeUpdate(sql);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    @Override
    public void update(Reclamation reclamation, int id) {

    }

    public void update(Reclamation r) {
        String sql = "UPDATE reclamation SET daterec=?, description=?, status=? WHERE id=?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(r.getDaterec().getTime()));
            pst.setString(2, r.getDescription());
            pst.setString(3, r.getStatus());
            pst.setInt(4, r.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reclamation WHERE id=?";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> list=new ArrayList<>();
        String sql="select * from reclamation";
        try {
            ste=cnx.createStatement();
            ResultSet rs=ste.executeQuery(sql);
            while(rs.next())
            {
                list.add(new Reclamation(rs.getInt(1),
                        rs.getDate(3),
                        rs.getString(4),
                        rs.getString(5)));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    @Override
    public Reclamation getById(int id) {
        String sql = "SELECT daterec, description, status FROM reclamation WHERE id=?";
        Reclamation reclamation = null;

        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Date daterec = rs.getDate("daterec");
                String description = rs.getString("description");
                String status = rs.getString("status");

                // Création d'une nouvelle instance de Reclamation avec les données récupérées
                reclamation = new Reclamation(daterec, description, status);
            }

            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reclamation;
    }

    /*public void addPST(Reclamation r) {
        String sql = "INSERT INTO reclamation (daterec, description, status) VALUES (?, ?, ?)";

        try {
            pst = cnx.prepareStatement(sql);
            pst.setDate(1, new java.sql.Date(r.getDaterec().getTime()));
            pst.setString(2, r.getDescription());
            pst.setString(3, r.getStatus());

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

}
