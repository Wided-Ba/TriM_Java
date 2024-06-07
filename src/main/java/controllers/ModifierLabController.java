package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import models.Chef_Lab;
import models.Laboratoire;
import org.json.JSONException;
import org.json.JSONObject;
import services.Chef_LabService;
import services.LabService;
import utils.MyDataBase;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifierLabController implements Initializable {

    @FXML
    private TextField adresselab;

    @FXML
    private Label errorAdresse;

    @FXML
    private Label errorNom;

    @FXML
    private TextField Hrdebut;

    @FXML
    private TextField Hrfin;

    @FXML
    private TextField Mndebut;

    @FXML
    private TextField Mnfin;

    @FXML
    private Label errorNtel;

    @FXML
    private Label errorhd;

    @FXML
    private TextField nomlab;

    @FXML
    private TextField numlab;

    @FXML
    private Pane map;

    @FXML
    private Circle circle;
    private Chef_LabService chefLabService = new Chef_LabService();
    private WebView webView;
    private WebEngine webEngine;
    private SimpleStringProperty selectedAddressProperty = new SimpleStringProperty();
    private String initialLabName;
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            Image img = new Image("/img/1.png");
            circle.setFill(new ImagePattern(img));

            webView = new javafx.scene.web.WebView();
            webEngine = webView.getEngine();

            // Charger la page HTML contenant la carte Leaflet
            String htmlFilePath = Objects.requireNonNull(getClass().getResource("/html/map2.html")).toExternalForm();
            webEngine.load(htmlFilePath);

            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    // Récupérer les coordonnées de l'adresse sélectionnée via une API de géocodage (par exemple, Nominatim)
                    String selectedAddress = adresselab.getText();
                    System.out.println("Adresse récupérée : " + selectedAddress);
                    if (!selectedAddress.isEmpty()) {
                        try {
                            URL url = new URL("https://nominatim.openstreetmap.org/search?format=json&q=" + URLEncoder.encode(selectedAddress, StandardCharsets.UTF_8));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();
                            JSONArray jsonArray = new JSONArray(response.toString());
                            if (!jsonArray.isEmpty()) {
                                JSONObject firstResult = jsonArray.getJSONObject(0);
                                double latitude = firstResult.getDouble("lat");
                                double longitude = firstResult.getDouble("lon");
                                // Centrer la carte sur les coordonnées récupérées et ajouter un marqueur
                                String jsScript = String.format("map.setView([%f, %f], 13); currentMarker = L.marker([%f, %f]).addTo(map);", latitude, longitude, latitude, longitude);
                                webEngine.executeScript(jsScript);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Set the dimensions of the map Pane
            map.setPrefWidth(600); // Desired width in pixels
            map.setPrefHeight(520); // Desired height in pixels
            webView.setPrefSize(map.getPrefWidth(), map.getPrefHeight());

            map.getChildren().add(webView); // Add the WebView to your map Pane
            System.out.println("Adresse sélectionnée : ");
            selet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selet(){
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
    }


    public SimpleStringProperty getSelectedAddressProperty() {
        return selectedAddressProperty;
    }

    public String updateSelectedAddress(String address) {
        selectedAddressProperty.set(address);
        return address;
    }
    public void initData2(String nom, String adresse, String ntel, String hdebut, String hfin) {
        nomlab.setText(nom);
        adresselab.setText(adresse);
        numlab.setText(ntel);
        String[] hdebutParts = hdebut.split(":");
        Hrdebut.setText(hdebutParts[0]);
        Mndebut.setText(hdebutParts[1]);
        String[] hfinParts = hfin.split(":");
        Hrfin.setText(hfinParts[0]);
        Mnfin.setText(hfinParts[1]);
        initialLabName = nom;
    }

    @FXML
    public void updateLab(ActionEvent event) throws IOException {
        // Récupérer les nouvelles valeurs entrées par l'utilisateur
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

        // Obtenir l'ID du laboratoire par son nom
        LabService labService = new LabService();
        int labId = labService.getLabIdByName(initialLabName);

        // Vérifier si l'ID du laboratoire est valide
        if (labId != 0) {
            errorNom.setText("");
            errorAdresse.setText("");
            errorNtel.setText("");
            errorhd.setText("");
            Laboratoire lab = new Laboratoire(nom, adresse, Integer.parseInt(ntel), Time.valueOf(String.valueOf(hdebut)), Time.valueOf(String.valueOf(hfin)));
            labService.update(lab, labId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("Les informations du laboratoire ont été mises à jour avec succès.");
            alert.showAndWait();
            switchToScene1(event);
        } else {
            System.out.println("Aucun laboratoire trouvé avec le nom : " + initialLabName);
        }
    }

    //******************************switchScene******************************

    @FXML
    void switchToScene1(ActionEvent event) throws IOException {
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
