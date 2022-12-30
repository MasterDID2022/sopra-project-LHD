package fr.univtln.lhd.view.slots;

import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.users.Professor;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import lombok.Getter;

/**
 * Slot Ui wrapper class extending FlowPane Node
 * used for converting a Slot to SlotUi, displaying it afterwards correctly
 */
@Getter
@SuppressWarnings("java:S110")//Using an IHM imply a lot of inheritance
public class SlotUI extends FlowPane {

    private static final int MAX_WIDTH_LABEL = 27;
    private static final int MIN_HOUR_INTERVAL = 2;

    //CM, TD, TP, EXAM, CONFERENCE, REUNION, OTHER
    private static final String[] colorArray = new String[] {"#EBCB8B", "#A3BE8C", "#B48EAD", "#BF616A", "#5E81AC", "#D08770"};

    private final Slot slot;
    private final Classroom classroom;
    private final Subject subject;

    private boolean isActive = true;

    private SlotUI(Slot slot){
        super();
        Label l = new Label();
        l.setMouseTransparent(true);
        this.getChildren().add( l );

        this.slot = slot;
        this.classroom = slot.getClassroom();
        this.subject = slot.getSubject();

        ((Label) this.getChildren().get(0) ).setText( formatSlotLabel() );
        this.getStyleClass().add("slot");
        this.setStyle("-fx-background-color:" + getAssociatedColor() );
    }

    public static SlotUI of(Slot slot) { return new SlotUI(slot); }

    public boolean isActive() { return isActive; }
    public void setIsActive(boolean value) { isActive = value; }

    private String limitWidthFormat(String text){
        if (text.length() > MAX_WIDTH_LABEL)
            return text.substring(0, MAX_WIDTH_LABEL-3) + "...\n";
        return text + '\n';
    }

    private String formatSlotLabel(){
        StringBuilder slotLabel = new StringBuilder( limitWidthFormat(subject.getName()) );

        long hoursInterval = slot.getTimeRange().toDuration().toHours();

        if (hoursInterval >= MIN_HOUR_INTERVAL){
            for (Professor professor : slot.getProfessors())
                slotLabel.append( limitWidthFormat(professor.getDisplayName()) );
            slotLabel.append( limitWidthFormat(slot.getType().name()) );
        }

        slotLabel.append( limitWidthFormat(classroom.getName()) );

        if (hoursInterval >= MIN_HOUR_INTERVAL)
            for (Group group : slot.getGroup())
                slotLabel.append( limitWidthFormat(group.getName()) );

        slotLabel.append(slot.getDisplayTimeInterval());


        if (hoursInterval < MIN_HOUR_INTERVAL){
            slotLabel.append("\n...");
            this.getStyleClass().add("reduced_slot");
        }

        return slotLabel.toString();
    }

    /**
     * Take a slot and return the color associate with his type
     * @return a string representing a color
     */
    public String getAssociatedColor(){
        //get color from classroom name, hash it, modulo color array, get color
        return colorArray[ slot.getType().ordinal() % colorArray.length ];
    }
}
