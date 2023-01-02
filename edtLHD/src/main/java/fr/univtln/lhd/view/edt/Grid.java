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

/**
 * Grid Wrapper class for manipulating GridPane JavaFX object with ease of use
 * Suppressing Warning java:S110, because too much inheritance is caused by using JavaFx Related Objects
 */
@Getter
@Setter
@SuppressWarnings("java:S110")
public class Grid extends GridPane {

    private final int rowNumber;
    private final int columnNumber;

    private double cellMinHeight;
    private double cellPrefHeight;
    private double cellMaxHeight;
    private double cellMinWidth;
    private double cellPrefWidth;
    private double cellMaxWidth;

    /**
     * Grid Builder class
     */
    public static class Builder {
        private final int rowNumber;
        private final int columnNumber;

        private double cellMinHeight = 20;
        private double cellPrefHeight = 20;
        private double cellMaxHeight = 1000;
        private double cellMinWidth = 30;
        private double cellPrefWidth = 30;
        private double cellMaxWidth = 1000;

        private boolean isGridLinesVisible = false;

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

    /**
     * Static Builder Factory given rowNumber and columnNumber
     * @param rowNumber int Number of Rows of the grid
     * @param columnNumber int Number of Column of the grid
     * @return Grid Builder to build from
     */
    public static Builder builder(final int rowNumber, final int columnNumber){
        return new Builder(rowNumber, columnNumber);
    }

    /**
     * Grid Constructor
     * @param builder Builder parameter
     */
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

    /**
     * Add method, override because base add method call was tedious to use properly
     * Base add method uses first columnIndex parameter then rowIndex parameter, which is odd
     * So Overriding it lets us use add method rowIndex, then columnIndex
     * @param node Node to add
     * @param rowIndex int RowIndex to place the node to
     * @param columnIndex int ColumnIndex to place the node to
     */
    @Override
    public void add(Node node, int rowIndex, int columnIndex){
        super.add(node, columnIndex, rowIndex);
    }

    /**
     * Add method with extra row and column span parameters
     * Same Overriding logic for the base add method
     * with extra parameters
     * @param node Node to add
     * @param rowIndex int RowIndex to place the node to
     * @param columnIndex int ColumnIndex to place the node to
     * @param rowSpan int How much the node takes row space
     * @param columnSpan int How much the node takes column space
     */
    @Override
    public void add(Node node, int rowIndex, int columnIndex, int rowSpan, int columnSpan) { super.add(node, columnIndex, rowIndex, columnSpan, rowSpan); }

    /**
     * Returns the Node object at cell 2D coordinates [ rowIndex, columnIndex ]
     * @param rowIndex int Row Coordinates
     * @param columnIndex int Column Coordinates
     * @return Node at [ rowIndex, columnIndex ] in grid, if none returns null
     */
    public Node get(int rowIndex, int columnIndex){
        for (Node node : this.getChildren()){
            if(GridPane.getRowIndex(node) == rowIndex && GridPane.getColumnIndex(node) == columnIndex)
                return node;
        }
        return null;
    }

    /**
     * Add a row to the grid
     */
    public void addRow(){
        RowConstraints rowConstraints = new RowConstraints(cellMinHeight, cellPrefHeight, cellMaxHeight, Priority.ALWAYS, VPos.CENTER, true);
        this.getRowConstraints().add(rowConstraints);
    }

    /**
     * Add a column to the grid
     */
    public void addColumn(){
        ColumnConstraints columnConstraints = new ColumnConstraints(cellMinWidth, cellPrefWidth, cellMaxWidth, Priority.ALWAYS, HPos.CENTER, true);
        this.getColumnConstraints().add(columnConstraints);
    }

    /**
     * Modify a row constraint
     * @param rowIndex int Row Index to modify
     * @param cellPrefHeight double New Pref Height
     */
    public void modifyRowConstraint(int rowIndex, double cellPrefHeight){
        this.getRowConstraints().get(rowIndex).setVgrow(Priority.NEVER);
        this.getRowConstraints().get(rowIndex).setPrefHeight(cellPrefHeight);
    }

    /**
     * Modify a column constraint
     * @param columnIndex int Column Index to modify
     * @param cellPrefWidth double New Pref Width
     */
    public void modifyColumnConstraints(int columnIndex, double cellPrefWidth){
        this.getColumnConstraints().get(columnIndex).setHgrow(Priority.NEVER);
        this.getColumnConstraints().get(columnIndex).setPrefWidth(cellPrefWidth);
    }

    /**
     * Sets Row Alignment
     * @param rowIndex int Row Index
     * @param alignment VPos alignment type
     */
    public void setRowAlignment(int rowIndex, VPos alignment){
        this.getRowConstraints().get(rowIndex).setValignment(alignment);
    }

    /**
     * Sets Column Alignment
     * @param columnIndex int Row Index
     * @param alignment HPos alignment type
     */
    public void setColumnAlignment(int columnIndex, HPos alignment){
        this.getColumnConstraints().get(columnIndex).setHalignment(alignment);
    }

    /**
     * Sets Row Priority
     * @param rowIndex int Row Index
     * @param vGrow Priority Vertical Grow
     */
    public void setRowPriority(int rowIndex, Priority vGrow){
        this.getRowConstraints().get(rowIndex).setVgrow(vGrow);
    }

    /**
     * Sets Column Priority
     * @param columnIndex int Column Index
     * @param hGrow Priority Horizontal Grow
     */
    public void setColumnPriority(int columnIndex, Priority hGrow){
        this.getColumnConstraints().get(columnIndex).setHgrow(hGrow);
    }
}
