package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Analyse;
import models.Chef_Lab;
import services.AnalyseService;
import services.Chef_LabService;
import utils.MyDataBase;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierAnalyseControlleur implements Initializable {

    @FXML
    private TextField Outillage;

    @FXML
    private TextField ID;

    @FXML
    private Circle circle;

    @FXML
    private TextField Conseils;

    @FXML
    private Label errorCs;

    @FXML
    private Label errorNom;

    @FXML
    private Label errorOtill;

    @FXML
    private Label errorType;

    @FXML
    private TextField nomAnalyse;

    @FXML
    private ComboBox<String> TypeAnalys;

    private Parent parent;
    private Chef_LabService chefLabService = new Chef_LabService();

    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
        TypeAnalys.setItems(FXCollections.observableArrayList("Urinaire", "Sanguin", "Urinaire", "Fécal(e)", "Liquide céphalorachidien (LCR)", "Salivaire"));
    }

    public void initData(String nom, String type, String outillage, String conseils, int id) {
        nomAnalyse.setText(nom);
        TypeAnalys.setValue(type);
        Conseils.setText(conseils);
        Outillage.setText(outillage);;
        ID.setText(String.valueOf(id));
    }

    @FXML
    void UpdateAnalyse(ActionEvent event) throws IOException {
        String nom = nomAnalyse.getText();
        String type = TypeAnalys.getValue();
        String outillage = Outillage.getText();
        String conseilsText = Conseils.getText();
        int id = Integer.parseInt(ID.getText());
        // Vérification des champs vides
        if (nom.isEmpty() || type.isEmpty() || outillage.isEmpty() || conseilsText.isEmpty()) {
            errorNom.setText("Ce champ est obligatoire.");
            errorType.setText("Ce champ est obligatoire.");
            errorOtill.setText("Ce champ est obligatoire.");
            errorCs.setText("Ce champ est obligatoire.");
            return;
        }
        if (nom.length() < 4) {
            errorNom.setText("Le nom d'analyse doit contenir au moins 4 caractères.");
            return;
        }

        if (type.length() < 4) {
            errorNom.setText("");
            errorType.setText("Le type d'analyse doit contenir au moins 4 caractères.");
            return; // Sortir de la méthode si l'adresse est trop courte
        }

        if (outillage.length() < 4) {
            errorType.setText("");
            errorOtill.setText("Le outillage d'analyse doit contenir au moins 4 caractères.");
            return;
        }

        if (conseilsText.length() < 4 ) {
            errorOtill.setText("");
            errorCs.setText("Le conseil d'analyse doit contenir au moins 4 caractères.");
            return;
        }

        // Mise à jour de l'analyse
        AnalyseService analyseService = new AnalyseService();
        Analyse analyse = new Analyse(nom, type, outillage, conseilsText);
        analyseService.update(analyse,id);

        // Affichage d'un message d'information approprié en fonction du résultat de la mise à jour
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les informations du laboratoire ont été mises à jour avec succès.");
            alert.showAndWait();
            switchToScene5(event);
    }

    //******************************switchScene******************************

    @FXML
    void switchToScene5(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheAnalyse(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
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
    void affichestatistiques(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Statistiques.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheLaboratoire(MouseEvent event) throws IOException {
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
