package fr.univtln.lhd.view;

import fr.univtln.lhd.view.edt.EdtGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML public Tab firstTab;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("ITEM 1", "ITEM 2", "ITEM 3");
        choiceBox.getSelectionModel().selectFirst();

        EdtGrid edtGrid = EdtGrid.getInstance();

        Label cours1 = new Label("INFO U001\nprof");
        Label cours2 = new Label("MATH U008\nprof 2\nGroupe 1");

        FlowPane flowPane = new FlowPane(cours1);
        //flowPane.setStyle("-fx-background-color: blue"); //java setting css line, always has the last word over css file
        flowPane.setId("slot");
        FlowPane flowPane2 = new FlowPane(cours2);
        flowPane2.setId("slot");
        flowPane2.setStyle("-fx-background-color: red");

        edtGrid.add(flowPane, "MERCREDI", "08:00", "12:00");
        edtGrid.add(flowPane2, "VENDREDI", "14:00", "16:00");



        firstTab.setContent(edtGrid);
    }

    @FXML
    protected void onOkButtonClick(){
        label.setText("YOOOOOOOO");
    }
}