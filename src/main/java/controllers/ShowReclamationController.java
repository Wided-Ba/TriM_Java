package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import models.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ReclamationService;
import javafx.scene.control.ListView;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import com.itextpdf.text.BaseColor;
import javafx.scene.text.Font;


// Utilisation de la classe Font de JavaFX avec le nom complet



public class ShowReclamationController {

    @FXML
    private Button Ajouter_Reclamation;
    @FXML
    private Circle circle;
    private ReclamationService rs = new ReclamationService();
    @FXML
    private Button btnGenererPDF;
    @FXML
    private ListView<Reclamation> ListReclamation;
    @FXML
    private TextField addEmployee_search;

    @FXML
    private Button AjouterReclam;
    private ObservableList<Reclamation> filteredList;

    public void initialize() {
        Image img = new Image("/img/wided.png");
        circle.setFill(new ImagePattern(img));
//******************************recherchee*************************************************
        filteredList = FXCollections.observableArrayList();
        filteredList.addAll(ListReclamation.getItems()); // Utilisation de getItems() pour obtenir la liste actuelle
        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) -> {
            // Si le nouveau texte est vide, cela signifie que du texte a été supprimé
            if (newValue.isEmpty()) {
                filteredList.clear();
                showAll();
            } else {
                // Sinon, il y a eu une entrée de texte, donc on filtre
                filterList(newValue);
            }
        });

//**********************************************************
        ListReclamation.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);

                if (empty || reclamation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Créer une mise en page personnalisée pour chaque élément
                    VBox container = new VBox();
                    container.setSpacing(5);
                    container.setAlignment(Pos.CENTER);
                    // Afficher les détails du rendez-vous avec des icônes
                    Label dateLabel = createStyledLabel("Date: " + reclamation.getDaterec());
                    Label statusLabel = createStyledLabel("Description: " + reclamation.getDescription());
                    Label motifLabel = createStyledLabel("Status: " + reclamation.getStatus());
                    motifLabel.setWrapText(true);

                    Button editButton = new Button("Modifier");
                    Button deleteButton = new Button("Supprimer");
                    HBox buttonsBox = new HBox(editButton, deleteButton);
                    buttonsBox.setSpacing(10);
                    buttonsBox.setAlignment(Pos.CENTER);
                    editButton.setStyle("-fx-background-color: #87acb5FF; -fx-background-radius: 10px; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #87ACB5FF; -fx-background-radius: 10px; -fx-text-fill: white;");
                    String fontName = "Berlin Sans FB"; // Nom de la police
                    double fontSize = 14.0; // Taille de la police
                    Font font = Font.font(fontName, fontSize); // Créer un objet Font avec le nom et la taille de la police
                    editButton.setFont(font); // Définir la police pour le bouton "Modifier"
                    deleteButton.setFont(font); // Définir la police pour le bouton "Supprimer"
                    editButton.setTextFill(Color.web("#2f3e44")); // Définir la couleur du texte pour le bouton "Modifier"
                    deleteButton.setTextFill(Color.web("#2f3e44")); // Définir la couleur du texte pour le bouton "Supprimer"
                    // Gérer les actions des boutons
                    editButton.setOnAction(event -> editReclamation(reclamation));
                    deleteButton.setOnAction(event -> deleteReclamation(reclamation));
                    // Appliquer le style CSS aux boutons

                    // Ajouter les éléments à la mise en page personnalisée
                    container.getChildren().addAll(dateLabel, statusLabel, motifLabel, buttonsBox);

                    // Ajouter une séparation entre les éléments
                    Separator separator = new Separator();
                    container.getChildren().add(separator);

                    // Définir la mise en page personnalisée comme contenu de la cellule
                    setGraphic(container);
                }
            }
        });

        showAll();


    }

    private void filterList(String searchText) {
        // Vérifier si le texte de recherche est vide
        if (!searchText.isEmpty()) {
            // Si le texte de recherche n'est pas vide, filtrer la liste en fonction du nouveau texte
            filteredList.clear(); // Effacer la liste filtrée actuelle
            // Parcourir la liste originale (probablement ListReclamation.getItems()) et ajouter les éléments correspondants au texte de recherche
            for (Reclamation reclamation : ListReclamation.getItems()) {
                if (reclamationMatchesSearch(reclamation, searchText)) {
                    filteredList.add(reclamation);
                }
            }
            showFilteredList();
        } else {
            // Si le texte de recherche est vide, afficher tous les éléments
            showAll();
        }
    }

    private boolean reclamationMatchesSearch(Reclamation reclamation, String searchText) {
        // Convertir le texte de recherche en minuscules pour l'ignorance de la casse
        String searchTextLowerCase = searchText.toLowerCase();
        // Vérifier si la description, le statut ou la date de la réclamation contient le texte de recherche
        return reclamation.getDescription().toLowerCase().contains(searchTextLowerCase)
                || reclamation.getStatus().toLowerCase().contains(searchTextLowerCase)
                || reclamation.getDaterec().toString().contains(searchTextLowerCase); // Assurez-vous que la méthode getDate() retourne une représentation textuelle de la date
    }

    private void showFilteredList() {
        ListReclamation.getItems().clear();
        ListReclamation.getItems().setAll(filteredList);
    }


    private void editReclamation(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditReclamationGui.fxml"));
            Parent root = loader.load();
            EditReclamationController controller = loader.getController();
            controller.SetReclamationService(rs);
            controller.SetData(reclamation);
            ListReclamation.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Une erreur s'est produite lors de l'édition du reclamation.");
            alert.setTitle("Erreur");
            alert.show();
        }
    }

    // Méthode pour supprimer un rendez-vous
    private void deleteReclamation(Reclamation reclamation) {
        rs.delete(reclamation.getId());
        ListReclamation.getItems().remove(reclamation);
    }

    public void showAll() {
        ListReclamation.getItems().clear();
        ListReclamation.getItems().addAll(rs.getAll());
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #000000;"); // Gras et couleur bleue
        return label;
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

    @FXML
    void btnGenererPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                Document document = new Document();
                PdfWriter.getInstance(document, fos);
                document.open();
                // Ajouter une image du logo
                com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance("C:/Users/benai/IdeaProjects/Trim_Java/src/main/resources/img/logo.png");
                // Redimensionner l'image si nécessaire
                logo.scaleToFit(200, 200);

                com.itextpdf.text.Image plan = com.itextpdf.text.Image.getInstance("C:/Users/benai/IdeaProjects/Trim_Java2/src/main/resources/img/plan.png");

// Redimensionner l'image pour qu'elle occupe toute la page
                plan.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());

// Positionner l'image dans le coin supérieur gauche
                plan.setAbsolutePosition(0, 0);
                document.add(plan);
                // Charger la police Berlin Sans FB Regular
                BaseFont berlinSans = BaseFont.createFont("C:/Users/benai/IdeaProjects/Trim_Java/src/main/resources/img/BRLNSR.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

// Créer une nouvelle police avec Berlin Sans FB Regular
                com.itextpdf.text.Font berlinSansRegular = new com.itextpdf.text.Font(berlinSans, 20);

// Définir la couleur de la police
                BaseColor customColor = new BaseColor(121, 195, 241);
                berlinSansRegular.setColor(customColor);

// Créer le paragraphe avec la police Berlin Sans FB Regular
                Paragraph title = new Paragraph("Liste des Réclamations", berlinSansRegular);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(Chunk.NEWLINE);

                // Style de paragraphe pour les détails de réclamation
                // Charger la police Berlin Sans FB Regular
                BaseFont berlinSans2 = BaseFont.createFont("C:/Users/benai/IdeaProjects/Trim_Java/src/main/resources/img/BRLNSR.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

// Créer une nouvelle police avec Berlin Sans FB Regular
                com.itextpdf.text.Font berlinSansRegular2 = new com.itextpdf.text.Font(berlinSans2, 15);

// Définir la couleur de la police
                BaseColor customColor2 = new BaseColor(121, 195, 241);
                berlinSansRegular.setColor(customColor2);

// Ajouter chaque réclamation au document PDF
                for (Reclamation item : ListReclamation.getItems()) {
                    // Créer un tableau avec une cellule vide pour la séparation
                    PdfPTable table = new PdfPTable(1);
                    table.setWidthPercentage(100);

                    // Ajouter une cellule vide avec une bordure basse pour la séparation
                    PdfPCell cell = new PdfPCell();
                    cell.setBorder(PdfPCell.BOTTOM);
                    cell.setBorderColor(BaseColor.LIGHT_GRAY); // Couleur de la bordure
                    cell.setPaddingBottom(5); // Marge inférieure
                    cell.setFixedHeight(10); // Hauteur de la ligne
                    table.addCell(cell);

                    // Ajouter le tableau au document
                    document.add(table);

                    // Ajouter les détails de la réclamation avec la police Berlin Sans FB Regular
                    Paragraph date = new Paragraph("Date: " + item.getDaterec(), berlinSansRegular2);
                    date.setAlignment(Element.ALIGN_CENTER);
                    document.add(date);

                    Paragraph description = new Paragraph("Description: " + item.getDescription(), berlinSansRegular2);
                    description.setAlignment(Element.ALIGN_CENTER);
                    document.add(description);

                    Paragraph status = new Paragraph("Statut: " + item.getStatus(), berlinSansRegular2);
                    status.setAlignment(Element.ALIGN_CENTER);
                    document.add(status);

                    // Ajouter une nouvelle ligne entre chaque réclamation
                    document.add(Chunk.NEWLINE);
                }


                document.close();

                // Ouvrir le fichier PDF généré
                Desktop.getDesktop().open(selectedFile);

            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                // Gestion des erreurs
            }


        }

    }


    @FXML
    void AjouterReclam(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AjouterReclamationGui.fxml"));
            circle.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("Sorry");
            alert2.setTitle("Error");
            alert2.show();
        }
    }
}