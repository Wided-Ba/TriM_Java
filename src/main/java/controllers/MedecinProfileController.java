package controllers;

import enums.EtatOrd;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.OrdonnanceService;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class MedecinProfileController {
    @FXML
    private Label num;
    @FXML
    private Circle circle;
    @FXML
    private Label num1;
    @FXML
    private Label num2;
    @FXML
    private PieChart pieChart;
    @FXML
    private AreaChart<?, ?> lineChart;
    private OrdonnanceService ordonnanceService;

    public void initialize() {
        ordonnanceService = new OrdonnanceService();
        try {
            int medecinId = 1;
            int ordonnanceCount = ordonnanceService.countOrdonnancesByMedecinId(medecinId);
            int maladieCount = ordonnanceService.countMaladiesByMedecinId(medecinId);
            int rdvCount = ordonnanceService.countRdvByMedecinId(medecinId);

            int ordonnanceEnAttenteCount = ordonnanceService.countOrdonnancesByEtat(EtatOrd.En_Attente);
            int ordonnancePreteCount = ordonnanceService.countOrdonnancesByEtat(EtatOrd.Prete);

            // Update PieChart
            pieChart.getData().clear();
            PieChart.Data enAttenteData = new PieChart.Data("En Attente (" + ordonnanceEnAttenteCount + ")", ordonnanceEnAttenteCount);
            PieChart.Data preteData = new PieChart.Data("Prete (" + ordonnancePreteCount + ")", ordonnancePreteCount);
            pieChart.getData().addAll(enAttenteData, preteData);

            // Update labels
            num.setText(String.valueOf(ordonnanceCount));
            num1.setText(String.valueOf(maladieCount));
            num2.setText(String.valueOf(rdvCount));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void toOrdList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to ordList: " + e.getMessage());
        }
    }


    @FXML
    void toMaladies(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
