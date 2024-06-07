package controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Medecin;
import models.Patient;
import models.Rendez_Vous;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import services.MedecinService;
import services.RendezVousService;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AjouterRendezVousController {

    @FXML
    private Button Show;
    @FXML
    private ChoiceBox<String> specialiteMed;
    @FXML
    private ChoiceBox<String> NomMed;
    @FXML
    private Label label;
    @FXML
    private Label labelMotif;
    @FXML
    private Circle circle;

    @FXML
    private Label labelNomMed;
    @FXML
    private Label labelDate;
    @FXML
    private Button ajouterRendezAction;

    @FXML
    private DatePicker dateAjout;

    @FXML
    private ChoiceBox<String> motifAjout;
    private MedecinService medecinService = new MedecinService();
    public void initialize() {
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));
        // Initialiser les valeurs du ChoiceBox
        motifAjout.getItems().addAll("Consultation", "Visite", "Controle"); // Ajoutez autant de valeurs que nécessaire
        specialiteMed.getItems().addAll("Dentiste", "Generaliste", "Pediatre"); // Ajoutez autant de valeurs que nécessaire
        // Ajouter un écouteur d'événements pour détecter les changements de sélection
        specialiteMed.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            List<String> medecins = medecinService.getMedecinsBySpecialite(newValue);

            NomMed.getItems().clear();

            NomMed.getItems().addAll(medecins);
        });

        List<LocalDate> datesDejaChoisies = rendezVousService.getDatesDejaChoisies();

        // Définir la date minimale comme étant la date actuelle
        dateAjout.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 || datesDejaChoisies.contains(date));
            }
        });
    }
    public void setDate(LocalDate date) {
        dateAjout.setValue(date);
    }
    RendezVousService rendezVousService=new RendezVousService();
    @FXML
    void ajouterRendezAction(ActionEvent event) {

        Time heure = Time.valueOf("10:30:00");
        Patient patient=new Patient(2);

        String specialite = specialiteMed.getValue();
        if (specialite == null || specialite.isEmpty()) {
            label.setTextFill(Color.RED); // Change la couleur du texte en rouge
            label.setText("Veuillez sélectionner une specialite");
            return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
        } else {
            label.setText(""); // Réinitialise le texte et la couleur

        }

        String nomMedecin = NomMed.getValue();
        if (nomMedecin == null || nomMedecin.isEmpty()) {
            labelNomMed.setTextFill(Color.RED); // Change la couleur du texte en rouge
            labelNomMed.setText("Veuillez sélectionner un nom de medecin");
            return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
        } else {
            labelNomMed.setText(""); // Réinitialise le texte et la couleur

        }
        String motif = motifAjout.getValue();
        if (motif== null || motif.isEmpty()) {
            labelMotif.setTextFill(Color.RED); // Change la couleur du texte en rouge
            labelMotif.setText("Veuillez sélectionner un motif");
            return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
        } else {
            labelMotif.setText(""); // Réinitialise le texte et la couleur

        }
        LocalDate selectedDate = dateAjout.getValue();
        if (selectedDate == null ) {
            labelDate.setTextFill(Color.RED);
            labelDate.setText("Veuillez sélectionner une date");
            return;
        } else {
            // Réinitialiser le texte et la couleur si une date est sélectionnée
            labelDate.setText("");
        }
        // Utiliser le nom pour récupérer l'ID correspondant
        int idMedecin = medecinService.getIdMedecinByNom(nomMedecin);
        // Utiliser l'ID pour créer un objet Medecin
        Medecin medecin = new Medecin(idMedecin);
        LocalDate date = dateAjout.getValue(); // Récupérer la date sélectionnée du DatePicker
        java.sql.Date sqlDatee = java.sql.Date.valueOf(date);
        rendezVousService.add(new Rendez_Vous(heure, sqlDatee, motifAjout.getValue().toString(), patient,medecin));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("L'ajout a été effectué avec succès! ");
        alert.show();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowRendezVousGui.fxml"));
            motifAjout.getScene().setRoot(root);
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
    @FXML
    void Show(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ShowRendezVousGui.fxml"));
            motifAjout.getScene().setRoot(root);
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