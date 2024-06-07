package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;
import models.Chef_Lab;
import models.Laboratoire;
import netscape.javascript.JSObject;
import services.Chef_LabService;
import services.LabService;
import javafx.scene.control.Alert;
import utils.MyDataBase;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

public class AjouterLabController implements Initializable {

    @FXML
    private TextField adresselab;

    @FXML
    private Label errorAdresse;

    @FXML
    private Label errorNom;

    @FXML
    private Label errorNtel;

    @FXML
    private Label errorhd;

    @FXML
    private TextField Hrdebut;

    @FXML
    private TextField Hrfin;

    @FXML
    private TextField Mndebut;

    @FXML
    private TextField Mnfin;

    @FXML
    private TextField nomlab;

    @FXML
    private TextField numlab;

    @FXML
    private Pane map;

    @FXML
    private Circle circle;
    private LabService labService = new LabService();
    private Chef_LabService chefLabService = new Chef_LabService();

    private WebView webView;
    private WebEngine webEngine;
    private SimpleStringProperty selectedAddressProperty = new SimpleStringProperty();
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;


    public void initialize(URL location, ResourceBundle resources) {
        try{
            Image img = new Image("/img/1.png");
            circle.setFill(new ImagePattern(img));
            webView = new javafx.scene.web.WebView();
            webEngine = webView.getEngine();

            // Charger la page HTML contenant la carte Leaflet
            String htmlFilePath = Objects.requireNonNull(getClass().getResource("/html/map2.html")).toExternalForm();
            webEngine.load(htmlFilePath);

            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("app", this);
                }
            });

            // Set the dimensions of the map Pane
            map.setPrefWidth(600); // Desired width in pixels
            map.setPrefHeight(520); // Desired height in pixels
            webView.setPrefSize(map.getPrefWidth(), map.getPrefHeight());

            map.getChildren().add(webView); // Add the WebView to your map Pane

            selectedAddressProperty.addListener((observable, oldValue, newValue) -> {
                System.out.println("Adresse sélectionnée : " + newValue);
                adresselab.setText(newValue);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public SimpleStringProperty getSelectedAddressProperty() {
        return selectedAddressProperty;
    }

    public String updateSelectedAddress(String address) {
        selectedAddressProperty.set(address);
        return address;
    }


    @FXML
    void creatLab(ActionEvent event) throws IOException {
        // Récupérer les valeurs entrées par l'utilisateur
        String nom = nomlab.getText();
        String adresse = adresselab.getText();
        String ntel = numlab.getText();
        String hrdebut = Hrdebut.getText();
        String hrfin = Hrfin.getText();
        String mndebut = Mndebut.getText();
        String mnfin = Mnfin.getText();

        // Vérifier si les champs de saisie sont vides
        if (nom.isEmpty() || adresse.isEmpty() || ntel.isEmpty() || hrdebut.isEmpty() || hrfin.isEmpty() || mndebut.isEmpty() || mnfin.isEmpty()) {
            errorNom.setText("Champ obligatoire");
            errorAdresse.setText("Champ obligatoire");
            errorNtel.setText("Champ obligatoire");
            errorhd.setText("Champ obligatoire");
            return;
        }

        // Vérifier si le nom contient au moins 4 caractères
        if (nom.length() < 4) {
            errorNom.setText("Le nom du laboratoire doit contenir au moins 4 caractères.");
            return;
        }

        // Vérifier si l'adresse contient au moins 4 caractères
        if (adresse.length() < 4) {
            errorNom.setText("");
            errorAdresse.setText("L'adresse du laboratoire doit contenir au moins 4 caractères.");
            return; // Sortir de la méthode si l'adresse est trop courte
        }

        // Vérifier si le numéro de téléphone contient uniquement 8 chiffres
        if (!ntel.matches("\\d{8}")) {
            errorAdresse.setText("");
            errorNtel.setText("Le numéro de téléphone doit contenir uniquement 8 chiffres.");
            return;
        }

        // Vérifier le format des heures de début et de fin
        if (!hrdebut.matches("([01]?[0-9]|2[0-3])") || !hrfin.matches("([01]?[0-9]|2[0-3])")) {
            errorNtel.setText("");
            errorhd.setText("Format incorrect pour les heures. Utilisez une valeur entre 00 et 23.");
            return;
        }

        // Vérifier le format des minutes de début et de fin
        if (!mndebut.matches("([0-5]?[0-9])") || !mnfin.matches("([0-5]?[0-9])")) {
            errorNtel.setText("");
            errorhd.setText("Format incorrect pour les minutes. Utilisez une valeur entre 00 et 59.");
            return;
        }

        if (hrfin.compareTo(hrdebut) <= 0) {
            errorNtel.setText("");
            errorhd.setText("L'heure de fin doit être postérieure à l'heure de début.");
            return;
        }

        // Convertir les heures et les minutes en objets Time
        Time hdebut = Time.valueOf(hrdebut + ":" + mndebut + ":00");
        Time hfin = Time.valueOf(hrfin + ":" + mnfin + ":00");

        Laboratoire lab = new Laboratoire();
        lab.setNom(nom);
        lab.setAdresse(adresse);
        lab.setNtel(Integer.parseInt(ntel));
        lab.setHdebut(hdebut);
        lab.setHfin(hfin);

        labService.add(lab);
        errorNom.setText("");
        errorAdresse.setText("");
        errorNtel.setText("");
        errorhd.setText("");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout réussi");
        alert.setHeaderText(null);
        alert.setContentText("Le laboratoire a été ajouté avec succès.");
        alert.showAndWait();
        switchToScene1(event);
    }

    @FXML
    void map(ActionEvent event) {

    }

    //******************************switchScene******************************

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
    void afficheAnalyse(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
    }
    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
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

