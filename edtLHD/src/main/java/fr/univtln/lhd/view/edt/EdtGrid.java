package fr.univtln.lhd.view.edt;

import fr.univtln.lhd.model.entities.slots.Slot;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

@Slf4j
@SuppressWarnings("java:S110")//Using an IHM imply a lot inheritance
public class EdtGrid extends Grid {

    public enum Days { LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI }
    private final List<String> hours;

    private LocalDate currentWeekStart;

    private EdtGrid(int rowNumber, int columnNumber){
        super( Grid.builder(rowNumber, columnNumber) );

        hours = new ArrayList<>();
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

        FlowPane empty = new FlowPane( new Label( currentWeekStart.getMonth().name().toUpperCase().substring(0, 3) + "." ) );
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

    public static EdtGrid getInstance(){ return new EdtGrid(11, 6); }

    private void setupEmptyGrid(){
        for (int i = 1; i < getRowNumber(); i++){
            for (int j = 1; j < getColumnNumber(); j++){
                FlowPane f = new FlowPane( new Label("") );
                f.getStyleClass().add("cell");
                add(f, i, j);
            }
        }
    }

    public void updateDaysLabel() {
        LocalDate week = currentWeekStart;
        int currentWeekDay;

        for (int i = 0; i < Days.values().length; i++){
            if (i == 0) {
                ((Label)((FlowPane) get(0, i)).getChildren().get(0)).setText( week.getMonth().name().toUpperCase().substring(0, 3) + "." );
            }

            currentWeekDay = week.getDayOfMonth();
            ((Label)((FlowPane) get(0, i+1)).getChildren().get(0)).setText( Days.values()[i].name() + " " + currentWeekDay );
            week = week.plusDays(1);
        }
    }

    public void clearFullGrid(){
        ObservableList<Node> childs = getChildren();

        for (int i = 0; i < childs.size(); i++) {
            getChildren().get(i).getStyleClass().removeAll("today", "cell_today");

            if (!childs.get(i).getStyleClass().contains("slot")) continue;

            getChildren().get(i).getStyleClass().removeAll("slot", "reduced_slot");
            getChildren().get(i).setStyle("");
            setColumnSpan(childs.get(i), 1);
            setRowSpan(childs.get(i), 1);
            ((Label) ((FlowPane) getChildren().get(i)).getChildren().get(0)).setText("");
        }
        changeTodayStyleClass();
    }

    public LocalDate getCurrentWeekStart() { return currentWeekStart; }

    public void setCurrentWeekStart() {
        int currentDayOfTheWeekIndex = LocalDateTime.now().getDayOfWeek().ordinal();
        this.currentWeekStart = LocalDateTime.now().minusDays(currentDayOfTheWeekIndex).toLocalDate();
    }

    public void nextWeek(int weeksToAdd) {
        this.currentWeekStart = currentWeekStart.plusWeeks(weeksToAdd);
        updateDaysLabel();
    }
    public void nextWeek() { nextWeek(1); }

    public void previousWeek(int weeksToSubtract) {
        this.currentWeekStart = currentWeekStart.minusWeeks(weeksToSubtract);
        updateDaysLabel();
    }
    public void previousWeek() { previousWeek(1); }

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

    private String convertIntToHourLabel(int hour){
        String hourStart;
        if (hour < 10) hourStart = "0" + hour + ":00";
        else hourStart = hour + ":00";
        return hourStart;
    }

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

    public void add(Node node, Days day, String hourStart, String hourEnd){
        add(node, day.name(), hourStart, hourEnd);
    }

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

    public void add(SlotUI slotUI){
        add(slotUI, slotUI.getSlot().getTimeRange());
    }

    public void add(Slot slot){
        SlotUI slotUI = SlotUI.of(slot);
        add( slotUI );
    }

    public void add(Slot ... slots){
        for(Slot slot : slots)
            add(slot);
    }
    public void add(List<Slot> slots){
        for(Slot slot : slots)
            add(slot);
    }
}
