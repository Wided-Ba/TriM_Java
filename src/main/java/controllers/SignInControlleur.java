package controllers;

import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.Chef_LabService;
import javafx.scene.control.Alert;
import services.MedecinService;
import services.PatientService;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignInControlleur {
    @FXML
    private Circle circle;

    @FXML
    private Button connecter;

    @FXML
    private TextField email;

    @FXML
    private TextField mdp;
    private Chef_LabService chefLabService = new Chef_LabService();
    private MedecinService medecinService = new MedecinService();
    private PatientService patientService = new PatientService();


    @FXML
    void connecter(ActionEvent event) throws IOException {
        String emailText = email.getText();
        String mdpText = mdp.getText();

        String utilisateurType = "";

        // Vérifier d'abord en tant que médecin
        if (medecinService.verifierUtilisateurMed(emailText, mdpText)) {
            utilisateurType = "medecin";
        }
        // Si l'utilisateur n'est pas un médecin, vérifier en tant que patient
        else if (patientService.verifierUtilisateurPat(emailText, mdpText)) {
            utilisateurType = "patient";
        }
        // Si l'utilisateur n'est ni un médecin ni un patient, vérifier en tant que chef de laboratoire
        else if (chefLabService.verifierUtilisateur(emailText, mdpText)) {
            utilisateurType = "chefLab";
        }

        if (!utilisateurType.isEmpty()) {
            String fxmlPath = "";
            switch (utilisateurType) {
                case "medecin":
                    fxmlPath = "/views/medecinProfile.fxml";
                    break;
                case "patient":
                    fxmlPath = "/views/ShowRendezVousGui.fxml";
                    break;
                case "chefLab":
                    fxmlPath = "/views/home.fxml";
                    break;
                default:
                    break;
            }

            if (!fxmlPath.isEmpty()) {
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
                Scene scene = new Scene(parent);
                Image image = new Image("/img/favicon.png");
                Stage oldStage = (Stage) connecter.getScene().getWindow();
                Stage newStage = new Stage();
                newStage.getIcons().add(image);
                newStage.setTitle("TriM");
                newStage.setScene(scene);
                newStage.initStyle(StageStyle.DECORATED);
                newStage.setWidth(1315);
                newStage.setHeight(880);
                newStage.setX(oldStage.getX());
                newStage.setY(oldStage.getY());
                newStage.setMaximized(oldStage.isMaximized());
                newStage.show();
                oldStage.close();
            }
        } else {
            // Afficher un message d'erreur si l'utilisateur n'est pas valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("L'email ou le mot de passe est incorrect !");
            alert.showAndWait();
        }
    }

}


