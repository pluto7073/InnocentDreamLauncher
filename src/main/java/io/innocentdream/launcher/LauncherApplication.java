package io.innocentdream.launcher.innocentdreamlauncher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LauncherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LauncherApplication.class.getResource("launcher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 550);

        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("assets/icon-512.png")));

        stage.setTitle("Innocent Dream Launcher");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}