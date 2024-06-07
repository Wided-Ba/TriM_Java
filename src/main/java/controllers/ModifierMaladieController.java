package controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Maladie;
import services.MaladieService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ModifierMaladieController {

    @FXML
    private Label sympError;

    @FXML
    private Label descError;

    @FXML
    private TextArea tfDescription;

    @FXML
    private TextField tfNom;

    @FXML
    private TextArea tfSymptome;

    @FXML
    private TextField tfType;

    private final MaladieService maladieService = new MaladieService();
    private Maladie maladie;

    public void setMaladie(Maladie maladie) {
        this.maladie = maladie;
        tfNom.setText(maladie.getNom());
        tfDescription.setText(maladie.getDescription());
        tfSymptome.setText(maladie.getSymptome());
        tfType.setText(maladie.getType());
    }

    @FXML
    void initialize() {
        sympError.setVisible(false);
        descError.setVisible(false);
    }

    @FXML
    void modifierMaladie(ActionEvent event) throws IOException, SQLException {
        if (maladie != null && validateFields()) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Etes vous sur de modifier cette Maladie?");
            alert.setContentText("Cliquez OK pour confirmer ou Annuler pour annuler.");
            Optional<ButtonType> result = alert.showAndWait();

            // If the user confirms the update
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String nom = tfNom.getText();
                String symptome = tfSymptome.getText();
                String type = tfType.getText();
                String description = tfDescription.getText();
                maladie.setNom(nom);
                maladie.setSymptome(symptome);
                maladie.setType(type);
                maladie.setDescription(description);
                maladieService.modifier(maladie);
                System.out.println("Maladie modifiée avec succées!");
                navigateToMaladiesList(event);
            }
        } else {
            if (tfSymptome.getText().isEmpty()) {
                tfSymptome.setStyle("-fx-border-color: red;");
            }
            if (tfDescription.getText().isEmpty()) {
                tfDescription.setStyle("-fx-border-color:red;");
            }
            System.out.println("Erreur: la validation est fausse!");
        }
    }
    @FXML
    void BackToList(ActionEvent event) throws IOException {
        navigateToMaladiesList(event);
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Validate tfSymptome
        if (tfSymptome.getText().isEmpty()) {
            sympError.setText("Le symptôme est obligatoire.");
            sympError.setVisible(true);
            isValid = false;
        } else {
            sympError.setVisible(false);
        }

        // Validate tfType
        if (tfDescription.getText().isEmpty()) {
            descError.setText("La description est obligatoire.");
            descError.setVisible(true);
            isValid = false;
        } else {
            descError.setVisible(false);
        }

        return isValid;
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

    private void navigateToMaladiesList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
            System.out.println("Erreur de navigation : " + e.getMessage());
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
