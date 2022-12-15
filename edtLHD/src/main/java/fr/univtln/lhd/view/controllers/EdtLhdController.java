package fr.univtln.lhd.view.controllers;

import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.view.edt.EdtGrid;
import fr.univtln.lhd.view.edt.SlotUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EdtLhdController implements Initializable {

    @FXML public BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EdtGrid edtGrid = EdtGrid.getInstance();

        Label cours_1 = new Label("INFO U001\nprof");
        Label cours_2 = new Label("MATH U008\nprof 2\nGroupe 1");

        FlowPane flowPane = new FlowPane(cours_1);
        //flowPane.setStyle("-fx-background-color: blue"); //java setting css line, always has the last word over css file
        flowPane.getStyleClass().add("slot");
        FlowPane flowPane2 = new FlowPane(cours_2);
        flowPane2.getStyleClass().add("slot");
        flowPane2.setStyle("-fx-background-color: #13635e");

        edtGrid.add(flowPane, "MERCREDI", "09:00", "12:00");
        edtGrid.add(flowPane2, "VENDREDI", "14:00", "16:00");

        //List<Group> groups = new ArrayList<>();
        //groups.add( Group.getInstance("M1 Info") );
        //Slot cours_test = Slot.getInstance(Slot.SlotType.CM, 0, 0, groups, "08:00"); //timerange wrong

        //SlotUI slotui_test = SlotUI.getInstance(cours_test);

        //edtGrid.

        borderPane.setCenter(edtGrid);
    }
}
