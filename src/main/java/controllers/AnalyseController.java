package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Analyse;
import services.AnalyseService;
import java.io.IOException;
import java.util.Objects;

public class AnalyseController {

    @FXML
    private Label nomAnalyse;

    @FXML
    private Label outillageAnalyse;

    @FXML
    private Label typeAnalyse;

    @FXML
    private Label idAnalyse;
    private Stage stage;
    private Parent parent;



    public void setData(Analyse analyse){
        nomAnalyse.setText(analyse.getNom());
        outillageAnalyse.setText(analyse.getOutillage());
        typeAnalyse.setText(analyse.getType());
        idAnalyse.setText(String.valueOf(analyse.getId()));
    }

    @FXML
    void DeleteAnalyse(ActionEvent event) throws IOException {
        int idAnalyseToDelete = Integer.parseInt(idAnalyse.getText());

        // Afficher une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette analyse ?");
        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");
        confirmationAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                try {
                    AnalyseService analyseService = new AnalyseService();
                    analyseService.delete(idAnalyseToDelete);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Analyse supprimée");
                    alert.setHeaderText(null);
                    alert.setContentText("L'analyse a été supprimée avec succès.");
                    alert.showAndWait();
                    // Actualiser l'affichage des analyses après la suppression
                    afficheAnalyse(event);
                } catch (Exception e) {
                    System.out.println("Une erreur s'est produite lors de la suppression du laboratoire : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void UpdateAnalyse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifierAnalyse.fxml"));
        Parent parent = loader.load();
        int idAnalyseToUpdate = Integer.parseInt(idAnalyse.getText());
        AnalyseService analyseService = new AnalyseService();
        Analyse analyseToUpdate = analyseService.getById(idAnalyseToUpdate);
        if (analyseToUpdate  != null) {
            ModifierAnalyseControlleur modifierAnalyseController = loader.getController();
            // Passer les données d'analyse à la scène de modification
            modifierAnalyseController.initData(analyseToUpdate.getNom(), analyseToUpdate.getType(), analyseToUpdate.getOutillage(), analyseToUpdate.getConseils(),idAnalyseToUpdate);
        } else {
            // Gérer le cas où l'analyse n'est pas trouvée
            System.out.println("Aucune analyse trouvée pour l'ID " + idAnalyse);
        }
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(parent);

    }


    //******************************switchScene******************************

    void afficheAnalyse(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/AfficherAnalyse.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(parent);
    }

}
