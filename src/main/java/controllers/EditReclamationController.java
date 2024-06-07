package controllers;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Reclamation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import services.ReclamationService;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class EditReclamationController {


    @FXML
    private Button EditReclamation;
    @FXML
    private Label descriptionLabelEdit;
    @FXML
    private Label statusLabelEdit;
    @FXML
    private TextField descriptionEdit;
    @FXML
    private Circle circle;
    @FXML
    private TextField statusEdit;

    private Reclamation reclamation;
    ReclamationService reclamationService=new ReclamationService();

    public void initialize() {
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));

    }
    @FXML
    void EditReclamation(ActionEvent event) {
        if (reclamation != null && reclamationService!= null) {
            String nouveauStatus = statusEdit.getText();
            String nouvelleDescription = descriptionEdit.getText();
            reclamation.setDescription(nouvelleDescription);
            reclamation.setStatus(nouveauStatus);
            reclamation.setDaterec(java.sql.Date.valueOf(java.time.LocalDate.now()));

            try {

                if (descriptionEdit.getText().length() < 3) {
                    descriptionLabelEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                    descriptionLabelEdit.setText("La description doit contenir au moins 3 caractères.");
                    if (statusEdit.getText().length() < 3) {
                        statusLabelEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                        statusLabelEdit.setText("Le statut doit contenir au moins 3 caractères.");
                        return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
                    } else {
                        statusLabelEdit.setText(""); // Réinitialise le texte et la couleur
                    }
                    return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
                } else {
                    descriptionLabelEdit.setText(""); // Réinitialise le texte et la couleur
                }

                if (statusEdit.getText().length() < 3) {
                    statusLabelEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                    statusLabelEdit.setText("Le statut doit contenir au moins 3 caractères.");
                    if (descriptionEdit.getText().length() < 3) {
                        descriptionLabelEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                        descriptionLabelEdit.setText("La description doit contenir au moins 3 caractères.");
                        return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
                    } else {
                        descriptionLabelEdit.setText(""); // Réinitialise le texte et la couleur
                    }
                    return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
                } else {
                    statusLabelEdit.setText(""); // Réinitialise le texte et la couleur
                }

                reclamationService.update(reclamation);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Actualité modifiée avec succès !");
                successAlert.setTitle("Modification réussie");
                successAlert.show();

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Erreur lors de la modification de l'actualité hhh: " + e.getMessage());
                errorAlert.setTitle("Erreur de modification");
                errorAlert.show();
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/ShowReclamationGui.fxml"));
                descriptionEdit.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sorry");
                alert.setTitle("Error");
                alert.show();
            }
        }
        else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier l'actualité car aucune actualité n'est sélectionnée ");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
    }
    public void SetData(Reclamation reclamation)
    {
        if (reclamation != null) {
            this.reclamation = reclamation;
            descriptionEdit.setText(reclamation.getDescription());
            statusEdit.setText(reclamation.getStatus());
        }

    }

    public void SetReclamationService(ReclamationService reclamationService)
    {
        this.reclamationService=reclamationService;

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