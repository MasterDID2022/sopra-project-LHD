package fr.univtln.lhd.controllers;

import fr.univtln.lhd.facade.EventChange;
import fr.univtln.lhd.facade.Observer;
import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
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

/**
 * Controller class for EDT LHD View
 */
public class EdtLhdController implements Initializable, Observer {
    @FXML private BorderPane borderPane;

    @FXML private Label edtTopSectionLabel;
    @FXML private Button edtTopDateLabelBtn;
    @FXML private ComboBox<Group> groupComboBox;

    @FXML private Button addBtn;
    @FXML private Label accountLabel;

    @FXML private Parent slotInfo; //need both Parent type and SlotInfoController
    @FXML private SlotInfoController slotInfoController; //name it exact same as Parent adding 'Controller', javafx auto attribute the associated Controller

    private EdtGrid edtGrid;
    private Auth currentAuth;

    private Node lastSlotClicked;

    /**
     * Initialize method, called automatically on initializing the view to render
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Schedule.subscribe(Schedule.SLOT_EVENT, this);

        //currentAuth = Auth.authAsStudent("Theo.hafsaoui@superEmail.com", "LeNomDeMonChien");
        currentAuth = Auth.authAsAdmin("test@test.lhd", "NO");
        //currentAuth = Auth.authAsGuest();

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

    /**
     * Getter for Current Authentication User
     * @return Auth Entity
     */
    public Auth getCurrentAuth() { return currentAuth; }

    /**
     * Setter for last clicked node
     * @param n Node to assign
     */
    public void setLastSlotClicked(Node n){ lastSlotClicked = n; }

    /**
     * Getter for Slot Percentage from Schedule
     * @param group Group to get the percentage for
     * @param slot Slot to access his subject information
     * @return double between 0-1
     */
    public double getSlotFinishedPercent(Group group, Slot slot){
        return Schedule.getPercentageOf(group, slot);
    }

    /**
     * Setup View based on role of auth user (also setup greetings msg)
     * - Admin will see : planning, group selection list, add slot button
     * - Guest will see : planning, group selection list
     * - Student / Professor : planning
     */
    private void setupAuthenticatedView(){
        if (!currentAuth.isAdmin()) addBtn.setVisible(false);
        if (!currentAuth.isGuest() && !currentAuth.isAdmin()) groupComboBox.setVisible(false);
        else if (currentAuth.isAdmin() || currentAuth.isGuest()) setupGroupsComboBox();

        accountLabel.setText( currentAuth.getGreetingsMessage() );
    }

    /**
     * Populate Group Selection List (aka Combo Box in java fx)
     * Get all groups from Schedule facade then populate the combo box with it
     */
    private void setupGroupsComboBox() {
        List<Group> groupList = Schedule.getAllGroups();
        groupComboBox.getItems().addAll(groupList);
        groupComboBox.setValue( groupList.get(0) );
    }

    /**
     * Observer Update Override
     * updates changes based on the type of change and the data given to it
     * @param change EventChange type, contains the type of changes (ADD / MODIFY / REMOVE), and the changed data
     */
    @Override
    public void update(EventChange<?> change) {

        switch (change.getType()){
            case ADD: edtGrid.add( (Slot) change.getData()); break;
            case MODIFY: edtGrid.replace( slotInfoController.getSlotUI(), (Slot) change.getData() ); break;
            case REMOVE: edtGrid.delete( slotInfoController.getSlotUI() ); break;
            default: break;
        }

        updateEdtForCurrentAuth();
    }

    /**
     * Populate Current Week edt planning for current auth user
     */
    private void updateEdtForCurrentAuth() {
        edtGrid.clearFullGrid();
        LocalDate weekStart = edtGrid.getCurrentWeekStart();
        LocalDate weekEnd = edtGrid.getCurrentWeekStart().plusDays(5);

        List<Slot> weekSlots = new ArrayList<>();
        switch (currentAuth.getType()){
            case STUDENT, PROFESSOR: {
                weekSlots = Schedule.getSchedule(
                        currentAuth.getAuthUser(),
                        weekStart,
                        weekEnd
                );
                break;
            }
            case ADMIN, GUEST: {
                weekSlots = Schedule.getSchedule(
                        groupComboBox.getValue(),
                        weekStart,
                        weekEnd
                );
                break;
            }
            default: break;
        }

        if (weekSlots.isEmpty()) return;
        edtGrid.add(weekSlots);
    }


    //region BUTTON EVENT HANDLER

    /**
     * Callback method when clicking on the add button (only available for admin users)
     * Show the add panel on the slot info controller child
     * @param actionEvent ActionEvent
     */
    @FXML private void addBtnOnClick(ActionEvent actionEvent) { slotInfoController.showAddPanel(); }

    /**
     * Callback method when clicking on the previous week button
     * go to previous week on the edt planning then update its content
     * @param actionEvent ActionEvent
     */
    @FXML private void previousWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.previousWeek();
        updateEdtForCurrentAuth();
    }

    /**
     * Callback method when clicking on the next week button
     * go to next week on the edt planning then update its content
     * @param actionEvent ActionEvent
     */
    @FXML private void nextWeekBtnOnClick(ActionEvent actionEvent) {
        edtGrid.nextWeek();
        updateEdtForCurrentAuth();
    }

    /**
     * Callback method when clicking on the Today's Date Top label
     * brings the user back to today's week planning
     * @param actionEvent ActionEvent
     */
    @FXML private void edtTopDateLabelBtnOnClick(ActionEvent actionEvent) {
        edtGrid.setCurrentWeekStart();
        edtGrid.updateDaysLabel();
        updateEdtForCurrentAuth();
    }

    /**
     * Toggle Slot Info Panel On Click upon SlotUi Element inside planning grid
     * @param event MouseEvent
     */
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

    /**
     * Callback method when clicking on the disconnect button
     * Disconnect current user and go back to auth view
     * @param actionEvent ActionEvent
     */
    @FXML private void disconnectBtnOnClick(ActionEvent actionEvent) {
        System.out.println("DISCONNECT BTN CLICKED - wip");
    }

    /**
     * Callback method when selecting an item in the group selection list
     * update current week planning to the selected group
     * @param actionEvent ActionEvent
     */
    public void groupComboBoxOnEndEdit(ActionEvent actionEvent) {
        updateEdtForCurrentAuth();
    }
    //endregion
}
