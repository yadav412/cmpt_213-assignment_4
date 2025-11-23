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
    private final Cell[][] board;
    private final boolean[][] fillStatus; // tracks which cells are part of current fill
    private int minValue;
    private int maxValue;
    private Random random;

    public GameBoard() {
        // generates 2D array of cells (board) that can hold integer values between 0-15
        this.board = new Cell[BOARD_SIZE][BOARD_SIZE];
        this.fillStatus = new boolean[BOARD_SIZE][BOARD_SIZE];
        // initialize board with random values
        initializeMap();
    }

    private void initializeMap() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = new Cell(row, col);
            }
        }
    }

    public int getCellByRowCol(int row, int col) {
        return board[row][col];
    }

    public boolean isCellRowColValid(int row, int col) {
        return (row >= 0 && row < BOARD_SIZE) && (col >= 0 && col < BOARD_SIZE);
    }

    public int getCenter() {
        return board[1][1];
    }

    public int getCell(int row, int col) {
        return getCellByRowCol(row, col);
    }

    public boolean isInFill(int row, int col) {
        return fillStatus[row][col];
    }

    public boolean isOuterCell(int row, int col) {
        return !(row == 1 && col == 1);
    }

    public void setCellValue(int row, int col, int value) {
        board[row][col] = value;
    }

    public void addToFill(int row, int col) {
        if (isOuterCell(row, col)) {
            fillStatus[row][col] = true;
        }
    }

    public void resetFill() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                fillStatus[i][j] = false;
            }
        }
    }

    public boolean isFillComplete() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOuterCell(i, j) && !fillStatus[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<int[]> findMatchingCells(int sum) {
        List<int[]> matches = new ArrayList<>();
        int center = getCenter();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOuterCell(i, j) && board[i][j] + center == sum) {
                    matches.add(new int[]{i, j});
                }
            }
        }
        return matches;
    }

    public int[] getLastAddedCell() {
        // Find the last cell that was added to fill
        // This is a simplified version - in practice we'd track this better
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isOuterCell(i, j) && fillStatus[i][j]) {
                    // Check if this is the last one (simplified)
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public void replaceCell(int row, int col) {
        board[row][col] = random.nextInt(maxValue - minValue + 1) + minValue;
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

    public int[][] getBoard() {
        return board;
    }

    public boolean[][] getFillStatus() {
        return fillStatus;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }
}

