package fr.univtln.lhd.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class LoginApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("/fr.univtln.lhd.view/login-lhd.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,1100,650);
        scene.getStylesheets().add("/styles/login-lhd-style.css");
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
