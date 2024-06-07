package controllers;

import enums.EtatOrd;
import enums.TypeOrd;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Ordonnance;
import services.OrdonnanceService;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierOrdController {

    @FXML
    private Label codeError;

    @FXML
    private Label dateError;

    @FXML
    private Label descError;

    @FXML
    private Label etatError;

    @FXML
    private Label typeError;

    @FXML
    private TextArea tfDescription;

    @FXML
    private ComboBox<EtatOrd> tfEtat;

    @FXML
    private ComboBox<TypeOrd> tfType;

    @FXML
    private TextField tfCode;

    private final OrdonnanceService ordonnanceService = new OrdonnanceService();
    private Ordonnance ordodnnance;

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordodnnance = ordonnance;
        tfDescription.setText(ordonnance.getDescription());
        tfCode.setText(ordonnance.getCode());

        tfEtat.setItems(FXCollections.observableArrayList(EtatOrd.values()));
        tfEtat.getSelectionModel().select(ordonnance.getEtat());

        tfType.setItems(FXCollections.observableArrayList(TypeOrd.values()));
        tfType.getSelectionModel().select(ordonnance.getType());
    }

    @FXML
    void initialize() {
        codeError.setVisible(false);
        descError.setVisible(false);
        typeError.setVisible(false);
        etatError.setVisible(false);
    }

    @FXML
    void modifierOrd(ActionEvent event) {
        try {
            String description = tfDescription.getText();
            String code = tfCode.getText();
            EtatOrd etat = tfEtat.getValue();
            TypeOrd type = tfType.getValue();
            ordodnnance.setDescription(description);
            ordodnnance.setCode(code);
            ordodnnance.setEtat(etat);
            ordodnnance.setType(type);
            ordonnanceService.modifier(ordodnnance);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (SQLException | IOException e) {
            System.out.println("Une erreur s'est produite lors de la modification de l'ordonnance : " + e.getMessage());
        }
    }

    @FXML
    void annuler(ActionEvent event) {
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
    void toListMaladies(MouseEvent event) {
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
