package controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Ordonnance;
import services.OrdonnanceService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ordsListController implements Initializable {
    @FXML
    private GridPane grid;

    private List<Ordonnance> ordonnances;
    private OrdonnanceService ordonnanceService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ordonnanceService = new OrdonnanceService();
        int medecinId = 1;
        try {
            ordonnances = ordonnanceService.getOrdonnancesByMedecinId(medecinId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        afficherOrdsDansGridPane();
    }

    private void afficherOrdsDansGridPane () {
        int column = 0;
        int row = 1;
        grid.getChildren().clear();
        for (Ordonnance ordonnance : ordonnances) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnance.fxml"));
                VBox ordBox = loader.load();
                OrdController ordController = loader.getController();
                ordController.setData(ordonnance);
                grid.add(ordBox, column++, row);
                GridPane.setMargin(ordBox, new Insets(19));
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void toMaladiesList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to maladies: " + e.getMessage());
        }
    }


    @FXML
    void newOrd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ajouterOrd.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to ajouterOrd: " + e.getMessage());
        }
    }


    @FXML
    void toProfile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecinProfile.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to medecinProfile: " + e.getMessage());
        }
    }

}
