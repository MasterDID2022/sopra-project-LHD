package fr.univtln.lhd.controllers;

import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.view.edt.EdtGrid;
import fr.univtln.lhd.view.edt.SlotUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;
import org.threeten.extra.Interval;

public class EdtLhdController implements Initializable {

    @FXML public BorderPane borderPane;
    @FXML public Label edtTopDateLabel;
    @FXML public Label edtTopSectionLabel;
    @FXML public Pane addSlotPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addSlotPane.setVisible(false);

        EdtGrid edtGrid = EdtGrid.getInstance();

        edtTopSectionLabel.setText("Semaine");
        edtTopDateLabel.setText(LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).toUpperCase());

        Label cours_1 = new Label("INFO U001\nprof");
        Label cours_2 = new Label("MATH U008\nprof 2\nGroupe 1");
        Label cours_3 = new Label("PROJET U1.203\nMr.Truc");
        Label cours_4 = new Label("Test Z001");

        FlowPane flowPane = new FlowPane(cours_1);
        flowPane.getStyleClass().add("slot");

        FlowPane flowPane2 = new FlowPane(cours_2);
        flowPane2.getStyleClass().add("slot");
        flowPane2.setStyle("-fx-background-color: #13635e");

        FlowPane flowPane3 = new FlowPane(cours_3);
        flowPane3.getStyleClass().add("slot");

        FlowPane flowPane4 = new FlowPane(cours_4);
        flowPane4.getStyleClass().add("slot");

        //flowPane.setStyle("-fx-background-color: blue"); //java setting css line, always has the last word over css file

        edtGrid.add(flowPane, "MERCREDI", "09:00", "12:00");
        edtGrid.add(flowPane2, "VENDREDI", "14:00", "16:00");
        edtGrid.add(flowPane3, EdtGrid.Days.MARDI, "13:00", "19:00");

        //2022-12-16T09:02:00.00+01:00 => Friday 16 December 2022, 09:02am in timezone UTC+1
        Interval timeRange = Interval.of(Instant.parse("2022-12-16T09:02:00.00+01:00"), Duration.ofHours(3));

        List<Group> groups = new ArrayList<>();
        groups.add( Group.getInstance("M1 Info") );
        groups.add( Group.getInstance("M5 SVT") );
        Slot courTest = Slot.getInstance(Slot.SlotType.CM, 0, 0, groups, timeRange);

        edtGrid.add(courTest);

        borderPane.setCenter(edtGrid);
    }

    @FXML
    protected void addBtnOnClick(ActionEvent actionEvent) { addSlotPane.setVisible(true); }

    @FXML
    protected void addCancelBtnOnClick(ActionEvent actionEvent) { addSlotPane.setVisible(false); }
}
