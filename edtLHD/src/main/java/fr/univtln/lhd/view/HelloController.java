package fr.univtln.lhd.view;

import fr.univtln.lhd.view.edt.Grid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML public Tab first_tab;
    @FXML private ChoiceBox choiceBox;
    @FXML private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("ITEM 1", "ITEM 2", "ITEM 3");
        choiceBox.getSelectionModel().selectFirst();

        Grid edtGrid = Grid.builder(3, 3).build();

        Label day = new Label("LUNDI");
        Label day2 = new Label("MARDI");

        edtGrid.add(day, 0, 0);
        edtGrid.add(day2, 0, 1);

        first_tab.setContent(edtGrid);
    }

    @FXML
    protected void onOkButtonClick(){
        label.setText("YOOOOOOOO");
    }
}