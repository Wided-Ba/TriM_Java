package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
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
import services.AnalyseService;
import services.Chef_LabService;
import utils.MyDataBase;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.*;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;

public class AfficherAnalyseController implements Initializable {
    @FXML
    private GridPane AnalyseContainer;

    @FXML
    private Circle circle;
    @FXML
    private ImageView tri;

    @FXML
    private TextField recherche;
    private boolean triCroissant = true;

    private List<Analyse> analyses;
    private AnalyseService analyseService;
    private Chef_LabService chefLabService = new Chef_LabService();

    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image ("/img/1.png");
        circle.setFill(new ImagePattern(img));
        Image img2 = new Image("/img/down.png");
        tri.setImage(img2);
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        int idLab = chefLab.getId_lab();
        analyseService = new AnalyseService();
        analyses = analyseService.getByIdLab(idLab);

        // Tri des analyses par défaut dans un ordre décroissant
        Collections.sort(analyses, Comparator.comparing(Analyse::getNom));

        int column = 0;
        int row = 1;
        try {
            for (Analyse analyse : analyses) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Analyse.fxml"));
                VBox analyseBox = loader.load();
                AnalyseController analyseController = loader.getController();
                analyseController.setData(analyse);

                // Ajouter la boîte VBox à la position correcte dans le GridPane
                AnalyseContainer.add(analyseBox, column++, row);
                GridPane.setMargin(analyseBox, new Insets(19));

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
        String outillageKeyword = recherche.getText();
        String typeKeyword = recherche.getText();
        filterAnalyses(keyword, outillageKeyword, typeKeyword);
    }
    private void filterAnalyses(String keyword, String outillageKeyword, String typeKeyword) {
        AnalyseContainer.getChildren().clear();

        int column = 0;
        int row = 1;
        boolean foundMatchingAnalyse = false; // Indicateur pour suivre si une analyse correspondante est trouvée

        for (Analyse analyse : analyses) {
            if (analyse.getNom().toLowerCase().contains(keyword.toLowerCase())
                    || analyse.getOutillage().toLowerCase().contains(outillageKeyword.toLowerCase())
                    || analyse.getType().toLowerCase().contains(typeKeyword.toLowerCase())) {
                foundMatchingAnalyse = true; // Définir l'indicateur sur true si une analyse correspondante est trouvée

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Analyse.fxml"));
                    VBox analyseBox = loader.load();
                    AnalyseController analyseController = loader.getController();
                    analyseController.setData(analyse);

                    AnalyseContainer.add(analyseBox, column++, row);
                    GridPane.setMargin(analyseBox, new Insets(19));

                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Vérifier si aucune analyse correspondante n'a été trouvée
        if (!foundMatchingAnalyse) {
            Text noDataText = new Text("Aucune analyse correspondante trouvée.");
            noDataText.setFont(Font.font("Berlin Sans FB", 30));
            noDataText.setStyle("-fx-fill: #02027c;");
            Insets insets = new Insets(500, 0, 0, 500);
            GridPane.setMargin(noDataText, insets);
            AnalyseContainer.add(noDataText, 0, 0);
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
            Collections.sort(analyses, Comparator.comparing(Analyse::getNom));
        } else {
            Collections.sort(analyses, Comparator.comparing(Analyse::getNom).reversed());
        }

        // Rafraîchir l'affichage des analyses dans le GridPane
        afficherAnalysesDansGridPane();
    }

    // Méthode pour afficher les analyses dans le GridPane
    private void afficherAnalysesDansGridPane() {
        int column = 0;
        int row = 1;
        try {
            AnalyseContainer.getChildren().clear(); // Effacer d'abord le contenu existant du GridPane
            for (Analyse analyse : analyses) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Analyse.fxml"));
                VBox analyseBox = loader.load();
                AnalyseController analyseController = loader.getController();
                analyseController.setData(analyse);

                // Ajouter la boîte VBox à la position correcte dans le GridPane
                AnalyseContainer.add(analyseBox, column++, row);
                GridPane.setMargin(analyseBox, new Insets(19));

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

    //******************************switchScene******************************

    @FXML
    void switchToScene5(ActionEvent event) throws IOException {
        int idChefLab = 2;
        Chef_LabService chefLabService = new Chef_LabService();
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        int idLab = chefLab.getId_lab();System.out.println(idLab);
        if (idLab == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("Aucun laboratoire trouvé");
            alert.setContentText("Vous devez d'abord créer un laboratoire pour pouvoir ajouter des analyses.");
            alert.showAndWait();
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/views/AjouterAnalyse.fxml"));
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

