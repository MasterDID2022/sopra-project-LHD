package fr.univtln.lhd.view.edt;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class EdtGrid extends Grid {

    private List<String> days;
    private List<String> hours;

    private EdtGrid(int rowNumber, int columnNumber){
        super( Grid.builder(rowNumber, columnNumber) );

        days = new ArrayList<>();
        days.add("LUNDI");
        days.add("MARDI");
        days.add("MERCREDI");
        days.add("JEUDI");
        days.add("VENDREDI");

        hours = new ArrayList<>();
        for (int i = 8; i < 18; i++) {
            if (i < 10)
                hours.add("0" + i + ":00");
            else
                hours.add(i + ":00");
        }

        for (int i = 0; i < days.size(); i++){
            FlowPane f = new FlowPane(new Label( days.get(i) ));
            f.setId("day");
            add(f, 0, i+1);
        }

        FlowPane empty = new FlowPane();
        empty.setId("hour");
        add(empty, 0, 0);

        for (int i = 0; i < hours.size(); i++){
            FlowPane f = new FlowPane( new Label( hours.get(i) ) );
            f.setId("hour");
            add(f, i+1, 0);
        }


        this.modifyColumnConstraints(0, 50);
    }

    public static EdtGrid getInstance(){ return new EdtGrid(11, 6); }

    public void add(Node node, String day, String hourStart, String hourEnd){
        int rowIndex = hours.indexOf(hourStart)+1;
        int rowIndexEnd = hours.indexOf(hourEnd)+1;
        int columnIndex = days.indexOf(day)+1;
        super.add(node, rowIndex, columnIndex, rowIndexEnd - rowIndex, 1);
    }
}
