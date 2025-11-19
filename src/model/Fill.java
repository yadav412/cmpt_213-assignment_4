package model;

import java.util.ArrayList;
import java.util.List;

public class Fill {
    private int strength;
    private List<Integer> selectedValues;  // For pattern checking (weapons)
    private List<Cell> fillOrder;          // For tracking order/final cell
    private long startTime;                // For time-based weapons

    public Fill() {
        this.strength = 0;
        this.selectedValues = new ArrayList<>();
        this.fillOrder = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    public void addCellToFill(Cell cell) {
        cell.addToFill();  // Mark cell as part of fill
        strength += cell.getValue();
        selectedValues.add(cell.getValue());
        fillOrder.add(cell);  // Track order for weapons/targeting
    }

    public boolean isComplete() {
        return fillOrder.size() >= 8;  // All unique outer cells
    }

    public Cell getFinalCell() {
        return fillOrder.get(fillOrder.size() - 1);
    }

    public boolean isAscending() {
        // Check for Frost Bow weapon
        for (int i = 1; i < selectedValues.size(); i++) {
            if (selectedValues.get(i) < selectedValues.get(i-1)) {
                return false;
            }
        }
        return true;
    }

    public int getMoveCount() {
        return selectedValues.size();  // Total moves including re-selections
    }
}
