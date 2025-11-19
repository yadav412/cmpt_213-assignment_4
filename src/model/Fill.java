package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the current fill state, including strength and cells added.
 */
public class Fill {
    private int strength;
    private List<int[]> cellsAdded;
    private List<Integer> valuesAdded;
    private long startTime;
    private int cellCount;

    public Fill() {
        this.strength = 0;
        this.cellsAdded = new ArrayList<>();
        this.valuesAdded = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
        this.cellCount = 0;
    }

    public void addCell(int row, int col, int value) {
        cellsAdded.add(new int[]{row, col});
        valuesAdded.add(value);
        strength += value;
        cellCount++;
    }

    public void reset() {
        strength = 0;
        cellsAdded.clear();
        valuesAdded.clear();
        startTime = System.currentTimeMillis();
        cellCount = 0;
    }

    public int getStrength() {
        return strength;
    }

    public int getCellCount() {
        return cellCount;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public List<Integer> getValuesAdded() {
        return new ArrayList<>(valuesAdded);
    }

    public int[] getLastCell() {
        if (cellsAdded.isEmpty()) {
            return null;
        }
        return cellsAdded.getLast();
    }

    public boolean isAscending() {
        if (valuesAdded.size() < 2) {
            return true;
        }
        for (int i = 1; i < valuesAdded.size(); i++) {
            if (valuesAdded.get(i) <= valuesAdded.get(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public boolean isDescending() {
        if (valuesAdded.size() < 2) {
            return true;
        }
        for (int i = 1; i < valuesAdded.size(); i++) {
            if (valuesAdded.get(i) >= valuesAdded.get(i - 1)) {
                return false;
            }
        }
        return true;


    }

    @Override
    public String toString() {
        return "Fill(" + strength + ")";
    }
}

