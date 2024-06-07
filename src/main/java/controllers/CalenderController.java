package controllers;
import controllers.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import models.Rendez_Vous;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import services.MedecinService;
import services.RendezVousService;
import javafx.application.Platform;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class CalenderController {
    @FXML
    private CalendarView calendarView;
    @FXML
    private Circle circle;
    public CalendarSource myCalendarSource;
    private RendezVousService rendezVousService = new RendezVousService();
    @FXML
    private DatePicker dateAjout;
    public void initialize() {
        Image img = new Image ("/img/wided.png");
        circle.setFill(new ImagePattern(img));
        createCalendarFromDatabase(2);
    }

    private void openAddRendezVousPage(LocalDate selectedDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AjouterRendezVousGui.fxml"));
            Parent root = loader.load();
            circle.getScene().setRoot(root);

// Obtenez maintenant le contrôleur de la page d'ajout de rendez-vous
            controllers.AjouterRendezVousController controller = loader.getController();


            // Mettre à jour la date dans le contrôle dateAjout de la page d'ajout de rendez-vous
            controller.setDate(selectedDate);

            // Afficher la page d'ajout de rendez-vous
            Stage stage = new Stage(); // Créer une nouvelle fenêtre
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Une erreur s'est produite lors du chargement de la page d'ajout de rendez-vous.");
            alert2.setTitle("Erreur");
            alert2.show();
        }
    }

    private void createCalendarFromDatabase(int idMedecin) {

        Locale.setDefault(Locale.FRENCH);

        List<Rendez_Vous> rdvs1 = rendezVousService.getRDVDataAgenda(idMedecin);

        Calendar birthdays = new Calendar("Mes rendez vous");
        birthdays.setReadOnly(true);
        birthdays.setStyle(Calendar.Style.STYLE1);

        // Ajouter un écouteur d'événements pour les clics de souris sur le CalendarView
        calendarView.getDayPage().getYearMonthView().setOnMouseClicked(event -> {
            // Récupérer la date sélectionnée
            LocalDate selectedDate = calendarView.getDate();
            // Vérifier si une date est sélectionnée
            if (selectedDate != null) {
                // Ouvrir la page d'ajout de rendez-vous avec la date sélectionnée
                openAddRendezVousPage(selectedDate);
            }
        });


        if (myCalendarSource == null) {
            myCalendarSource = new CalendarSource("My Calendars");
            calendarView.getCalendarSources().addAll(myCalendarSource);
        } else {
            // Sinon, vider la source du calendrier des anciens rendez-vous
            myCalendarSource.getCalendars().forEach(calendar -> calendar.clear());
        }

        // Ajouter le calendrier actuel à la source du calendrier
        myCalendarSource.getCalendars().add(birthdays);

        for (Rendez_Vous rdv : rdvs1) {
            // Date et heure du rendez-vous
            Time heure = rdv.getHeure();

            // Création de l'entrée pour le rendez-vous
            Entry<String> rdvAgenda = new Entry<>("Bonjour :" +rdv.getPatient().getNom() + " vous avez un rendez vous numero : " + rdv.getId());
            rdvAgenda.changeStartDate(LocalDate.parse(rdv.getDate().toString())); // Start date
            rdvAgenda.changeStartTime(heure.toLocalTime()); // Start time
            rdvAgenda.changeEndDate(LocalDate.parse(rdv.getDate().toString())); // End date
            rdvAgenda.changeEndTime(heure.toLocalTime().plusHours(1)); // End time: Add 1 hour to start time
            rdvAgenda.setCalendar(birthdays);
            rdvAgenda.setId(String.valueOf(rdv.getId()));

            // Ajout de l'entrée au calendrier
            birthdays.addEntry(rdvAgenda);

        }


        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

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