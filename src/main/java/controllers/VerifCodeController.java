package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VerifCodeController implements Initializable {

    @FXML
    private HBox Profile;

    @FXML
    private HBox afficheAnalyse;

    @FXML
    private HBox afficheLaboratoire;

    @FXML
    private Button btnupdate;

    @FXML
    private Circle circle;

    @FXML
    private TextField code;

    @FXML
    private HBox prelevement;

    @FXML
    private HBox statistiques;

    @FXML
    private VBox vbox3;

    @FXML
    private VBox vbox5;
    private String generatedCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
    }
    public void setCode(String code) {
        generatedCode = code;
    }
    @FXML
    void confirmer(ActionEvent event) throws IOException {
        String enteredCode = code.getText();

        if (enteredCode.equals(generatedCode)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("L'état de l'ordonnance a été mis à jour avec succès.");
            alert.showAndWait();
            retour(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Code invalide");
            alert.setHeaderText(null);
            alert.setContentText("Le code entré est incorrect. Veuillez réessayer.");
            alert.showAndWait();
            retour(event);
        }

    }

    //******************************switchScene******************************
    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
        circle.getScene().setRoot(root);
    }

}
