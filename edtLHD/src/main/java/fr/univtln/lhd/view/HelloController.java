package fr.univtln.lhd.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AmbientLight;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML public GridPane grid;
    @FXML private ChoiceBox choiceBox;
    @FXML private Label label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll("ITEM 1", "ITEM 2", "ITEM 3");
        choiceBox.getSelectionModel().selectFirst();

        ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        column1.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints(100, 100, Double.MAX_VALUE);
        row.setVgrow(Priority.ALWAYS);
        row.setValignment(VPos.CENTER);

        grid.getColumnConstraints().add(column1);
        grid.getRowConstraints().add(row);

        Label n = new Label("yiu");
        Label w = new Label("shdkq");
        grid.add(n, 2, 2);
        grid.add(w, 2, 1);
    }

    @FXML
    protected void onOkButtonClick(){
        label.setText("YOOOOOOOO");
    }
}