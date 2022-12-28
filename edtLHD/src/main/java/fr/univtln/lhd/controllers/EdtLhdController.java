package fr.univtln.lhd.controllers;

import fr.univtln.lhd.facade.EventChange;
import fr.univtln.lhd.facade.Observer;
import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.users.Student;
import fr.univtln.lhd.view.edt.EdtGrid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.layout.Pane;

public class EdtLhdController implements Initializable, Observer {

    @FXML public BorderPane borderPane;
    @FXML public Label edtTopSectionLabel;
    @FXML public Button edtTopDateLabelBtn;
    @FXML public Pane addSlotPane;
    @FXML public Button previousWeekBtn;
    @FXML public Button nextWeekBtn;

    private EdtGrid edtGrid;
    private Student currentStudent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Schedule.subscribe("slot", this);

        addSlotPane.setVisible(false);

        edtGrid = EdtGrid.getInstance();

        edtTopSectionLabel.setText("Semaine");
        edtTopDateLabelBtn.setText(LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).toUpperCase());

        currentStudent = Schedule.getStudentFromAuth("Theo.hafsaoui@superEmail.com", "LeNomDeMonChien").orElseThrow(RuntimeException::new);
        updateEdtForCurrentStudent();

        borderPane.setCenter(edtGrid);
    }

    @FXML
    protected void addBtnOnClick(ActionEvent actionEvent) { addSlotPane.setVisible(true); }

    @FXML
    protected void addCancelBtnOnClick(ActionEvent actionEvent) { addSlotPane.setVisible(false); }

    @Override
    public void udpate(List<EventChange<?>> changes) {
        //wip
    }

    private void updateEdtForCurrentStudent() {
        edtGrid.clearFullGrid();
        LocalDate weekStart = edtGrid.getCurrentWeekStart();
        LocalDate weekEnd = edtGrid.getCurrentWeekStart().plusDays(5);
        List<Slot> weekStudentSlots = Schedule.getSchedule(currentStudent, weekStart, weekEnd);
        edtGrid.add(weekStudentSlots);
    }

    public void previousWeekBtnOnClick() {
        edtGrid.previousWeek();
        updateEdtForCurrentStudent();
    }

    public void nextWeekBtnOnClick() {
        edtGrid.nextWeek();
        updateEdtForCurrentStudent();
    }

    public void edtTopDateLabelBtnOnClick() {
        edtGrid.setCurrentWeekStart();
        edtGrid.updateDaysLabel();
        updateEdtForCurrentStudent();
    }
}
