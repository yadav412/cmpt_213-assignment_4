package model;

/**
 * A cell that contains a randomly generated number between 0-15. Tracks state of a cell
 */

public class Cell {
    private int value;           // The number (0-15)
    private boolean isPartOfCurrentFill;
    private final int row;
    private final int col;

    public Cell(int row, int col, int initialValue) {
        this.row = row;
        this.col = col;
        this.value = initialValue;
        this.isPartOfCurrentFill = false;
    }

    public void addToFill() {
        this.isPartOfCurrentFill = true;
    }

    public void resetFillStatus() {
        this.isPartOfCurrentFill = false;
    }

    public boolean isInFill() {
        return isPartOfCurrentFill;
    }

    public boolean isCenter() {
        return row == 1 && col == 1;
    }

    public boolean isOuter() {
        return !isCenter();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        this.value = newValue;
    }
}
