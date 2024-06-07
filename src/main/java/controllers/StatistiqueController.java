package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Rendez_Vous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import services.RendezVousService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class StatistiqueController {
    @FXML
    private PieChart pieChart;
    @FXML
    private Label num;
    @FXML
    private Label num1;
    @FXML
    private Label num2;
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private Circle circle;
    private final RendezVousService rendezVousService = new RendezVousService();

    public void initialize() throws SQLException {
        int id_patient=2;
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int consultationCount = countRendezVousByMotif("Consultation");
        int visiteCount = countRendezVousByMotif("Visite");
        int controleCount = countRendezVousByMotif("Controle");

        int rdvCount = rendezVousService.countRdvByPatientId(id_patient);
        num.setText("5");
        num1.setText("2");
        num2.setText(String.valueOf(rdvCount));
// Ajout des données à la PieChart
        pieChartData.addAll(
                new PieChart.Data("Consultation", consultationCount),
                new PieChart.Data("Visite", visiteCount),
                new PieChart.Data("Controle", controleCount)
        );

// Configuration de la PieChart
        pieChart.setData(pieChartData);
        pieChart.setTitle("Répartition des rendez-vous par motif");

        Map<String, Integer> motifCounts = countRendezVousByMotif();

        // Créer une série de données pour le BarChart
        XYChart.Series series = new XYChart.Series();
        series.setName("Rendez-vous par motif");

        // Ajouter les données à la série
        for (Map.Entry<String, Integer> entry : motifCounts.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série au BarChart
        barChart.getData().add(series);

        // Configuration du titre du BarChart
        barChart.setTitle("Répartition des rendez-vous par motif");



    }
    private int countRendezVousByMotif(String motif) {
        int count = 0;
        List<String> motifs = rendezVousService.getMotif();
        for (String m : motifs) { // Itérer sur la liste de motifs
            if (m.equals(motif)) { // Comparaison avec le motif
                count++;
            }
        }
        return count;
    }

    private Map<String, Integer> countRendezVousByMotif() {
        Map<String, Integer> motifCounts = new HashMap<>();
        List<String> motifs = rendezVousService.getMotif();
        for (String motif : motifs) {
            motifCounts.put(motif, motifCounts.getOrDefault(motif, 0) + 1);
        }
        return motifCounts;
    }

    @FXML
    void AjouterRendezVous(MouseEvent event) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowRendezVousGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
    @FXML
    void AjouterReclamation(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowReclamationGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
    @FXML
    void Calendrier(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/CalenderGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
    @FXML
    void Statistique(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/StatistiqueGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
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