package fr.univtln.lhd.controllers;

import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.view.authentification.Auth;
import fr.univtln.lhd.view.slots.SlotUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Controller for Slot Info Panel
 */
public class SlotInfoController {
    private EdtLhdController controller;

    @FXML public BorderPane slotInfoPanel;
    @FXML private Label sipTitle;
    @FXML private Button sipDeleteBtn;
    @FXML private TextField sipSName;
    @FXML private TextField sipSpFName;
    @FXML private TextField sipSpName;
    @FXML private TextField sipSgName;
    @FXML private ComboBox<Slot.SlotType> sipSType;
    @FXML private TextField sipSClassName;
    @FXML private DatePicker sipSDate;
    @FXML private TextField sipSHourStart;
    @FXML private TextField sipSHourEnd;
    @FXML private ProgressIndicator sipSProgress;
    @FXML private TextField sipSMaxHour;
    @FXML private Button sipOkBtn;
    @FXML private Button sipCancelBtn;

    private Auth currentAuth;

    private enum SlotManagementType { ADD, MODIFY, READ }
    private SlotManagementType managementType;

    private SlotUI slotUI;

    /**
     * Base method call from Parent Controller (aka EdtLhdController)
     * @param controller EdtLhdController parent
     */
    public void setParentController(EdtLhdController controller){
        this.controller = controller;
        this.currentAuth = controller.getCurrentAuth();

        slotInfoPanel.getStyleClass().add("slotInfoPanel");

        //testing only
        sipSProgress.setProgress(0.43);

        setupSlotInfoPanel();
    }

    /**
     * Getter for SlotUi contained inside this Panel
     * @return SlotUi Entity or null
     */
    public SlotUI getSlotUI() { return slotUI; }

    /**
     * Show Slot Info Panel
     */
    public void showSlotInfoPanel() { slotInfoPanel.setVisible(true); }

    /**
     * Hide Slot Info Panel
     */
    public void hideSlotInfoPanel() { slotInfoPanel.setVisible(false); }

    /**
     * Toggle Show/Hide Slot Info Panel
     */
    public void toggleSlotInfoPanel(){
        if (slotInfoPanel.isVisible())
            hideSlotInfoPanel();
        else
            showSlotInfoPanel();
    }

    /**
     * Show Slot Info Panel for Adding a Slot
     */
    public void showAddPanel(){
        managementType = SlotManagementType.ADD;
        showSlotInfoPanel();
        //empty panel
        clearPanelInfo();

        sipDeleteBtn.setVisible(false);
        sipTitle.setVisible(true);
        sipTitle.setText( managementType.name() );
    }

    /**
     * Show Slot Info Panel given a SlotUi (generally called when clicking on a slot on the planning)
     * @param slotUI SlotUi Entity from the planning
     */
    public void showSlotInfoPanel(SlotUI slotUI){
        this.slotUI = slotUI;
        showSlotInfoPanel();
        populateSlotInfoPanel();
    }

    /**
     * Setting up base information on Slot Info Panel
     * populate SlotType Combo Box
     * Show/Hide some fields depending on which types of user is logged in
     */
    private void setupSlotInfoPanel(){
        for (Slot.SlotType type : Slot.SlotType.values())
            sipSType.getItems().add(type);

        boolean isAdmin = currentAuth.isAdmin();

        if(!isAdmin){
            managementType = SlotManagementType.READ;
            sipOkBtn.setText("Ok");
            sipCancelBtn.setText("Fermer");
        }else{
            sipOkBtn.setText("Sauvegarder");
            sipCancelBtn.setText("Annuler");
        }

        sipTitle.setVisible(isAdmin);
        sipDeleteBtn.setVisible(isAdmin);
        sipSName.setDisable(!isAdmin);
        sipSpFName.setDisable(!isAdmin);
        sipSpName.setDisable(!isAdmin);
        sipSType.setDisable(!isAdmin);
        sipSClassName.setDisable(!isAdmin);
        sipSgName.setDisable(!isAdmin);
        sipSDate.setDisable(!isAdmin);
        sipSMaxHour.setDisable(!isAdmin);
        sipSHourStart.setDisable(!isAdmin);
        sipSHourEnd.setDisable(!isAdmin);
    }

    /**
     * Clears every field to default value
     */
    private void clearPanelInfo(){
        sipSName.setText("");
        sipSpFName.setText("");
        sipSpName.setText("");
        sipSType.setValue(Slot.SlotType.CM);
        sipSClassName.setText("");
        sipSgName.setText("");
        sipSDate.setValue( LocalDate.now() );
        sipSMaxHour.setText("");
        sipSHourStart.setText("");
        sipSHourEnd.setText("");
    }

    /**
     * Populate Slot Info Panel from current slotUi
     */
    private void populateSlotInfoPanel(){
        Slot slot = slotUI.getSlot();

        slotInfoPanel.setStyle("-fx-border-color: " + slotUI.getAssociatedColor());

        if (currentAuth.isAdmin()){
            managementType = SlotManagementType.MODIFY;
            sipTitle.setText( managementType.name() );
        }

        sipSName.setText( slot.getSubject().getName() );
        sipSpFName.setText( slot.getProfessors().get(0).getFname() );
        sipSpName.setText( slot.getProfessors().get(0).getName() );

        sipSType.setValue( slot.getType() );

        sipSClassName.setText( slot.getClassroom().getName() );
        sipSgName.setText( slot.getGroup().get(0).getName() );

        sipSDate.setValue( slot.getTimeRange().getStart().atZone(ZoneId.systemDefault()).toLocalDate() );
        sipSMaxHour.setText( "/ " + slot.getSubject().getHourCountMax() + "h");

        String[] hour = slot.getDisplayTimeInterval().split(" - ");
        sipSHourStart.setText( hour[0] );
        sipSHourEnd.setText( hour[1] );
    }

    /**
     * Add new slot
     * Gets all information from each field
     * Calls a method on parent Controller to create a new slot
     */
    private void addNewSlot(){
        //wip
        //need to get all panel entry, maybe call method on edt lhd controller which calls schedule to convert entry to right type
        //exemple, classroom name entry -> string, needs to be Classroom Entity from database
        //need to add method to create subject or classroom in schedule
        System.out.println("ADD SLOT WIP");
    }

    /**
     * Modify a slot
     * Gets all information from each field
     * Calls a method on parent Controller to modify old Slot with new modified information
     */
    private void modifySlot(){
        //wip
        System.out.println("MODIFY SLOT WIP");
    }

    /**
     * Delete a slot
     * Gets all informatino from each field
     * Calls a method on parent Controller to delete the selected slot
     */
    private void deleteSlot(){
        //wip
        System.out.println("DELETE SLOT WIP");
    }

    //region BUTTON EVENT HANDLER

    /**
     * Callback Method when clicking on the ok button
     * do different action based on managementType
     * - ADD => add new slot
     * - MODIFY => modify a slot
     * - default / READ => hide slot info panel
     * @param actionEvent ActionEvent
     */
    @FXML public void sipOkBtnOnClick(ActionEvent actionEvent) {
        switch (managementType){
            case ADD -> addNewSlot();
            case MODIFY -> modifySlot();
            default -> hideSlotInfoPanel();
        }
    }

    /**
     * Callback method when clicking on the cancel button
     * Hides the slot info panel
     * @param actionEvent ActionEvent
     */
    @FXML public void sipCancelBtnOnClick(ActionEvent actionEvent) { hideSlotInfoPanel(); }

    /**
     * Callback method when clicking on the delete button
     * Delete currently selected slot
     * @param actionEvent ActionEvent
     */
    @FXML public void sipDeleteBtnOnClick(ActionEvent actionEvent) { deleteSlot(); }
    //endregion
}
