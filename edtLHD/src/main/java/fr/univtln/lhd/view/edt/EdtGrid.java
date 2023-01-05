package fr.univtln.lhd.view.edt;

import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.view.slots.SlotUI;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

import static java.time.format.TextStyle.SHORT_STANDALONE;

/**
 * Edt Planning Wrapper class, extending Grid base class
 * Suppressing Warning java:S110, because too much inheritance is caused by using JavaFx Related Objects
 */
@Slf4j
@SuppressWarnings("java:S110")
public class EdtGrid extends Grid {

    public enum Days { LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI }
    public static List<String> hours = new ArrayList<>();

    private LocalDate currentWeekStart;

    /**
     * Base constructor of Edt grid, setup planning for row number and column number
     * Setting up each row's hour label
     * Setting up each column's days label
     * Leaving the planning grid empty of custom cells (Flow Pane Label)
     * @param rowNumber int Number of Rows
     * @param columnNumber int Number of Columns
     */
    private EdtGrid(int rowNumber, int columnNumber){
        super( Grid.builder(rowNumber, columnNumber) );

        for (int i = 8; i < 18; i++)
            hours.add(convertIntToHourLabel(i));

        setCurrentWeekStart();

        int currentWeekDay = currentWeekStart.getDayOfMonth();
        for (int i = 0; i < Days.values().length; i++){
            FlowPane f = new FlowPane(new Label( Days.values()[i].name() + " " + currentWeekDay ));
            f.getStyleClass().add("day");
            add(f, 0, i+1);
            currentWeekDay++;
        }

        FlowPane empty = new FlowPane( new Label( currentWeekStart.getMonth().getDisplayName(SHORT_STANDALONE, Locale.getDefault()).toUpperCase()) );
        empty.getStyleClass().add("day");
        add(empty, 0, 0);

        for (int i = 0; i < hours.size(); i++){
            FlowPane f = new FlowPane( new Label( hours.get(i) ) );
            f.getStyleClass().add("hour");
            add(f, i+1, 0);
        }

        this.modifyColumnConstraints(0, 65);
        setupEmptyGrid();
        changeTodayStyleClass();
    }

    /**
     * Returns an Instance of EdtGrid Class, default rowNumber to 11, and columnNumber to 6
     * from 08:00 to 17:00 and from Monday to Friday
     * @return EdtGrid instance
     */
    public static EdtGrid getInstance(){ return new EdtGrid(11, 6); }

    /**
     * Populate each planning cells to an empty cell
     */
    private void setupEmptyGrid(){
        for (int i = 1; i < getRowNumber(); i++)
            for (int j = 1; j < getColumnNumber(); j++)
                add( getEmptyCell(), i, j);
    }

    /**
     * Returns an empty cell (FlowPane parent, Label children of flow pane)
     * adding a custom style of cell to manipulate its style within css files
     * @return FlowPane empty cell
     */
    private FlowPane getEmptyCell(){
        FlowPane f = new FlowPane( new Label("") );
        f.getStyleClass().add("cell");
        return f;
    }

    /**
     * Update each day's label based on current week
     * getting days name and Date of Month ( example Monday 18 )
     */
    public void updateDaysLabel() {
        LocalDate week = currentWeekStart;
        int currentWeekDay;

        for (int i = 0; i < Days.values().length; i++){
            if (i == 0) {
                ((Label)((FlowPane) get(0, i)).getChildren().get(0)).setText( week.getMonth().getDisplayName(SHORT_STANDALONE, Locale.getDefault()).toUpperCase());
            }

            currentWeekDay = week.getDayOfMonth();
            ((Label)((FlowPane) get(0, i+1)).getChildren().get(0)).setText( Days.values()[i].name() + " " + currentWeekDay );
            week = week.plusDays(1);
        }
    }

    /**
     * Clears each cell from the planning leaving hours and days label untouched
     * Resets slot styles to empty cells styles
     */
    public void clearFullGrid(){
        ObservableList<Node> childs = getChildren();

        for (int i = 0; i < childs.size(); i++) {
            getChildren().get(i).getStyleClass().removeAll("today", "cell_today");

            if (getChildren().get(i) instanceof SlotUI slotUI)
                slotUI.setIsActive(false);
            if (!childs.get(i).getStyleClass().contains("slot")) continue;

            getChildren().get(i).getStyleClass().removeAll("slot", "reduced_slot");
            getChildren().get(i).setStyle("");
            setColumnSpan(childs.get(i), 1);
            setRowSpan(childs.get(i), 1);
            ((Label) ((FlowPane) getChildren().get(i)).getChildren().get(0)).setText("");
        }
        changeTodayStyleClass();
    }

    /**
     * Getter for current Week Start
     * @return LocalDate current week start (monday)
     */
    public LocalDate getCurrentWeekStart() { return currentWeekStart; }

    /**
     * Setup for current week start
     * Gets LocalDateTime.now() and gets the LocalDate of Monday of that week
     */
    public void setCurrentWeekStart() {
        int currentDayOfTheWeekIndex = LocalDateTime.now().getDayOfWeek().ordinal();
        this.currentWeekStart = LocalDateTime.now().minusDays(currentDayOfTheWeekIndex).toLocalDate();
    }

    /**
     * Add weeksToAdd to the current week start then updates days label
     * @param weeksToAdd int Number of Weeks to add
     */
    public void nextWeek(int weeksToAdd) {
        this.currentWeekStart = currentWeekStart.plusWeeks(weeksToAdd);
        updateDaysLabel();
    }

    /**
     * Add 1 week to the current week start
     */
    public void nextWeek() { nextWeek(1); }


    /**
     * Subtract weeksToSubtract to the current week start then updates days label
     * @param weeksToSubtract int Number of Weeks to subtract
     */
    public void previousWeek(int weeksToSubtract) {
        this.currentWeekStart = currentWeekStart.minusWeeks(weeksToSubtract);
        updateDaysLabel();
    }

    /**
     * Subtract 1 week to the current week start
     */
    public void previousWeek() { previousWeek(1); }

    /**
     * Based on today's date and if it's currently displayed on planning
     * Change column style of today's date for aesthetic purposes
     */
    private void changeTodayStyleClass(){
        LocalDate nowDate = LocalDate.now();
        int dayIndex = nowDate.getDayOfWeek().ordinal();
        if (dayIndex >= Days.values().length) return;

        Interval currentWeekInterval = Interval.of(currentWeekStart.atStartOfDay().toInstant(ZoneOffset.UTC), currentWeekStart.plusWeeks(1).atStartOfDay().toInstant(ZoneOffset.UTC));
        Interval nowInterval = Interval.of(Instant.now(), Instant.now().plusSeconds(60));
        if(!currentWeekInterval.overlaps(nowInterval)) return;

        for (int i = 0; i < getRowNumber(); i++)
            get(i, dayIndex+1)
                    .getStyleClass()
                    .add(i == 0 ? "today" : "cell_today");

    }

    /**
     * Converts int hour to hour String Ui Format ( example: 8 => 08:00 )
     * @param hour int Hour to converts
     * @return String Hour formatted
     */
    public static String convertIntToHourLabel(int hour){
        String hourStart;
        if (hour < 10) hourStart = "0" + hour + ":00";
        else hourStart = hour + ":00";
        return hourStart;
    }

    /**
     * Add node to planning grid
     * converts day and hour information to 2D cell coordinates
     * @param node Node to add to planning grid
     * @param day String day
     * @param hourStart String starting hour format
     * @param hourEnd String end hour format
     */
    public void add(Node node, String day, String hourStart, String hourEnd){
        int rowIndex = hours.indexOf(hourStart);
        if (rowIndex == -1) {
            log.error("Slot starting hour out of range");
            return;
        }

        int rowIndexEnd = hours.indexOf(hourEnd);
        if (rowIndexEnd == -1) {
            log.error("Slot ending hour out of range");
            return;
        }

        int columnIndex = Days.valueOf(day).ordinal()+1;
        super.add(node, rowIndex+1, columnIndex, (rowIndexEnd+1) - (rowIndex+1), 1);
    }

    /**
     * Add node to planning grid at Day and specific hour
     * @param node Node to add
     * @param day Days type, day of the week
     * @param hourStart String starting hour format
     * @param hourEnd String end hour format
     */
    public void add(Node node, Days day, String hourStart, String hourEnd){
        add(node, day.name(), hourStart, hourEnd);
    }

    /**
     * Add Node at Interval
     * @param node Node to add
     * @param interval Interval, containing starting and ending hour
     */
    public void add(Node node, Interval interval){
        LocalDateTime localDateTimeStart = LocalDateTime.ofInstant(interval.getStart(), ZoneId.systemDefault());
        LocalTime localTimeEnd = LocalTime.ofInstant(interval.getEnd(), ZoneId.systemDefault());

        int dayIndex = localDateTimeStart.getDayOfWeek().ordinal();
        if (dayIndex >= Days.values().length) dayIndex = Days.values().length-1;

        Days day = Days.values()[ dayIndex ];

        String hourStart = convertIntToHourLabel( localDateTimeStart.getHour() );
        String hourEnd = convertIntToHourLabel( localTimeEnd.getHour() );

        add(node, day, hourStart, hourEnd);
    }

    /**
     * Add SlotUi to planning
     * @param slotUI SlotUI to add
     */
    public void add(SlotUI slotUI){
        add(slotUI, slotUI.getSlot().getTimeRange());
    }

    /**
     * Add Slot to planning
     * @param slot Slot to add
     */
    public void add(Slot slot){
        SlotUI slotUI = SlotUI.of(slot);
        add( slotUI );
    }

    /**
     * Add Slots to planning
     * @param slots List of Slots to add
     */
    public void add(Slot ... slots){
        for(Slot slot : slots)
            add(slot);
    }

    /**
     * Add Slots to planning
     * @param slots List of Slots to add
     */
    public void add(List<Slot> slots){
        for(Slot slot : slots)
            add(slot);
    }

    /**
     * Delete SlotUI from planning
     * @param slotUI SlotUI to delete
     */
    public void delete(SlotUI slotUI){
        getChildren().remove( slotUI );
    }

    /**
     * Replace SlotUI with new Slot
     * @param slotUI SlotUI to delete
     * @param slot Slot to add and replace the slotUi with
     */
    public void replace(SlotUI slotUI, Slot slot){
        delete(slotUI);
        add(slot);
    }
}
