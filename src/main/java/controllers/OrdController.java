package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Ordonnance;
import models.RendezVous;
import services.OrdonnanceService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class OrdController {

    @FXML
    private Label dateOrd;
    @FXML
    private Label descO;

    @FXML
    private Label etatOrd;

    @FXML
    private Label idOrd;

    @FXML
    private VBox maladieBox;
    @FXML
    private Label nomPatient;


    @FXML
    private Label typeOrd;
    private Ordonnance ordonnance;

    private final OrdonnanceService ordonnanceService = new OrdonnanceService();

    public void setData(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
        typeOrd.setText(String.valueOf(ordonnance.getType()));
        etatOrd.setText(String.valueOf(ordonnance.getEtat()));
        descO.setText((ordonnance.getDescription()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = ordonnance.getDate().format(formatter);
        dateOrd.setText(formattedDateTime);
        idOrd.setText(String.valueOf(ordonnance.getId()));
        try {
            String rendezVousId = String.valueOf(ordonnance.getIdRendezVous());
            String nomPrenomPatient = ordonnanceService.getPatientNomPrenomByRendezVousId(rendezVousId);
            nomPatient.setText(nomPrenomPatient);
        } catch (SQLException e) {
            System.err.println("Error fetching patient name: " + e.getMessage());
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modifierOrd.fxml"));
            Parent root = loader.load();
            ModifierOrdController modifierOrdController = loader.getController();
            modifierOrdController.setOrdonnance(this.ordonnance);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        try {
            int ordId = Integer.parseInt(idOrd.getText());

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete this entry?");
            confirmationAlert.setContentText("This action cannot be undone.");
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ordonnanceService.supprimer(ordId);
                        Stage stage = (Stage) maladieBox.getScene().getWindow();
                        stage.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
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
