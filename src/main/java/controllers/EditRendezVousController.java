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
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import services.MedecinService;
import services.RendezVousService;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EditRendezVousController {

    @FXML
    private Circle circle;

    @FXML
    private Button EditRendezVous;
    @FXML
    private DatePicker dateEdit;

    @FXML
    private Label labelEdit;

    @FXML
    private Label labelMotifEdit;

    @FXML
    private Label labelNomMedEdit;
    @FXML
    private Label labelDateEdit;
    @FXML
    private ChoiceBox<String> motifEdit;

    @FXML
    private ChoiceBox<String > NomMedEdit;
    @FXML
    private ChoiceBox<String> specialiteMedEdit;
    private Rendez_Vous rendezVous;
    RendezVousService rendezVousService =  new RendezVousService();
    private MedecinService medecinService = new MedecinService();
    public void initialize() {
        Image img = new Image("/img/wided.png");
        circle.setFill(new ImagePattern(img));

        List<LocalDate> datesDejaChoisies = rendezVousService.getDatesDejaChoisies();
        // Définir la date minimale comme étant la date actuelle
        dateEdit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 || datesDejaChoisies.contains(date));
            }
        });
    }
    @FXML
    void EditRendezVous(ActionEvent event) {
        if (rendezVous != null && rendezVousService!= null) {
            String specialite = specialiteMedEdit.getValue();
            if (specialite == null || specialite.isEmpty()) {
                labelEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                labelEdit.setText("Veuillez sélectionner une specialite");
                return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
            } else {
                labelEdit.setText(""); // Réinitialise le texte et la couleur

            }

            String nomMedecin = NomMedEdit.getValue();
            if (nomMedecin == null || nomMedecin.isEmpty()) {
                labelNomMedEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                labelNomMedEdit.setText("Veuillez sélectionner un nom de medecin");
                return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
            } else {
                labelNomMedEdit.setText(""); // Réinitialise le texte et la couleur

            }
            String motif = motifEdit.getValue();
            if (motif== null || motif.isEmpty()) {
                labelMotifEdit.setTextFill(Color.RED); // Change la couleur du texte en rouge
                labelMotifEdit.setText("Veuillez sélectionner un motif");
                return; // Arrêter l'exécution de la méthode si la condition n'est pas satisfaite
            } else {
                labelMotifEdit.setText(""); // Réinitialise le texte et la couleur

            }
            LocalDate selectedDate = dateEdit.getValue();
            if (selectedDate == null ) {
                labelDateEdit.setTextFill(Color.RED);
                labelDateEdit.setText("Veuillez sélectionner une date");
                return;
            } else {
                // Réinitialiser le texte et la couleur si une date est sélectionnée
                labelDateEdit.setText("");
            }
            String nouvellemotif = motifEdit.getValue();
            String nouvelNomMed = NomMedEdit.getValue();
            Time heure = Time.valueOf("10:30:00");

            LocalDate date = dateEdit.getValue();
            java.sql.Date sqlDatee = java.sql.Date.valueOf(date);
            Patient patient=new Patient(2);
            rendezVous.setMotif(nouvellemotif);
            rendezVous.setPatient(patient);
            rendezVous.setDate(sqlDatee);
            int idMedecin = medecinService.getIdMedecinByNom(nomMedecin);
            // Utiliser l'ID pour créer un objet Medecin
            Medecin medecin = new Medecin(idMedecin);
            rendezVous.setMedecin(medecin);

            try {

                rendezVousService.update(rendezVous);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Actualité modifiée avec succès !");
                successAlert.setTitle("Modification réussie !");
                successAlert.show();

            } catch (Exception e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("mochkl: " + e.getMessage());
                errorAlert.setTitle("mochkl");
                errorAlert.show();
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/views/ShowRendezVousGui.fxml"));
                motifEdit.getScene().setRoot(root);
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
    public void SetDataRendezVous(Rendez_Vous rendezVous) {
        dateEdit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        List<LocalDate> datesDejaChoisies = rendezVousService.getDatesDejaChoisies();
        // Définir la date minimale comme étant la date actuelle
        dateEdit.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 || datesDejaChoisies.contains(date));
            }
        });
        if (rendezVous != null && dateEdit != null) {
            this.rendezVous = rendezVous;
            java.sql.Date sqlDate = (java.sql.Date) rendezVous.getDate();
            LocalDate rendezVousDate = sqlDate.toLocalDate();
            dateEdit.setValue(rendezVousDate);
            motifEdit.setValue(rendezVous.getMotif());
            motifEdit.getItems().addAll("Consultation", "Visite", "Controle"); // Ajoutez autant de valeurs que nécessaire
            NomMedEdit.setValue(rendezVous.getMedecin().getNom());
            specialiteMedEdit.setValue((rendezVous.getMedecin().getSpecialite()));
            specialiteMedEdit.getItems().addAll("Dentiste", "Generaliste", "Pediatre");

            specialiteMedEdit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

                List<String> medecins = medecinService.getMedecinsBySpecialite(newValue);
                // Effacer les anciennes valeurs du ChoiceBox NomMed
                NomMedEdit.getItems().clear();

                // Remplir le ChoiceBox NomMed avec les nouveaux noms de médecins
                NomMedEdit.getItems().addAll(medecins);

            });
            // Assurez-vous que dateEdit.getValue() n'est pas null avant de l'utiliser
            LocalDate date = dateEdit.getValue(); // Récupérer la date sélectionnée du DatePicker
            if (date != null) {
                java.util.Date sqlDatee = java.sql.Date.valueOf(date);
                rendezVous.setDate(sqlDatee);
            } else {
                // Gérer le cas où la date est nulle (éventuellement afficher un message d'erreur)
            }
        }
    }
    public void SetRendezVousService(RendezVousService rendezVousService)
    {
        this.rendezVousService=rendezVousService;

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