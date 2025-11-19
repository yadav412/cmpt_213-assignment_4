package model;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private static final int BOARD_SIZE = 3;
    private final Cell[][] cells;
    private Fill currentFill;

    public Map() {
        this.cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        this.currentFill = new Fill();
    }

    public int getCentreValue() {
        return cells[1][1].getValue();
    }

    // returns an array of all 8 outer cells
    public List<Cell> getOuterCells() {
        List<Cell> outer = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (!(row == 1 && col == 1)) {
                    outer.add(cells[row][col]);
                }
            }
        }
        return outer;
    }

    // Find which outer cell(s) match the entered sum
    public List<Cell> findMatchingCells(int enteredSum) {
        // matches is a list that contains a possible 1 to 8 repetitions of the matching value
        List<Cell> matches = new ArrayList<>();
        int centerVal = getCentreValue();

        for (Cell outer : getOuterCells()) {
            if (outer.getValue() + centerVal == enteredSum) {
                matches.add(outer);
            }
        }
        return matches;
    }
}
