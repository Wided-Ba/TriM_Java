package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Analyse;
import models.Chef_Lab;
import models.Ordonnance;
import services.Chef_LabService;
import services.OrdonnanceService;
import utils.MyDataBase;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import enums.EtatOrd;



public class DemandePrelevementController implements Initializable {
    @FXML
    private GridPane DemandeContainer;
    @FXML
    private Circle circle;
    @FXML
    private ImageView tri;
    @FXML
    private TextField recherche;
    private boolean triCroissant = true;
    private List<Ordonnance> ordonnances;
    private OrdonnanceService ordonnanceService;
    private Chef_LabService chefLabService = new Chef_LabService();
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
        Image img2 = new Image("/img/down.png");
        tri.setImage(img2);
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        int idLab = chefLab.getId_lab();
        ordonnanceService = new OrdonnanceService();
        try {
            // Récupérer toutes les ordonnances
            ordonnances = ordonnanceService.recupererParIdLab(idLab);

            // Filtrer les ordonnances avec l'état "En_cours" ou "En_attente"
            ordonnances = ordonnances.stream()
                    .filter(ordonnance -> ordonnance.getEtat() == EtatOrd.En_Cours || ordonnance.getEtat() == EtatOrd.En_Attente)
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Tri des analyses par défaut dans un ordre décroissant
        Collections.sort(ordonnances, Comparator.comparing(Ordonnance::getDate));
        int column = 0;
        int row = 1;
        try {
            for (Ordonnance ordonnance : ordonnances) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Demande.fxml"));
                VBox demandeBox = loader.load();
                DemandeController demandeController = loader.getController();
                demandeController.setData(ordonnance);

                // Ajouter la boîte VBox à la position correcte dans le GridPane
                DemandeContainer.add(demandeBox, column++, row);
                GridPane.setMargin(demandeBox, new Insets(19));

                // Mettre à jour les colonnes et les lignes si nécessaire
                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void recherche(KeyEvent event) {
        String keyword = recherche.getText();
        String datedemandeKeyword = recherche.getText();
        String etatKeyword = recherche.getText();
        filterOrd(keyword, datedemandeKeyword, etatKeyword);
    }

    private void filterOrd(String keyword, String datedemandeKeyword, String etatKeyword) {
        DemandeContainer.getChildren().clear();

        int column = 0;
        int row = 1;
        boolean found = false; // Variable pour suivre si une ordonnance correspondante est trouvée

        for (Ordonnance ordonnance : ordonnances) {
            if (ordonnance.getType().name().toLowerCase().contains(keyword.toLowerCase())
                    || ordonnance.getDate().toString().toLowerCase().contains(datedemandeKeyword.toLowerCase())
                    || ordonnance.getEtat().name().toLowerCase().contains(etatKeyword.toLowerCase())) {
                found = true; // Une ordonnance correspondante a été trouvée
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Demande.fxml"));
                    VBox demandeBox = loader.load();
                    DemandeController demandeController = loader.getController();
                    demandeController.setData(ordonnance);

                    DemandeContainer.add(demandeBox, column++, row);
                    GridPane.setMargin(demandeBox, new Insets(19));

                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Vérifiez si aucune ordonnance correspondante n'a été trouvée
        if (!found) {

            Text noDataText = new Text("Aucune ordonnance correspondante trouvée.");
            noDataText.setFont(Font.font("Berlin Sans FB", 30));
            noDataText.setStyle("-fx-fill: #02027c;");
            Insets insets = new Insets(500, 0, 0, 500);
            GridPane.setMargin(noDataText, insets);
            DemandeContainer.add(noDataText, 0, 0);

        }
    }
    @FXML
    void tri(MouseEvent event) {
        // Mettre à jour l'image en fonction de l'état du tri
        String imagePath = triCroissant ? "/img/up.png" : "/img/down.png";
        Image img2 = new Image(imagePath);
        tri.setImage(img2);

        // Mettre à jour l'état du tri
        triCroissant = !triCroissant;

        // Effectuer le tri
        if (triCroissant) {
            Collections.sort(ordonnances, Comparator.comparing(Ordonnance::getDate));
        } else {
            Collections.sort(ordonnances, Comparator.comparing(Ordonnance::getDate).reversed());
        }

        // Rafraîchir l'affichage des analyses dans le GridPane
        afficherDemandeDansGridPane();
    }

    private void afficherDemandeDansGridPane() {
        int column = 0;
        int row = 1;
        try {
            DemandeContainer.getChildren().clear(); // Effacer d'abord le contenu existant du GridPane
            for (Ordonnance ordonnance : ordonnances) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Demande.fxml"));
                VBox demandeBox = loader.load();
                DemandeController demandeController = loader.getController();
                demandeController.setData(ordonnance);

                DemandeContainer.add(demandeBox, column++, row);
                GridPane.setMargin(demandeBox, new Insets(19));

                if (column == 3) {
                    column = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    void afficherProfil(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
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
