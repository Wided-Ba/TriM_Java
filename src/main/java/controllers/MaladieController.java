package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Maladie;
import services.MaladieService;


import java.io.IOException;
import java.sql.SQLException;


public class MaladieController {

    @FXML
    private Label idMaladie;


    @FXML
    private VBox maladieBox;

    @FXML
    private Label nomMaladie;

    @FXML
    private Label symptomeMaladie;

    @FXML
    private Label typeMaladie;

    private Maladie maladie;
    private final MaladieService maladieService = new MaladieService();

    public void setData(Maladie maladie){
        this.maladie = maladie;
        nomMaladie.setText(maladie.getNom());
        symptomeMaladie.setText(maladie.getSymptome());
        typeMaladie.setText(maladie.getType());
        idMaladie.setText(String.valueOf(maladie.getId()));
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/editMaladie.fxml"));
            Parent root = loader.load();
            ModifierMaladieController editController = loader.getController();
            editController.setMaladie(this.maladie);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        try {
            int maladieId = Integer.parseInt(idMaladie.getText());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete this entry?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        maladieService.supprimer(maladieId);
                        Stage stage = (Stage) maladieBox.getScene().getWindow();
                        stage.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
                        Parent root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (SQLException | IOException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            });
        } catch (NumberFormatException e) {
            System.err.println("Error parsing maladie ID: " + e.getMessage());
        }
    }






}
