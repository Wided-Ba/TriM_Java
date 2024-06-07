package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.Analyse;
import models.Ordonnance;
import services.AnalyseService;
import services.OrdonnanceService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DemandeController {
    @FXML
    private Label datedemande;


    @FXML
    private Label etat;

    @FXML
    private Label idDemande;


    @FXML
    private Label typeAnalyse;

    @FXML
    private Circle circle;
    private Stage stage;
    private Scene scene;
    private Parent parent;



    public void setData(Ordonnance ordonnance){
        AnalyseService analyseService = new AnalyseService();
        typeAnalyse.setText(String.valueOf(analyseService.getById(ordonnance.getIdAnalyse()).getNom()));
        LocalDateTime date = ordonnance.getDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        datedemande.setText(formattedDate);
        etat.setText(String.valueOf(ordonnance.getEtat()));
        idDemande.setText(String.valueOf(ordonnance.getId()));
    }


    @FXML
    void UpdateDemande(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierDemande.fxml"));
        Parent parent = loader.load();
        int idDemandeToUpdate = Integer.parseInt(idDemande.getText());
        OrdonnanceService ordonnanceService = new OrdonnanceService();
        Ordonnance demandeToUpdate = ordonnanceService.recupererParId(idDemandeToUpdate);
        if (demandeToUpdate  != null) {
            ModifierDemandeControlleur modifierdemandeController = loader.getController();
            // Passer les données d'analyse à la scène de modification
            modifierdemandeController.initData(demandeToUpdate.getType(), demandeToUpdate.getDate(), demandeToUpdate.getEtat(), demandeToUpdate.getDescription(),demandeToUpdate.getIdAnalyse(),idDemandeToUpdate);
        } else {
            // Gérer le cas où l'analyse n'est pas trouvée
            System.out.println("Aucune demande trouvée pour l'ID " + idDemande);
        }
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(parent);

    }


    //******************************switchScene******************************

    @FXML
    void afficheprelevement(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        circle.getScene().setRoot(root);
    }

}
