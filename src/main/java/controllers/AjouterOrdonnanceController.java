package controllers;

import enums.EtatOrd;
import enums.TypeOrd;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Ordonnance;
import models.Patient;
import services.OrdonnanceService;
import services.TwilioService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterOrdonnanceController implements Initializable {
    private final OrdonnanceService ordonnanceService = new OrdonnanceService();
    private final TwilioService twilioService = new TwilioService();

    @FXML
    private TextField tfTel;

    @FXML
    private TextField tfAdresse;

    @FXML
    private TextField tfGenre;

    @FXML
    private Label codeError;

    @FXML
    private Label dateError;

    @FXML
    private Label descError;

    @FXML
    private Label etatError;

    @FXML
    private Label selectError;

    @FXML
    private Label typeError;

    @FXML
    private Label ListOrd;

    @FXML
    private TextField tfAge;

    @FXML
    private TextField tfCode;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private DatePicker tdDate;

    @FXML
    private TextArea tfDescription;

    @FXML
    private ComboBox<EtatOrd> tfEtat;

    @FXML
    private ComboBox<String> selectAanalyse;

    @FXML
    private ComboBox<TypeOrd> tfType;

    @FXML
    private ComboBox<String> selectRendezVous;

    @FXML
    private ComboBox<String> selectedName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            selectAanalyse.setDisable(true);
            List<Integer> rendezVousIds = ordonnanceService.getRendezVousIdsByMedecinId(1);
            ObservableList<String> observableList = FXCollections.observableArrayList();
            observableList.add("Choisir rendez vous");

            ObservableList<EtatOrd> etatOrdList = FXCollections.observableArrayList(EtatOrd.values());
            tfEtat.setItems(etatOrdList);
            tfEtat.getSelectionModel().selectFirst();

            ObservableList<TypeOrd> typeOrdList = FXCollections.observableArrayList(TypeOrd.values());
            typeOrdList.add(0, null);

            tfType.setItems(typeOrdList);
            tfType.getSelectionModel().selectFirst();
            tfType.setOnAction(event -> {
                TypeOrd selectedType = tfType.getValue();
                if (selectedType == null) {
                    ListOrd.setText("Liste");
                    selectedName.setItems(null);
                    selectAanalyse.setDisable(true);
                } else if (selectedType == TypeOrd.Laboratoire) {
                    ListOrd.setText("Laboratoire");
                    selectAanalyse.setDisable(false);
                    try {
                        List<String> laboratoireNames = ordonnanceService.getAllLaboratoireNames();
                        ObservableList<String> laboratoireObservableList = FXCollections.observableArrayList(laboratoireNames);
                        selectedName.setItems(laboratoireObservableList);
                    } catch (SQLException e) {
                        System.out.println("An error occurred while fetching laboratory names: " + e.getMessage());
                    }
                } else if (selectedType == TypeOrd.Pharmacie) {
                    ListOrd.setText("Pharmacie");
                    selectAanalyse.setDisable(true);
                    try {
                        List<String> pharmacieNames = ordonnanceService.getAllPharmacieNames();
                        ObservableList<String> pharmacieObservableList = FXCollections.observableArrayList(pharmacieNames);
                        selectedName.setItems(pharmacieObservableList);
                    } catch (SQLException e) {
                        System.out.println("An error occurred while fetching pharmacy names: " + e.getMessage());
                    }
                }
            });

            for (Integer id : rendezVousIds) {
                observableList.add(String.valueOf(id));
            }
            selectRendezVous.setItems(observableList);
            selectRendezVous.getSelectionModel().selectFirst();

            codeError.setVisible(false);
            dateError.setVisible(false);
            descError.setVisible(false);
            etatError.setVisible(false);
            selectError.setVisible(false);
            typeError.setVisible(false);
            tdDate.setValue(LocalDate.now());
        } catch (SQLException e) {
            System.out.println("An error occurred while initializing: " + e.getMessage());
        }

        selectedName.setOnAction(event -> {
            String selectedLaboratoire = selectedName.getValue();
            if (selectedLaboratoire != null) {
                try {
                    int labId = ordonnanceService.getLabId(selectedLaboratoire);
                    List<String> analyseTypes = ordonnanceService.getAnalyseTypesByLabId(labId);
                    ObservableList<String> analyseNamesList = FXCollections.observableArrayList(analyseTypes);
                    selectAanalyse.setItems(analyseNamesList);

                } catch (SQLException e) {
                    System.out.println("An error occurred while fetching analysis names: " + e.getMessage());
                }
            }
        });
    }


    @FXML
    void selectRDV(ActionEvent event) {
        String selectedRendezVous = selectRendezVous.getValue();
        if (selectedRendezVous != null && !selectedRendezVous.equals("Choisir rendez vous")) {
            try {
                Patient patient = ordonnanceService.getPatientDetails(selectedRendezVous);
                if (patient != null) {
                    tfNom.setText(patient.getNom());
                    tfPrenom.setText(patient.getPrenom());
                    tfAge.setText(String.valueOf(patient.getAge()));
                    tfGenre.setText(patient.getGenre());
                    tfTel.setText(String.valueOf(patient.getNtel()));
                    tfAdresse.setText(patient.getAdresse());
                    String generatedCode = generateCode(patient.getPrenom(), patient.getNom(), patient.getAge(), patient.getGenre());
                    tfCode.setText(generatedCode);
                }
            } catch (SQLException e) {
                System.out.println("An error occurred while fetching patient details: " + e.getMessage());
            }
        } else {
            tfNom.setText(null);
            tfPrenom.setText(null);
            tfAge.setText(null);
            tfGenre.setText(null);
            tfTel.setText(null);
            tfAdresse.setText(null);
        }
    }

    @FXML
    void selectedType(ActionEvent event) {
        String selectedType = selectedName.getValue();
        System.out.println("Selected type: " + selectedType);
        if (selectedType != null) {
            try {
                if (tfType.getValue() == TypeOrd.Laboratoire) {
                    handleLaboratoireSelected(selectedType);
                } else if (tfType.getValue() == TypeOrd.Pharmacie) {
                    handlePharmacieSelected(selectedType);
                }
            } catch (SQLException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleLaboratoireSelected(String selectedLaboratoire) throws SQLException {
        int selectedId = ordonnanceService.getLabId(selectedLaboratoire);
        System.out.println("Selected Laboratoire ID: " + selectedId);
    }

    private void handlePharmacieSelected(String selectedPharmacie) throws SQLException {
        int selectedId = ordonnanceService.getPharmacieId(selectedPharmacie);
        System.out.println("Selected Pharmacie ID: " + selectedId);
    }


    @FXML
    void ajouterOrd(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        String selectedRendezVous = selectRendezVous.getValue();
        String selectedType = selectedName.getValue();
        if (selectedRendezVous == null || selectedRendezVous.equals("Choisir rendez vous")) {
            System.out.println("Veuillez sélectionner un rendez-vous");
            return;
        }

        try {
            if (ordonnanceService.ordonnanceExistsForRendezVous(selectedRendezVous)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une ordonnance existe déjà pour ce rendez-vous.");
                alert.showAndWait();
                return;
            }

            int medecinId = ordonnanceService.getMedecinId(selectedRendezVous);
            int patientId = ordonnanceService.getPatientId(selectedRendezVous);

            Ordonnance ordonnance = new Ordonnance();
            ordonnance.setType(tfType.getValue());
            ordonnance.setEtat(tfEtat.getValue());
            ordonnance.setDescription(tfDescription.getText());
            String generatedCode = generateCode(tfNom.getText(), tfPrenom.getText(), Integer.parseInt(tfAge.getText()), tfGenre.getText());
            ordonnance.setCode(generatedCode);
            ordonnance.setDate(LocalDateTime.now());
            ordonnance.setIdRendezVous(Integer.parseInt(selectedRendezVous));
            ordonnance.setIdMedecin(medecinId);
            ordonnance.setIdPatient(patientId);

            if (tfType.getValue() == TypeOrd.Laboratoire) {
                String selectedAnalyse = selectAanalyse.getValue();
                int analyseId = ordonnanceService.getAnalyseId(selectedAnalyse);
                ordonnance.setIdAnalyse(analyseId);
                addLaboratoireOrdonnance(ordonnance, selectedType);
            } else if (tfType.getValue() == TypeOrd.Pharmacie) {
                addPharmacieOrdonnance(ordonnance, selectedType);
            }

            String recipient = "+216" + tfTel.getText();
            String messageBody = "Votre Ordonnance est prete! , merci de choisir notre Application";
            twilioService.sendSMS(recipient, messageBody);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Ordonnance ajoutée avec succès!");
            alert.showAndWait();
            naviguezVersLisOrds(event);
        } catch (SQLException e) {
            System.out.println("An error occurred while adding prescription: " + e.getMessage());
        }
    }


    private void addLaboratoireOrdonnance(Ordonnance ordonnance, String selectedLaboratoire) throws SQLException {
        int selectedId = ordonnanceService.getLabId(selectedLaboratoire);
        ordonnance.setIdLaboratoire(selectedId);
        ordonnanceService.ajouter(ordonnance);
    }

    private void addPharmacieOrdonnance(Ordonnance ordonnance, String selectedPharmacie) throws SQLException {
        int selectedId = ordonnanceService.getPharmacieId(selectedPharmacie);
        ordonnance.setIdPharmacie(selectedId);
        ordonnanceService.ajouterPharmacie(ordonnance);
    }


    private boolean validateFields() {
        boolean isValid = true;

        String code = tfCode.getText().trim();
        if (code.isEmpty() || code.length() < 3 || !code.matches(".*[a-zA-Z].*")) {
            codeError.setText("Le code doit contenir au moins 3 caractères et uniquement des lettres.");
            codeError.setVisible(true);
            isValid = false;
        } else {
            codeError.setVisible(false);
        }

        // Validate date
        if (tdDate.getValue() == null) {
            dateError.setText("La date est obligatoire.");
            dateError.setVisible(true);
            isValid = false;
        } else {
            dateError.setVisible(false);
        }

        String description = tfDescription.getText().trim();
        if (description.isEmpty() || description.length() < 4 || !description.matches(".*[a-zA-Z].*")) {
            descError.setText("La description doit contenir au moins 4 caractères et uniquement des lettres.");
            descError.setVisible(true);
            isValid = false;
        } else {
            descError.setVisible(false);
        }

        if (tfEtat.getValue() == null) {
            etatError.setText("L'état est obligatoire.");
            etatError.setVisible(true);
            isValid = false;
        } else {
            etatError.setVisible(false);
        }

        if (tfType.getValue() == null) {
            typeError.setText("Le type est obligatoire.");
            typeError.setVisible(true);
            isValid = false;
        } else {
            typeError.setVisible(false);
        }

        if (selectRendezVous.getValue() == null || selectRendezVous.getValue().equals("Choisir rendez vous")) {
            selectError.setVisible(true);
            System.out.println("Veuillez sélectionner un rendez-vous avant d'ajouter l'ordonnance.");
            isValid = false;
        } else {
            selectError.setVisible(false);
        }
        return isValid;
    }

    void naviguezVersLisOrds(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }


    @FXML
    void toOrd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }


    private String generateCode(String firstName, String lastName, int age, String gender) {
        // Convert first character of first name and last name to uppercase
        String firstInitial = firstName.substring(0, 1).toUpperCase();
        String lastInitial = lastName.substring(0, 1).toUpperCase();

        // Generate code based on initials, age, and gender
        String code = firstInitial + lastInitial + age;

        // Append additional characters based on gender
        if (gender.equalsIgnoreCase("male")) {
            code += "M";
        } else if (gender.equalsIgnoreCase("female")) {
            code += "F";
        } else {
            code += "O"; // Assume other gender
        }

        // Add random numbers to ensure uniqueness
        code += (int) (Math.random() * 1000);

        return code;
    }

    @FXML
    void toOrdList(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ordonnancesList.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
            currentStage.setFullScreen(true);
        } catch (Exception e) {
            System.out.println("Erreur de navigation : " + e.getMessage());
        }
    }


    @FXML
    void toProfile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/medecinProfile.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(newScene);
        } catch (IOException e) {
            System.out.println("Error navigating to profile: " + e.getMessage());
        }
    }

    @FXML
    void toMaladies(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/maladies.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

}
