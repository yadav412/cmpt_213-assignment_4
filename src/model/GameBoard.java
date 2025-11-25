package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the 3x3 game board.
 * The center cell is at position [1][1].
 * Outer cells are the 8 cells around the edge.
 */
public class GameBoard {
    private static final int BOARD_SIZE = 3; // 3x3 board size (indices: 0-2)
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 15;
    private final Cell[][] board;
    private int minValue;
    private int maxValue;
    private final Random random;
    private Cell lastAddedCell;

    public GameBoard() {
        // generates 2D array of cells (board) that can hold integer values between 0-15
        this.board = new Cell[BOARD_SIZE][BOARD_SIZE];
        this.minValue = DEFAULT_MIN;
        this.maxValue = DEFAULT_MAX;
        this.random = new Random();
        initializeCells(); // initialize board with random values
    }

    // A board of cells is generated with a random number within the range of min -
    // max
    // min and max are stored in GameBoard but passed to the cell to create itself
    private void initializeCells() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int initialValue = random.nextInt(maxValue - minValue + 1) + minValue;
                board[row][col] = new Cell(row, col, initialValue);
            }
        }
    }

    public void regenerateCellValue(int row, int col) {
        int newValue = random.nextInt(maxValue - minValue + 1) + minValue;
        board[row][col].setValue(newValue);
    }

    public Cell getCellByRowCol(int row, int col) {
        return board[row][col];
    }

    public boolean isCellRowColValid(int row, int col) {
        return (row >= 0 && row < BOARD_SIZE) && (col >= 0 && col < BOARD_SIZE);
    }

    public Cell getCenter() {
        return board[1][1];
    }

    public Cell getCell(int row, int col) {
        return getCellByRowCol(row, col);
    }

    public boolean isInFill(int row, int col) {
        return board[row][col].isInFill(); // ask the cell if it is in the fill
    }

    public boolean isOuterCell(int row, int col) {
        return !(row == 1 && col == 1);
    }

    public void setCellValue(int row, int col, int value) {
        board[row][col].setValue(value);
    }

    public int getCellValue(int row, int col) {
        return board[row][col].getValue();
    }

    public void addToFill(int row, int col) {
        if (isOuterCell(row, col)) {
            lastAddedCell = board[row][col];
            board[row][col].addToFill(); // cell marks itself as being part of the fill
        }
    }

    public void resetFill() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j].resetFillStatus();
            }
        }
    }

    public boolean isFillComplete() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOuterCell(i, j) && !board[i][j].isInFill()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<int[]> findMatchingCells(int sum) {
        List<int[]> matches = new ArrayList<>();
        int center = getCenter().getValue();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOuterCell(i, j) && board[i][j].getValue() + center == sum) {
                    matches.add(new int[] { i, j });
                }
            }
        }
        return matches;
    }

    // Returns last cell added to fill. Cannot be null, no null check required, only
    // called once fill is complete.
    public Cell getLastAddedCell() {
        return lastAddedCell;
    }

    public void setValueRange(int min, int max) {
        this.minValue = min;
        this.maxValue = max;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
