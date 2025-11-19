package model;

/**
 * Tracks state of a cell
 */

public class Cell {
    private int value;           // The number (0-15)
    private boolean isPartOfCurrentFill;
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.value = generateRandomValue();
        this.isPartOfCurrentFill = false;
        this.row = row;
        this.col = col;
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

    private int generateRandomValue() {
        return (int)(Math.random() * 16);  // 0-15
    }

    public boolean isCenter() {
        return row == 1 && col == 1;
    }

    public boolean isOuter() {
        return !isCenter();
    }
}
