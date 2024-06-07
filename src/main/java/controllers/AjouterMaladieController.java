package controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterMaladieController implements Initializable {
    private final MaladieService maladieService = new MaladieService();



    @FXML
    private Label descError;
    @FXML
    private Label sympError;
    @FXML
    private Label nomError;
    @FXML
    private Label typeError;

    @FXML
    private TextArea tfDescription;

    @FXML
    private TextField tfNom;

    @FXML
    private TextArea tfSymptome;
    @FXML
    private Label nomMed;

    @FXML
    private TextField tfType;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            nomError.setVisible(false);
            sympError.setVisible(false);
            descError.setVisible(false);
            typeError.setVisible(false);
            String medecinNomPrenom = maladieService.getMedecinNomPrenomById(1);
            nomMed.setText(medecinNomPrenom);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        if (tfNom.getText().isEmpty()) {
            nomError.setText("Le nom est obligatoire.");
            nomError.setVisible(true);
            isValid = false;
        } else {
            if (tfNom.getText().length() < 3) {
                nomError.setText("Le nom doit contenir au moins 3 caractères.");
                nomError.setVisible(true);
                isValid = false;
            } else {
                nomError.setVisible(false);
            }
        }
        if (tfType.getText().isEmpty()) {
            typeError.setVisible(true);
            isValid = false;
        } else {
            typeError.setVisible(false);
        }

        if (tfSymptome.getText().isEmpty()) {
            sympError.setVisible(true);
            isValid = false;
        } else {
            sympError.setVisible(false);
        }

        if (tfDescription.getText().isEmpty()) {
            descError.setVisible(true);
            isValid = false;
        } else {
            descError.setVisible(false);
        }
        return isValid;
    }

    @FXML
    void ajouterMaladie(ActionEvent event) {
        if (!validateFields()) {

            if (tfNom.getText().isEmpty()) {
                tfNom.setStyle("-fx-border-color: red; -fx-border-radius: 10;");
            }
            if (tfType.getText().isEmpty()) {
                tfType.setStyle("-fx-border-color: red;-fx-border-radius: 10;");
            }
            if (tfSymptome.getText().isEmpty()) {
                tfSymptome.setStyle("-fx-border-color: red;");
            }
            if (tfDescription.getText().isEmpty()) {
                tfDescription.setStyle("-fx-border-color:red;");
            }
            return;
        }
        try {
            int medecinId = 1;
            Maladie maladie = new Maladie();
            maladie.setNom(tfNom.getText());
            maladie.setType(tfType.getText());
            maladie.setSymptome(tfSymptome.getText());
            maladie.setDescription(tfDescription.getText());
            maladie.setIdMedecin(medecinId);
            maladieService.ajouter(maladie);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Maladie ajoutée avec succès.");
            alert.showAndWait();
            naviguezVersListMaladies(event);
            //sinon
        } catch (SQLException e) {
            System.out.println("Une erreur s'est produite lors de l'ajout de la maladie : " + e.getMessage());
        }
    }

    @FXML
    void naviguezVersListMaladies(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
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
    void toOrdList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (Exception e) {
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
            System.out.println("Error navigating to profile: " + e.getMessage());
        }
    }

}
