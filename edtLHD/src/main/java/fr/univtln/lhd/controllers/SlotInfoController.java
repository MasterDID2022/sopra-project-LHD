package fr.univtln.lhd.controllers;

import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.users.Admin;
import fr.univtln.lhd.model.entities.users.User;
import fr.univtln.lhd.view.slots.SlotUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.ZoneId;

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
    @FXML private Button sipOkBtn;
    @FXML private Button sipCancelBtn;

    private User currentAuthUser;

    private enum SlotManagementType { ADD, MODIFY, READ }
    private SlotManagementType managementType;

    private SlotUI slotUI;

    public void setParentController(EdtLhdController controller){
        this.controller = controller;
        this.currentAuthUser = controller.getCurrentAuthStudent();

        slotInfoPanel.getStyleClass().add("slotInfoPanel");

        //testing only
        sipSProgress.setProgress(0.43);

        setupSlotInfoPanel();
    }

    public SlotUI getSlotUI() { return slotUI; }

    public void showSlotInfoPanel() { slotInfoPanel.setVisible(true); }
    public void hideSlotInfoPanel() { slotInfoPanel.setVisible(false); }

    public void toggleSlotInfoPanel(){
        if (slotInfoPanel.isVisible())
            hideSlotInfoPanel();
        else
            showSlotInfoPanel();
    }

    public void showAddPanel(){
        managementType = SlotManagementType.ADD;
        showSlotInfoPanel();
        //empty panel
        clearPanelInfo();

        sipDeleteBtn.setDisable(true);
        sipTitle.setVisible(true);
        sipTitle.setText( managementType.name() );
    }

    public void showSlotInfoPanel(SlotUI slotUI){
        this.slotUI = slotUI;
        showSlotInfoPanel();
        populateSlotInfoPanel();
    }

    private void setupSlotInfoPanel(){
        for (Slot.SlotType type : Slot.SlotType.values())
            sipSType.getItems().add(type);

        boolean isAdmin = currentAuthUser instanceof Admin;

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
        sipSName.setDisable(isAdmin);
        sipSpFName.setDisable(isAdmin);
        sipSpName.setDisable(isAdmin);
        sipSType.setDisable(isAdmin);
        sipSClassName.setDisable(isAdmin);
        sipSgName.setDisable(isAdmin);
        sipSDate.setDisable(isAdmin);
        sipSHourStart.setDisable(isAdmin);
        sipSHourEnd.setDisable(isAdmin);
    }

    private void clearPanelInfo(){
        sipSName.setText("");
        sipSpFName.setText("");
        sipSpName.setText("");
        sipSType.setValue(Slot.SlotType.CM);
        sipSClassName.setText("");
        sipSgName.setText("");
        sipSDate.setValue( LocalDate.now() );
        sipSHourStart.setText("");
        sipSHourEnd.setText("");
    }

    private void populateSlotInfoPanel(){
        Slot slot = slotUI.getSlot();

        slotInfoPanel.setStyle("-fx-border-color: " + slotUI.getAssociatedColor());

        if (currentAuthUser instanceof Admin){
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
        String[] hour = slot.getDisplayTimeInterval().split(" - ");
        sipSHourStart.setText( hour[0] );
        sipSHourEnd.setText( hour[1] );
    }

    private void addNewSlot(){
        //wip
        //need to get all panel entry, maybe call method on edt lhd controller which calls schedule to convert entry to right type
        //exemple, classroom name entry -> string, needs to be Classroom Entity from database
        //need to add method to create subject or classroom in schedule
        System.out.println("ADD SLOT WIP");
    }

    private void modifySlot(){
        //wip
        System.out.println("MODIFY SLOT WIP");
    }

    private void deleteSlot(){
        //wip
        System.out.println("DELETE SLOT WIP");
    }

    //region BUTTON EVENT HANDLER
    @FXML public void sipOkBtnOnClick(ActionEvent actionEvent) {
        switch (managementType){
            case ADD -> addNewSlot();
            case MODIFY -> modifySlot();
            default -> hideSlotInfoPanel();
        }
    }

    @FXML public void sipCancelBtnOnClick(ActionEvent actionEvent) { hideSlotInfoPanel(); }

    @FXML public void sipDeleteBtnOnClick(ActionEvent actionEvent) { deleteSlot(); }
    //endregion
}
