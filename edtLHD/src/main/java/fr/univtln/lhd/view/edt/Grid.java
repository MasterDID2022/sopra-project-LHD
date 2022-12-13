package fr.univtln.lhd.view.edt;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Grid extends GridPane {

    private final int rowNumber;
    private final int columnNumber;

    private double cellMinHeight;
    private double cellPrefHeight;
    private double cellMaxHeight;
    private double cellMinWidth;
    private double cellPrefWidth;
    private double cellMaxWidth;

    public static class Builder {
        private final int rowNumber;
        private final int columnNumber;

        private double cellMinHeight = 20;
        private double cellPrefHeight = 20;
        private double cellMaxHeight = 1000;
        private double cellMinWidth = 30;
        private double cellPrefWidth = 30;
        private double cellMaxWidth = 1000;

        private boolean isGridLinesVisible = true;

        private Builder(int rowNumber, int columnNumber){
            this.rowNumber = rowNumber;
            this.columnNumber = columnNumber;
        }

        public Builder setCellMinHeight(double cellMinHeight) { this.cellMinHeight = cellMinHeight; return this; }
        public Builder setCellPrefHeight(double cellPrefHeight) { this.cellPrefHeight = cellPrefHeight; return this; }
        public Builder setCellMaxHeight(double cellMaxHeight) { this.cellMaxHeight = cellMaxHeight; return this; }
        public Builder setCellMinWidth(double cellMinWidth) { this.cellMinWidth = cellMinWidth; return this; }
        public Builder setCellPrefWidth(double cellPrefWidth) { this.cellPrefWidth = cellPrefWidth; return this; }
        public Builder setCellMaxWidth(double cellMaxWidth) { this.cellMaxWidth = cellMaxWidth; return this; }
        public Builder setIsGridLinesVisible(boolean isGridLinesVisible) { this.isGridLinesVisible = isGridLinesVisible; return this; }

        public Grid build() { return new Grid(this); }
    }

    public static Builder builder(final int rowNumber, final int columnNumber){
        return new Builder(rowNumber, columnNumber);
    }

    protected Grid(Builder builder){
        super();

        rowNumber = builder.rowNumber;
        columnNumber = builder.columnNumber;

        cellMinWidth = builder.cellMinWidth;
        cellPrefWidth = builder.cellPrefWidth;
        cellMaxWidth = builder.cellMaxWidth;

        cellMinHeight = builder.cellMinHeight;
        cellPrefHeight = builder.cellPrefHeight;
        cellMaxHeight = builder.cellMaxHeight;

        this.setGridLinesVisible( builder.isGridLinesVisible );

        for (int i = 0; i < rowNumber; i++)
            addRow();
        for (int i = 0; i < columnNumber; i++)
            addColumn();
    }

    @Override
    public void add(Node node, int rowIndex, int columnIndex){
        super.add(node, columnIndex, rowIndex);
    }

    @Override
    public void add(Node node, int rowIndex, int columnIndex, int rowSpan, int columnSpan) { super.add(node, columnIndex, rowIndex, columnSpan, rowSpan); }

    public void addRow(){
        RowConstraints rowConstraints = new RowConstraints(cellMinHeight, cellPrefHeight, cellMaxHeight, Priority.ALWAYS, VPos.CENTER, true);
        this.getRowConstraints().add(rowConstraints);
    }

    public void addColumn(){
        ColumnConstraints columnConstraints = new ColumnConstraints(cellMinWidth, cellPrefWidth, cellMaxWidth, Priority.ALWAYS, HPos.CENTER, true);
        this.getColumnConstraints().add(columnConstraints);
    }

    public void modifyRowConstraint(int rowIndex, double cellPrefHeight){
        this.getRowConstraints().get(rowIndex).setVgrow(Priority.NEVER);
        this.getRowConstraints().get(rowIndex).setPrefHeight(cellPrefHeight);
    }
    public void modifyColumnConstraints(int columnIndex, double cellPrefWidth){
        this.getColumnConstraints().get(columnIndex).setHgrow(Priority.NEVER);
        this.getColumnConstraints().get(columnIndex).setPrefWidth(cellPrefWidth);
    }

    public void setRowAlignment(int rowIndex, VPos alignment){
        this.getRowConstraints().get(rowIndex).setValignment(alignment);
    }

    public void setColumnAlignment(int columnIndex, HPos alignment){
        this.getColumnConstraints().get(columnIndex).setHalignment(alignment);
    }
}
