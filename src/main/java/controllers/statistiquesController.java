package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import models.Chef_Lab;
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
import java.time.Month;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class statistiquesController implements Initializable {
    @FXML
    private Circle circle;
    private Parent parent;
    @FXML
    private Pane chart;
    private Chef_LabService chefLabService = new Chef_LabService();
    private OrdonnanceService ordonnanceService;
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        ordonnanceService = new OrdonnanceService();
        int idLab = chefLab.getId_lab();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Mois");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'ordonnances");

        // Calculer le maximum des valeurs de l'axe y
        int maxValue = 0; // Vous devez implémenter getMaxValue() pour obtenir la valeur maximale
        try {
            maxValue = getMaxValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Définir l'échelle personnalisée pour l'axe y
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0); // Définir la limite inférieure de l'axe y à 0
        yAxis.setUpperBound(maxValue); // Ajouter une marge de 10 à la valeur maximale
        yAxis.setTickUnit(1); // Définir l'unité de tic sur 10 pour multiplier par 10

        // Définir un formateur d'étiquettes pour l'axe des nombres (yAxis)
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                // Formater le nombre sans virgule
                return String.format("%.0f", object);
            }
        });

        Map<Month, Integer> ordonnancesParMois = null;
        try {
            ordonnancesParMois = ordonnanceService.compterOrdonnancesParMois(idLab);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Convertir la carte en une liste pour la trier
        List<Map.Entry<Month, Integer>> entries = new ArrayList<>(ordonnancesParMois.entrySet());

        // Trier la liste des entrées par ordre croissant de mois
        Collections.sort(entries, Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Month, Integer> entry : entries) {
            Month mois = entry.getKey();
            int nombreOrdonnances = entry.getValue();
            series.getData().add(new XYChart.Data<>(mois.toString(), nombreOrdonnances));
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);
        barChart.setTitle("Évolution du nombre d'ordonnances dans le mois");
        series.setName("Nombre d'ordonnances");
        for (XYChart.Data<String, Number> data : series.getData()) {
            Node node = data.getNode();
            String barColor = "#007ACC"; // Couleur bleue
            node.setStyle("-fx-bar-fill: " + barColor + ";");
        }


        // Modifier les couleurs du graphique
        String chartColor = "-fx-bar-fill: #007ACC;"; // Bleu
        barChart.setStyle(chartColor);


        StackPane pane = new StackPane(barChart);
        StackPane.setAlignment(barChart, Pos.CENTER);
        StackPane.setMargin(barChart, new Insets(10)); // Optionnel : définir une marge autour du graphique
        chart.getChildren().clear();
        chart.getChildren().add(pane);
    }



    private int getMaxValue() throws SQLException {
        int maxValue = 0;
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        ordonnanceService = new OrdonnanceService();
        int idLab = chefLab.getId_lab();
        Map<Month, Integer> ordonnancesParMois = ordonnanceService.compterOrdonnancesParMois(idLab);
        for (Map.Entry<Month, Integer> entry : ordonnancesParMois.entrySet()) {
            int nombreOrdonnances = entry.getValue();
            maxValue ++;
        }
        return maxValue;
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
        // Afficher la scène
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
    void affichestatistiques(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Statistiques.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficherProfil(MouseEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(parent);
    }
}
