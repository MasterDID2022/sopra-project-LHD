package fr.univtln.lhd.view.edt;

import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import lombok.Getter;

/**
 * WIP
 */
@Getter
public class SlotUI extends FlowPane {

    /*
     CM = white
     */
    private static final String[] colorArray = new String[] {"red", "yellow", "blue", "violet", "white"};

    private Slot slot;
    //private Classroom classroom;
    //private Subject subject;

    private SlotUI(Slot slot){
        super( new Label() );

        this.slot = slot;

        /* Classroom classroom = EdtLhdController.getClassroom( slot.getClassroomId() ); */
        ((Label) this.getChildren().get(0) ).setText( formatSlotLabel(slot) );

        this.getStyleClass().add("slot");

        this.setStyle("-fx-background-color:" + getAssociatedColor(slot) );
    }

    public static SlotUI of(Slot slot) { return new SlotUI(slot); }

    private String formatSlotLabel(Slot slot){
        //get classroom name, subject name, slot type from slot parameter
        //format slot ui label
        //REPLACE WITH CLASSROOM DAO GET CALLED ON CONTROLLER
        StringBuilder tmpLabel = new StringBuilder("SLOT " + slot.getId())
                .append("\nCLASSROOM ")
                .append(slot.getClassroomId())
                .append("\nSUBJECT ")
                .append(slot.getSubjectId());
        tmpLabel.append("\nGROUPS : ");
        for (Group group : slot.getGroup())
            tmpLabel.append(group.getName()).append("\n");

        return tmpLabel.toString();
    }

    private String getAssociatedColor(Slot slot){
        //get color from classroom name, hash it, modulo color array, get color
        int nameIndex = slot.getType().name().hashCode();
        return colorArray[ nameIndex % colorArray.length ];
    }
}
