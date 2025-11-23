package model;

import model.ring.Ring;
import model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private static final int MIN_INT_VALUE = 0;
    private static final int MAX_INT_VALUE = 16;
    private GameBoard board;
    private Player player;
    private List<Opponent> opponents;
    private Fill currentFill;
    private Weapon weapon;
    private Ring[] rings;
    private int totalMovesInFill;  // to track for weapons like Fire Staff
    private StatsTracker stats;
    private List<GameObserver> observers;
    private Random random;
    private int turnCounter;  // for periodic opponent attacks
    private int nextOpponentAttack;


    public GameEngine(Player player) {
        this.board = new GameBoard();
        this.player = player;
        this.opponents = createOpponents();
        this.currentFill = new Fill();
        this.totalMovesInFill = 0;
        this.observers = new ArrayList<>();
        this.stats = new StatsTracker();
        this.random = new Random();
        this.turnCounter = 0;

        // Register stats as observer
        registerObserver(stats);
    }

    public MoveResult processMove(int enteredSum) {
        turnCounter++;
        List<Cell> matches = board.findMatchingCells(enteredSum);

        // if enteredSum is invalid, then move has failed, opponent attacks
        if (matches.isEmpty()) {
            notifyObservers(new FailedMoveEvent());
            triggerOpponentAttack("failed_move");
            return new MoveResult(false,  "No match for sum: " + enteredSum);
        }

        // enteredSum is valid, update Fill
        Cell selected = selectBestMatch(matches);

        // Track if this is a re-selection (for weapons)
        boolean isReselection = selected.isInFill();

        // Add to fill (or re-add for weapons that count total moves)
        if (!isReselection) {
            currentFill.addCellToFill(selected);
            selected.addToFill();  // Mark cell as part of fill
        } else {
            // Re-selection still counts for some weapon conditions
            currentFill.addReselection(selected);
        }

        totalMovesInFill++;

        // Notify observers of successful move
        notifyObservers(new SuccessfulMoveEvent(selected, currentFill.getStrength()));

        // Update map, center becomes outer value, outer gets new random
        board.updateAfterMove(selected);

        // Check if fill is complete (8 unique outer cells)
        if (currentFill.isComplete()) {
            player.attack(opponents, currentFill);  // Player handles attack logic
            resetFill();
        }

        // Check for periodic opponent attack (every 3-5 turns)
        checkPeriodicOpponentAttack();

        return new MoveResult(true, "Selected cell at (" + selected.getRow() +
                "," + selected.getCol() + ")");
    }

    public void displayGear() {
        Weapon weapon = player.getWeapon();
        Ring[] rings = player.getRings();
        // TODO display weapon and rings
    }

}
