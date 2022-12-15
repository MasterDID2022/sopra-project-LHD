package fr.univtln.lhd.view.edt;

import fr.univtln.lhd.model.entities.slots.Slot;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class SlotUI extends FlowPane {

    private String slotColor = "yellow";

    private SlotUI(Slot slot){
        super( new Label(slot.getClass().getName()) );
        //get classroom name, subject name, slot type from slot parameter
        //format slot ui label

        this.setId("slot");
        this.setStyle("-fx-background-color:" + slotColor);
    }

    public static SlotUI getInstance(Slot slot) { return new SlotUI(slot); }
}
