package fr.univtln.lhd.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Edt App Class which loads xml and css files, then display it into the app
 */
public class EdtApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getResource("/fr.univtln.lhd.view/edt-lhd.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1100, 650);

        stage.setTitle("EDT LHD");
        scene.getStylesheets().add("/styles/edt-lhd-style.css");
        scene.getStylesheets().add("/styles/slot-info-panel-style.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
