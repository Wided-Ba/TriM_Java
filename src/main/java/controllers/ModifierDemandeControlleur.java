package controllers;

import enums.EtatOrd;
import enums.TypeOrd;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Analyse;
import models.Chef_Lab;
import models.Ordonnance;
import models.Patient;
import services.*;
import utils.MyDataBase;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class ModifierDemandeControlleur implements Initializable {

    @FXML
    private TextField ID;

    @FXML
    private Circle circle;

    @FXML
    private Label dateDemande;

    @FXML
    private Label descriptionDemande;

    @FXML
    private Label etatDemande;

    @FXML
    private ComboBox<String> etatdemande;

    @FXML
    private Label TypeDemande;

    @FXML
    private Label affichee;


    private Parent parent;
    private Chef_LabService chefLabService = new Chef_LabService();

    int code = 0;

    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
        etatdemande.setItems(FXCollections.observableArrayList("En_Attente", "En_Cours", "Prete"));
    }

    public void initData(TypeOrd type, LocalDateTime date, EtatOrd etat, String description,int idana, int id) {
        AnalyseService analyseService = new AnalyseService();
        TypeDemande.setText(String.valueOf(analyseService.getById(idana).getNom()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = date.format(formatter);
        dateDemande.setText(formattedDate);
        etatDemande.setText(String.valueOf(etat));
        etatdemande.setValue(String.valueOf(etat));
        descriptionDemande.setText(String.valueOf(description));
        ID.setText(String.valueOf(id)); // Assurez-vous que ID est un objet de type Label ou TextField pour afficher l'ID
    }

    @FXML
    void UpdateAnalyse(ActionEvent event) throws IOException {
        String etat = etatdemande.getValue(); // Récupérer l'état de l'analyse
        int id = Integer.parseInt(ID.getText()); // Récupérer l'ID de l'analyse
        // Créer une instance de OrdonnanceService
        OrdonnanceService ordonnanceService = new OrdonnanceService();
        PatientService patientService = new PatientService();
        MedecinService medecinService = new MedecinService();
        try {
            if (etat.equals("En_Cours")) {
                Ordonnance Ordonnance = ordonnanceService.recupererParId(id);
                String nom= patientService.getById2(Ordonnance.getIdPatient()).getNom();
                String email= patientService.getById2(Ordonnance.getIdPatient()).getEmail();
                code = generateRandomCode(1000, 9999);
                String subject = "Code de verification";
                String message = String.valueOf(code);
                EmailSender.sendEmail3(email, subject, message, nom);
                VerifCode(event);
                ordonnanceService.updateOrdonnanceEtat(id, EtatOrd.valueOf(etat));
            } else if (etat.equals("Prete")) {
                chooseFile(event);
            }
           else if (etat.equals("En_Attente")) {
                ordonnanceService.updateOrdonnanceEtat(id, EtatOrd.valueOf(etat));
                switchToScene5(event);
        }
            // Redirection vers la scène appropriée
        } catch (SQLException e) {
            // En cas d'erreur lors de la mise à jour, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur lors de la modification");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de la modification de l'état de l'ordonnance : " + e.getMessage());
            alert.showAndWait();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    int generateRandomCode(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private void chooseFile(ActionEvent event) {
        // Créer une boîte de dialogue de sélection de fichiers
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        // Définir les filtres de fichiers si nécessaire
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue et attendre que l'utilisateur choisisse un fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Récupérer le chemin absolu du fichier sélectionné
            String filePath = selectedFile.getAbsolutePath();

            // Afficher le chemin du fichier sélectionné (optionnel)
            System.out.println("Fichier sélectionné : " + filePath);

            // Appeler la méthode pour envoyer l'e-mail avec le fichier sélectionné
            try {
                // Invoquer la méthode sendEmail avec le chemin du fichier sélectionné
                sendEmailWithAttachment(filePath);
            } catch (MessagingException | IOException e) {
                // Gérer les exceptions si nécessaire
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendEmailWithAttachment(String filePath) throws MessagingException, IOException, SQLException {
        String etat = etatdemande.getValue();
        int id = Integer.parseInt(ID.getText());
        OrdonnanceService ordonnanceService = new OrdonnanceService();
        PatientService patientService = new PatientService();
        MedecinService medecinService = new MedecinService();
        Ordonnance Ordonnance = ordonnanceService.recupererParId(id);
        String nom= patientService.getById2(Ordonnance.getIdPatient()).getNom();
        String email= patientService.getById2(Ordonnance.getIdPatient()).getEmail();
        String nom2= medecinService.getMedecinById(Ordonnance.getIdMedecin()).getNom();
        String email2= medecinService.getMedecinById(Ordonnance.getIdMedecin()).getEmail();
        String codeOrd=Ordonnance.getCode();
        String subject = "Analyse terminée";
        EmailSender.sendEmail2(email2, subject, nom,nom2,codeOrd,filePath);
        EmailSender.sendEmail(email, subject,nom, filePath);
        ordonnanceService.updateOrdonnanceEtat(id, EtatOrd.valueOf(etat));
        switchToScene5(null);
    }



    //******************************switchScene******************************

    @FXML
    void switchToScene5(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DemandePrelevement.fxml"));
        Parent root = loader.load();
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheAnalyse(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficherProfil(MouseEvent event) throws IOException {
        parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/home.fxml")));
        circle.getScene().setRoot(parent);
    }

    @FXML
    void afficheprelevement(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void affichestatistiques(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Statistiques.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheLaboratoire(MouseEvent event) throws IOException {
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/afficherLab.fxml"));
        Parent parent = loader.load();
        AfficherLabController afficherLabController = loader.getController();

        int idLab = chefLab.getId_lab();
        String select = "SELECT * FROM lab WHERE id = ?";
        try {
            cnx = MyDataBase.getInstance().getConnection();
            st = cnx.prepareStatement(select);
            st.setInt(1, idLab);
            rs = st.executeQuery();
            if (rs.next()) {
                // Passer les valeurs récupérées depuis la base de données en tant que paramètres
                afficherLabController.initData(rs.getString("nom"), rs.getString("adresse"), rs.getString("ntel"), rs.getString("hdebut"), rs.getString("hfin"));
            } else {
                // Gérer le cas où aucun résultat n'est trouvé
                afficherLabController.initData(null, null, null, null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        circle.getScene().setRoot(parent);
    }

    @FXML
    void VerifCode(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/VerifCode.fxml"));
        Parent root = loader.load();
        VerifCodeController verifCodeController = loader.getController();
        verifCodeController.setCode(String.valueOf(code)); // Passer le code généré au contrôleur VerifCodeController
        circle.getScene().setRoot(root);
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

