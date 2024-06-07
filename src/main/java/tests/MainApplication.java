package tests;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Charger le SplashScreen
        Stage splashScreen = new Stage();
        Parent splashScreenRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/logo.fxml")));
        Scene splashScreenScene = new Scene(splashScreenRoot);
        Image image = new Image("/img/favicon.png");
        splashScreen.getIcons().add(image);
        splashScreen.setTitle("TriM");
        splashScreenScene.setFill(Color.TRANSPARENT);
        splashScreen.setScene(splashScreenScene);
        splashScreen.initStyle(StageStyle.TRANSPARENT);
        splashScreen.centerOnScreen(); // Centrer la fenêtre sur l'écran

        // Ajouter une transition de fondu pour le SplashScreen
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), splashScreenRoot);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        splashScreen.show();

        // Fermer la fenêtre du SplashScreen après 5 secondes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Appliquer l'effet de fondu lors de la fermeture du SplashScreen
                    fadeOut.play();
                    fadeOut.setOnFinished(event -> {
                        splashScreen.close();
                        // Charger la fenêtre principale
                        loadMainScene(primaryStage);
                    });
                });
            }
        }, 2000); // 5 secondes
    }

    private void loadMainScene(Stage primaryStage) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Main.fxml")));
            Scene scene = new Scene(parent);
            Image image = new Image("/img/favicon.png");
            primaryStage.getIcons().add(image);
            primaryStage.setTitle("TriM");
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.centerOnScreen(); // Centrer la fenêtre sur l'écran

            // Ajouter une transition de fondu pour la fenêtre principale
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), parent);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
