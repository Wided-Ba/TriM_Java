package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import enums.EtatOrd;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Chef_Lab;
import models.Ordonnance;
import models.Patient;
import models.Reclamation;
import services.AnalyseService;
import services.Chef_LabService;
import services.OrdonnanceService;
import services.PatientService;
import utils.MyDataBase;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;


public class HomeController implements Initializable {

    @FXML
    private Label num;
    @FXML
    private Label num1;
    @FXML
    private Circle circle;
    @FXML
    private Pane chart;
    @FXML
    private Button generepdf;
    private Chef_LabService chefLabService = new Chef_LabService();
    private AnalyseService analyseService = new AnalyseService();

    private PatientService patientService = new PatientService();
    private OrdonnanceService ordonnanceService;

    private Patient patient;
    private List<Ordonnance> ordonnances;
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("/img/1.png");
        circle.setFill(new ImagePattern(img));
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        ordonnanceService = new OrdonnanceService();
        int idLab = chefLab.getId_lab();
        try {
            int ordonnanceCount = ordonnanceService.compterOrdonnances(idLab);
            int analyseCount = analyseService.compterAnalyses(idLab);
            num.setText(String.valueOf(ordonnanceCount));
            num1.setText(String.valueOf(analyseCount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Mois");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'ordonnances");

        // Calculer le maximum des valeurs de l'axe y
        int maxValue = 0; // Vous devez implémenter getMaxValue() pour obtenir la valeur maximale
        try {
            maxValue = getMaxValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Définir l'échelle personnalisée pour l'axe y
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0); // Définir la limite inférieure de l'axe y à 0
        yAxis.setUpperBound(maxValue); // Ajouter une marge de 10 à la valeur maximale
        yAxis.setTickUnit(1); // Définir l'unité de tic sur 10 pour multiplier par 10

        // Définir un formateur d'étiquettes pour l'axe des nombres (yAxis)
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                // Formater le nombre sans virgule
                return String.format("%.0f", object);
            }
        });

        Map<Month, Integer> ordonnancesParMois = null;
        try {
            ordonnancesParMois = ordonnanceService.compterOrdonnancesParMois(idLab);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Convertir la carte en une liste pour la trier
        List<Map.Entry<Month, Integer>> entries = new ArrayList<>(ordonnancesParMois.entrySet());

        // Trier la liste des entrées par ordre croissant de mois
        Collections.sort(entries, Map.Entry.comparingByKey());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Month, Integer> entry : entries) {
            Month mois = entry.getKey();
            int nombreOrdonnances = entry.getValue();
            series.getData().add(new XYChart.Data<>(mois.toString(), nombreOrdonnances));
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);
        barChart.setTitle("Évolution du nombre d'ordonnances dans le mois");
        series.setName("Nombre d'ordonnances");
        for (XYChart.Data<String, Number> data : series.getData()) {
            Node node = data.getNode();
            String barColor = "#007ACC"; // Couleur bleue
            node.setStyle("-fx-bar-fill: " + barColor + ";");
        }


        // Modifier les couleurs du graphique
        String chartColor = "-fx-bar-fill: #007ACC;"; // Bleu
        barChart.setStyle(chartColor);


        StackPane pane = new StackPane(barChart);
        StackPane.setAlignment(barChart, Pos.CENTER);
        StackPane.setMargin(barChart, new Insets(10)); // Optionnel : définir une marge autour du graphique
        chart.getChildren().clear();
        chart.getChildren().add(pane);
    }

    private int getMaxValue() throws SQLException {
        int maxValue = 0;
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        ordonnanceService = new OrdonnanceService();
        int idLab = chefLab.getId_lab();
        Map<Month, Integer> ordonnancesParMois = ordonnanceService.compterOrdonnancesParMois(idLab);
        for (Map.Entry<Month, Integer> entry : ordonnancesParMois.entrySet()) {
            int nombreOrdonnances = entry.getValue();
            maxValue ++;
        }
        return maxValue;
    }

    public List<Ordonnance> listord() {
        int idChefLab = 2;
        Chef_Lab chefLab = chefLabService.getById(idChefLab);
        int idLab = chefLab.getId_lab();
        ordonnanceService = new OrdonnanceService();
        List<Ordonnance> ordonnances = new ArrayList<>();
        try {
            // Récupérer toutes les ordonnances
            ordonnances = ordonnanceService.recupererParIdLab(idLab);

            // Filtrer les ordonnances avec l'état "En_cours" ou "En_attente"
            ordonnances = ordonnances.stream()
                    .filter(ordonnance -> ordonnance.getEtat() == EtatOrd.Prete)
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordonnances;
    }


    @FXML
    void pdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                Document document = new Document();
                PdfWriter writer = PdfWriter.getInstance(document, fos);

                // Créer un footer personnalisé
                writer.setPageEvent(new Footer());

                document.open();

                com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
                com.itextpdf.text.Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.WHITE);
                com.itextpdf.text.Font tableDataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

                // Ajouter le titre
                Paragraph title = new Paragraph("Historique de demande de prélèvement", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Espacement après le titre
                document.add(Chunk.NEWLINE);

                // Tableau des données
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingAfter(10); // Espacement après le tableau
                table.setHeaderRows(1); // Ligne d'en-tête

                // En-tête du tableau
                String[] tableHeaders = {"Code", "Type", "Date", "Email du Patient"};
                for (String header : tableHeaders) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, tableHeaderFont));
                    headerCell.setBackgroundColor(BaseColor.GRAY);
                    table.addCell(headerCell);
                }

                // Remplir le tableau avec les données
                List<Ordonnance> ordonnances = listord();
                for (Ordonnance item : ordonnances) {
                    table.addCell(new Phrase(item.getCode(), tableDataFont));
                    table.addCell(new Phrase(String.valueOf(analyseService.getById(item.getIdAnalyse()).getNom()), tableDataFont));
                    LocalDateTime date = item.getDate();
                    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    table.addCell(new Phrase(formattedDate, tableDataFont));
                    table.addCell(new Phrase(String.valueOf(patientService.getById2(item.getIdPatient()).getEmail()), tableDataFont));
                }

                // Ajouter le tableau au document
                document.add(table);

                document.close();
                Desktop.getDesktop().open(selectedFile);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                // Gestion des erreurs
            }
        }
    }

    // Classe pour ajouter un footer personnalisé
    // Classe pour ajouter un footer personnalisé
    private class Footer extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            footerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Ajouter votre contenu de footer ici
            Phrase footerPhrase = new Phrase("Votre contenu de footer ici", FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY));
            PdfPCell footerCell = new PdfPCell(footerPhrase);
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footerCell.setBorder(Rectangle.NO_BORDER);
            footerTable.addCell(footerCell);

            // Positionner le footer en bas de la page
            float tableHeight = footerTable.getTotalHeight();
            if (tableHeight > 0) {
                footerTable.writeSelectedRows(0, -1, document.left(), document.bottom() + tableHeight, writer.getDirectContent());
            }
        }
    }






    //******************************switchScene******************************

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
            // Afficher la scène
        circle.getScene().setRoot(parent);
    }

    @FXML
    void afficheAnalyse(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/AfficherAnalyse.fxml"));
        circle.getScene().setRoot(root);
    }

    @FXML
    void afficheprelevement(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/DemandePrelevement.fxml"));
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
