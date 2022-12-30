package fr.univtln.lhd.controllers;

import fr.univtln.lhd.facade.EventChange;
import fr.univtln.lhd.facade.Observer;
import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.users.Admin;
import fr.univtln.lhd.model.entities.users.Student;
import fr.univtln.lhd.model.entities.users.User;
import fr.univtln.lhd.view.edt.EdtGrid;
import fr.univtln.lhd.view.slots.SlotUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EdtLhdController implements Initializable, Observer {
    @FXML private BorderPane borderPane;

    @FXML private Label edtTopSectionLabel;
    @FXML private Button edtTopDateLabelBtn;

    @FXML private Button previousWeekBtn;
    @FXML private Button nextWeekBtn;

    @FXML private Button addBtn;
    @FXML private Label accountLabel;

    @FXML private Parent slotInfo;
    @FXML private SlotInfoController slotInfoController;

    private EdtGrid edtGrid;
    private User currentAuthStudent;

    private Node lastSlotClicked;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Schedule.subscribe("slotEvent", this);

        slotInfoController.setParentController(this);

        edtGrid = EdtGrid.getInstance();
        edtGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clickGrid);

        slotInfoController.slotInfoPanel.setVisible(false);

        edtTopSectionLabel.setText("Semaine");
        edtTopDateLabelBtn.setText(LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).toUpperCase());

        //currentAuthStudent = Schedule.getAdminFromAuth("adminTest@test.com", "adminPasswordTest").orElseThrow(RuntimeException::new);
        currentAuthStudent = Schedule.getStudentFromAuth("Theo.hafsaoui@superEmail.com", "LeNomDeMonChien").orElseThrow(RuntimeException::new);
        //currentAuthStudent = Schedule.getProfessorFromAuth("pierre-Mahe@univ-tln.fr", "LeNomDuChien").orElseThrow(RuntimeException::new);

        setupAuthenticatedView();
        updateEdtForCurrentStudent();

        borderPane.setCenter(edtGrid);
    }

    private void setupAuthenticatedView(){
        if (!(currentAuthStudent instanceof Admin))
            addBtn.setVisible(false);

        accountLabel.setText( "Bonjour "
                + currentAuthStudent.getFname().toUpperCase()
                + ", "
                + currentAuthStudent.getName()
                + " - "
                + currentAuthStudent.getClass().getSimpleName()
        );
    }

    @Override
    public void update(EventChange<?> change) {
        //wip
        switch (change.getType()){
            case ADD -> edtGrid.add( (Slot) change.getData());
            case MODIFY -> edtGrid.replace( slotInfoController.getSlotUI(), (Slot) change.getData() );
            case REMOVE -> edtGrid.delete( slotInfoController.getSlotUI() );
        }
    }

    public User getCurrentAuthStudent() { return currentAuthStudent; }

    private void updateEdtForCurrentStudent() {
        //bug if user is not student, will throw exception
        edtGrid.clearFullGrid();
        LocalDate weekStart = edtGrid.getCurrentWeekStart();
        LocalDate weekEnd = edtGrid.getCurrentWeekStart().plusDays(5);
        List<Slot> weekStudentSlots = Schedule.getSchedule((Student) currentAuthStudent, weekStart, weekEnd);
        edtGrid.add(weekStudentSlots);
    }

    //region BUTTON EVENT HANDLER
    @FXML private void addBtnOnClick(ActionEvent actionEvent) { slotInfoController.showAddPanel(); }

    @FXML private void previousWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.previousWeek();
        updateEdtForCurrentStudent();
    }

    @FXML private void nextWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.nextWeek();
        updateEdtForCurrentStudent();
    }

    @FXML private void edtTopDateLabelBtnOnClick(ActionEvent actionEvent) {
        edtGrid.setCurrentWeekStart();
        edtGrid.updateDaysLabel();
        updateEdtForCurrentStudent();
    }

    private void clickGrid(MouseEvent event){
        Node clickedNode = event.getPickResult().getIntersectedNode();

        if (clickedNode instanceof SlotUI slotUI && slotUI.isActive()){
            if (lastSlotClicked != null && lastSlotClicked == clickedNode)
                slotInfoController.toggleSlotInfoPanel();
            else
                slotInfoController.showSlotInfoPanel(slotUI);
            lastSlotClicked = clickedNode;
        }
    }

    @FXML private void disconnectBtnOnClick(ActionEvent actionEvent) {
        System.out.println("DISCONNECT BTN CLICKED - wip");
    }
    //endregion
}
