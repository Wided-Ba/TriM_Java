package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.Chef_LabService;
import models.Chef_Lab;
import javafx.scene.control.Label;
import services.LabService;
import utils.MyDataBase;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherLabController implements Initializable {


    @FXML
    private Label textlabAdr;

    @FXML
    private Circle circle;

    @FXML
    private Label textlabhd;

    @FXML
    private Label textlabhf;

    @FXML
    private Label textlaberreur;

    @FXML
    private Label textlabnom;

    @FXML
    private Label textlabnum;
    private Parent parent;
    private Chef_LabService chefLabService = new Chef_LabService();

    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
    }

    public void initData(String nom, String adresse, String ntel, String hdebut, String hfin) {
        if (nom == null || adresse == null || ntel == null || hdebut == null || hfin == null) {
            textlaberreur.setText("Pour commencer votre expérience avec TriM, veuillez créer un laboratoire.");
        } else {
            textlabnom.setText(nom);
            textlabAdr.setText(adresse);
            textlabnum.setText(ntel);
            String heureDebutFormatee = hdebut.substring(0, 5);
            String heureFinFormatee = hfin.substring(0, 5);
            textlabhd.setText(heureDebutFormatee);
            textlabhf.setText(heureFinFormatee);
        }
    }

    public void DeleteLab(ActionEvent event) {
        int idChefLab = 2;
        Chef_Lab chefLab2 = chefLabService.getById(idChefLab);
        int idLab = chefLab2.getId_lab();
        if (idLab == 0) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce laboratoire ?");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Afficher l'alerte et attendre la réponse de l'utilisateur
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    LabService labService = new LabService();
                    labService.delete(idLab);
                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Suppression réussie");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Le laboratoire a été supprimé avec succès.");
                    successAlert.showAndWait();
                    switchToScene4(event);
                } catch (Exception e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du laboratoire : " + e.getMessage());
                }
            }
        });
    }

    //******************************switchScene******************************
    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierLab.fxml"));
        Parent parent = loader.load();
        ModifierLabController modifierLabController = loader.getController();

        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        int idLab = chefLab.getId_lab();
        if (idLab == 0) {
            return;
        }
        String select = "SELECT * FROM lab WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idLab);
            rs = st.executeQuery();
            // Passer les valeurs récupérées depuis la base de données en tant que paramètres
            if (rs.next()) {
                modifierLabController.initData2(rs.getString("nom"), rs.getString("adresse"), rs.getString("ntel"), rs.getString("hdebut"), rs.getString("hfin")); // Appelez la méthode sur l'instance de ModifierLabController
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                System.out.println("Aucun laboratoire trouvé pour l'ID " + idLab);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        circle.getScene().setRoot(parent);
    }
    @FXML
    public void switchToScene3(ActionEvent event) throws IOException {
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        if (chefLab.getId_lab() == 0) {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AjouterLab.fxml")));
            circle.getScene().setRoot(parent);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Vous ne pouvez avoir qu'un seul laboratoire associé à votre compte.");
            alert.showAndWait();
        }

    }
    @FXML
    public void switchToScene4(ActionEvent event) throws IOException {
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherLab.fxml"));
        Parent parent = loader.load();
        AfficherLabController afficherLabController = loader.getController();

        int idLab = chefLab.getId_lab();
        String select = "SELECT * FROM lab WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idLab);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherLabController.initData(rs.getString("nom"), rs.getString("adresse"), rs.getString("ntel"), rs.getString("hdebut"), rs.getString("hfin"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherLabController.initData(null, null, null, null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        circle.getScene().setRoot(parent);
    }
    @FXML
    void afficheAnalyse(MouseEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherAnalyse.fxml")));
        circle.getScene().setRoot(parent);
    }

    @FXML
    void afficherProfil(MouseEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(parent);
    }

    @FXML
    void afficheprelevement(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void deconnecter(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");
        alert.setContentText("Cliquez sur OK pour vous déconnecter ou Annuler pour rester connecté.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Main.fxml")));
            Scene scene = new Scene(parent);
            Image image = new Image("/img/favicon.png");
            Stage oldStage = (Stage) circle.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.getIcons().add(image);
            newStage.setTitle("TriM");
            scene.setFill(Color.TRANSPARENT);
            newStage.setScene(scene);
            newStage.initStyle(StageStyle.TRANSPARENT);
            newStage.setWidth(980);
            newStage.setHeight(680);
            newStage.setX(oldStage.getX());
            newStage.setY(oldStage.getY());
            newStage.setMaximized(oldStage.isMaximized());
            newStage.show();
            oldStage.close();
        }
    }


}
