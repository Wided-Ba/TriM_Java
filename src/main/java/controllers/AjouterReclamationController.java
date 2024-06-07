package controllers;

import javafx.stage.StageStyle;
import models.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import services.ReclamationService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class AjouterReclamationController {

    @FXML
    private Button ajouterAction;
    @FXML
    private Button ShowAction;


    @FXML
    private TextField descriptionAjout;
    @FXML
    private Circle circle;

    @FXML
    private TextField statusAjout;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label statusLabel;

    ReclamationService reclamation = new ReclamationService();
    java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
    public void initialize() {
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));

    }

    @FXML
    public void ajouterAction(ActionEvent actionEvent) {
        if (descriptionAjout.getText().length() < 3) {
            descriptionLabel.setTextFill(Color.RED); // Change la couleur du texte en rouge
            descriptionLabel.setText("La description doit contenir au moins 3 caractères.");
            if (statusAjout.getText().length() < 3) {
                statusLabel.setTextFill(Color.RED); // Change la couleur du texte en rouge
                statusLabel.setText("Le statut doit contenir au moins 3 caractères.");
                return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
            } else {
                statusLabel.setText(""); // Réinitialise le texte et la couleur
            }
            return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
        } else {
            descriptionLabel.setText(""); // Réinitialise le texte et la couleur
        }
        if (statusAjout.getText().length() < 3) {
            statusLabel.setTextFill(Color.RED); // Change la couleur du texte en rouge
            statusLabel.setText("Le statut doit contenir au moins 3 caractères.");
            if (descriptionAjout.getText().length() < 3) {
                descriptionLabel.setTextFill(Color.RED); // Change la couleur du texte en rouge
                descriptionLabel.setText("La description doit contenir au moins 3 caractères.");
                return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
            } else {
                descriptionLabel.setText(""); // Réinitialise le texte et la couleur
            }
            return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
        } else {
            statusLabel.setText(""); // Réinitialise le texte et la couleur
        }

        reclamation.add(new Reclamation(sqlDate, descriptionAjout.getText(), statusAjout.getText()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("GG");
        alert.show();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowReclamationGui.fxml"));
            descriptionAjout.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public void Show(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowReclamationGui.fxml"));
            descriptionAjout.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
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