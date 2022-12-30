package fr.univtln.lhd.controllers;

import fr.univtln.lhd.facade.EventChange;
import fr.univtln.lhd.facade.Observer;
import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.users.Student;
import fr.univtln.lhd.view.authentification.Auth;
import fr.univtln.lhd.view.edt.EdtGrid;
import fr.univtln.lhd.view.slots.SlotUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EdtLhdController implements Initializable, Observer {
    @FXML private BorderPane borderPane;

    @FXML private Label edtTopSectionLabel;
    @FXML private Button edtTopDateLabelBtn;

    @FXML private Button previousWeekBtn;
    @FXML private Button nextWeekBtn;
    @FXML private ComboBox<Group> groupComboBox;

    @FXML private Button addBtn;
    @FXML private Label accountLabel;

    @FXML private Parent slotInfo; //need both Parent type and SlotInfoController
    @FXML private SlotInfoController slotInfoController; //name it exact same as Parent adding 'Controller', javafx auto attribute the associated Controller

    private EdtGrid edtGrid;
    //private User currentAuthStudent;
    private Auth currentAuth;

    private Node lastSlotClicked;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Schedule.subscribe(Schedule.SLOT_EVENT, this);

        currentAuth = Auth.authAsStudent("Theo.hafsaoui@superEmail.com", "LeNomDeMonChien");
        //currentAuthStudent = Schedule.getAdminFromAuth("adminTest@test.com", "adminPasswordTest").orElseThrow(RuntimeException::new);
        //currentAuthStudent = Schedule.getStudentFromAuth("Theo.hafsaoui@superEmail.com", "LeNomDeMonChien").orElseThrow(RuntimeException::new);
        //currentAuthStudent = Schedule.getProfessorFromAuth("pierre-Mahe@univ-tln.fr", "LeNomDuChien").orElseThrow(RuntimeException::new);

        slotInfoController.setParentController(this);

        edtGrid = EdtGrid.getInstance();
        edtGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clickGrid);

        slotInfoController.hideSlotInfoPanel();

        edtTopSectionLabel.setText("Semaine");
        edtTopDateLabelBtn.setText(LocalDateTime.now().toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).toUpperCase());

        setupAuthenticatedView();
        updateEdtForCurrentAuth();

        borderPane.setCenter(edtGrid);
    }

    public Auth getCurrentAuth() { return currentAuth; }

    private void setupAuthenticatedView(){
        if (!currentAuth.isAdmin()){
            addBtn.setVisible(false);
            groupComboBox.setVisible(false);
        }else
            setupGroupsComboBox();

        accountLabel.setText( currentAuth.getGreetingsMessage() );
    }

    private void setupGroupsComboBox() {
        //wip
        //get all groups from schedule method
        //populate group combobox

    }

    @Override
    public void update(EventChange<?> change) {
        //wip
        switch (change.getType()){
            case ADD: edtGrid.add( (Slot) change.getData()); break;
            case MODIFY: edtGrid.replace( slotInfoController.getSlotUI(), (Slot) change.getData() ); break;
            case REMOVE: edtGrid.delete( slotInfoController.getSlotUI() ); break;
            default: break;
        }
    }

    private void updateEdtForCurrentAuth() {
        //bug if user is not student, will throw exception
        edtGrid.clearFullGrid();
        LocalDate weekStart = edtGrid.getCurrentWeekStart();
        LocalDate weekEnd = edtGrid.getCurrentWeekStart().plusDays(5);

        List<Slot> weekSlots = new ArrayList<>();
        switch (currentAuth.getType()){

            case STUDENT: {
                weekSlots = Schedule.getSchedule(
                        (Student) currentAuth.getAuthUser(),
                        weekStart,
                        weekEnd
                );
                break;
            }
        }

        //List<Slot> weekStudentSlots = Schedule.getSchedule((Student) currentAuthStudent, weekStart, weekEnd);
        edtGrid.add(weekSlots);
    }

    //region BUTTON EVENT HANDLER
    @FXML private void addBtnOnClick(ActionEvent actionEvent) { slotInfoController.showAddPanel(); }

    @FXML private void previousWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.previousWeek();
        updateEdtForCurrentAuth();
    }

    @FXML private void nextWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.nextWeek();
        updateEdtForCurrentAuth();
    }

    @FXML private void edtTopDateLabelBtnOnClick(ActionEvent actionEvent) {
        edtGrid.setCurrentWeekStart();
        edtGrid.updateDaysLabel();
        updateEdtForCurrentAuth();
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

    public void groupComboBoxOnEndEdit(ActionEvent actionEvent) {
        System.out.println("sdjfklsdjflsdjfkls");
    }
    //endregion
}
