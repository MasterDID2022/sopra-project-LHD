package fr.univtln.lhd.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML private ChoiceBox choiceBox;
    @FXML private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("ITEM 1", "ITEM 2", "ITEM 3");
        choiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    protected void onOkButtonClick(){
        label.setText("YOOOOOOOO");
    }
}