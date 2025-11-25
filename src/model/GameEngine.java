package model;

import model.ring.*;
import model.weapon.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private GameBoard board;
    private Player player;
    private Opponent[] opponents;
    private Fill currentFill;
    private StatsTracker stats;
    private List<GameObserver> observers;
    private Random random;
    private int turnCounter; // for periodic opponent attacks
    private int nextOpponentAttack;
    private boolean cheatLowHealth;
    private boolean cheatHighHealth;
    private int basePlayerHealth;
    private int baseOpponentHealth;
    private int baseOpponentDamage;

    public GameEngine() {
        this.board = new GameBoard();
        this.currentFill = new Fill();
        this.observers = new ArrayList<>();
        this.stats = new StatsTracker();
        // TODO: Register stats as observer so it can track game events
        // Missing: registerObserver(stats);
        this.random = new Random();
        this.turnCounter = 0;

        this.cheatLowHealth = false;
        this.cheatHighHealth = false;
        this.basePlayerHealth = 620;
        this.baseOpponentHealth = 500;
        this.baseOpponentDamage = 50;

        startNewMatch();
    }

    public void startNewMatch() {
        this.board = new GameBoard();
        this.player = new Player(basePlayerHealth); // create/reset player

        int opponentHealth = baseOpponentHealth;
        if (cheatLowHealth) {
            opponentHealth = 50;
        } else if (cheatHighHealth) {
            opponentHealth = 2000;
        }

        this.opponents = new Opponent[3];
        for (int i = 0; i < 3; i++) {
            opponents[i] = new Opponent(opponentHealth);
        }

        this.currentFill = new Fill();
        this.turnCounter = 0;
        this.nextOpponentAttack = random.nextInt(3) + 3; // 3-5 turns

        notifyObservers(new GameEvent(GameEvent.EventType.FILL_STARTED, "GameEngine", null));
    }

    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.notify(event);
        }
    }

    public boolean processSum(int sum) {
        turnCounter++;

        List<int[]> matches = board.findMatchingCells(sum);

        if (matches.isEmpty()) {
            // Failed move - opponent attacks
            handleFailedMove();
            return false;
        }

        // Find a matching cell not yet in fill
        int[] chosenCell = null;
        for (int[] cell : matches) {
            if (!board.isInFill(cell[0], cell[1])) {
                chosenCell = cell;
                break;
            }
        }

        // If all matches are in fill, randomly choose one
        if (chosenCell == null && !matches.isEmpty()) {
            chosenCell = matches.get(random.nextInt(matches.size()));
        }

        if (chosenCell != null) {
            handleSuccessfulMove(chosenCell);
            return true;
        }

        return false;
    }

    private void handleSuccessfulMove(int[] cell) {
        int row = cell[0];
        int col = cell[1];
        int value = board.getCellValue(row, col);

        // Check if fill was already complete before this move
        boolean wasComplete = board.isFillComplete();

        // Add to fill (even if already in fill - allows re-selection)
        // NOTE: We add the OLD value to strength, then regenerate the cell with a NEW
        // value
        // This allows Fire Staff to count re-selections (cellCount > 8) while using old
        // values for strength
        board.addToFill(row, col);
        currentFill.addCell(row, col, value); // Adds OLD value to strength

        // Update center - replace center value with the selected cell's OLD value
        board.setCellValue(1, 1, value);

        // Replace cell with NEW random value (for next turn)
        board.regenerateCellValue(row, col);

        notifyObservers(new GameEvent(GameEvent.EventType.CELL_ADDED_TO_FILL, "GameEngine",
                new GameEvent.CellAddedData(row, col, value, currentFill.getStrength())));

        // Check if fill just became complete (all 8 outer cells now filled)
        // Attack triggers immediately when fill becomes complete
        // Note: cell count can be >8 if cells were re-selected before completion
        if (!wasComplete && board.isFillComplete()) {
            // TODO: Fire FILL_COMPLETED event before attack (StatsTracker needs this)
            // Missing: notifyObservers(new GameEvent(GameEvent.EventType.FILL_COMPLETED,
            // "GameEngine", null));
            performPlayerAttack();
            // NOTE: Order matters - reset fill AFTER attack so attack can use currentFill
            // data
            board.resetFill();
            currentFill.reset();
            notifyObservers(new GameEvent(GameEvent.EventType.FILL_STARTED, "GameEngine", null));
        }

        // Random opponent attack every 3-5 turns
        if (turnCounter >= nextOpponentAttack) {
            performOpponentAttack();
            nextOpponentAttack = turnCounter + random.nextInt(3) + 3;
        }
    }

    private void handleFailedMove() {
        performOpponentAttack();
        notifyObservers(new GameEvent(GameEvent.EventType.TURN_FAILED, "GameEngine", null));
    }

    private void performPlayerAttack() {
        // Player handles all attack logic and returns the data
        // TODO: Player.attack() needs to fire EQUIPMENT_ACTIVATED events, but Player
        // doesn't have access to observers
        // FIXME: Either pass GameEngine reference to Player, or have Player return
        // equipment activation info
        // and fire events here in GameEngine
        GameEvent.AttackData attackData = player.attack(opponents, currentFill);

        // Notify observers
        notifyObservers(new GameEvent(GameEvent.EventType.ATTACK_PERFORMED, "Player", attackData));

        // Check if match won
        if (allOpponentsDefeated()) {
            notifyObservers(new GameEvent(GameEvent.EventType.MATCH_WON, "GameEngine", null));
        }
    }

    public Weapon getRandomWeapon() {
        int weaponNum = random.nextInt(6) + 1;
        switch (weaponNum) {
            case 1:
                return new LightningWand();
            case 2:
                return new FireStaff();
            case 3:
                return new FrostBow();
            case 4:
                return new StoneHammer();
            case 5:
                return new DiamondSword();
            case 6:
                return new SparkleDagger();
            default:
                return new NoWeapon();
        }
    }

    private Ring createRing(int ringNumber) {
        return switch (ringNumber) {
            case 0 -> new NoRing();
            case 1 -> new BigOneRing();
            case 2 -> new LittleOneRing();
            case 3 -> new TenacityRing();
            case 4 -> new MehRing();
            case 5 -> new PrimeDirectiveRing();
            case 6 -> new TwoRing();
            default -> throw new IllegalArgumentException("Invalid ring number: " + ringNumber);
        };
    }

    private Weapon createWeapon(int weaponNumber) {
        return switch (weaponNumber) {
            case 0 -> new NoWeapon();
            case 1 -> new LightningWand();
            case 2 -> new FireStaff();
            case 3 -> new FrostBow();
            case 4 -> new StoneHammer();
            case 5 -> new DiamondSword();
            case 6 -> new SparkleDagger();
            default -> throw new IllegalArgumentException("Invalid weapon number: " + weaponNumber);
        };
    }

    public Ring getRandomRing() {
        int ringNum = random.nextInt(6) + 1;
        return createRing(ringNum);
    }

    public void equipPlayerRings(int ring1, int ring2, int ring3) {
        player.setRings(createRing(ring1), createRing(ring2), createRing(ring3));
    }

    public void equipPlayerWeapon(int weaponNumber) {
        player.setWeapon(createWeapon(weaponNumber));
    }

    private void performOpponentAttack() {
        // Choose random alive opponent to attack
        List<Integer> aliveIndices = new ArrayList<>();
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].isAlive()) {
                aliveIndices.add(i);
            }
        }

        if (aliveIndices.isEmpty() || !player.isAlive()) {
            return;
        }

        int attackerIndex = aliveIndices.get(random.nextInt(aliveIndices.size()));
        int damage = baseOpponentDamage;
        player.takeDamage(damage);

        notifyObservers(new GameEvent(GameEvent.EventType.OPPONENT_ATTACKED, "GameEngine",
                new GameEvent.OpponentAttackData(attackerIndex, damage)));
        notifyObservers(new GameEvent(GameEvent.EventType.CHARACTER_DAMAGED, "GameEngine",
                new StatsTracker.DamageData(damage, true)));

        if (!player.isAlive()) {
            notifyObservers(new GameEvent(GameEvent.EventType.MATCH_LOST, "GameEngine", null));
        }
    }

    private boolean allOpponentsDefeated() {
        for (Opponent opponent : opponents) {
            if (opponent.isAlive()) {
                return false;
            }
        }
        return true;
    }

    // Getters
    public GameBoard getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public Opponent[] getOpponents() {
        return opponents;
    }

    public Fill getCurrentFill() {
        return currentFill;
    }
    // TODO: Add getStats() method - TextUI is trying to call gameEngine.getStats()
    // but it doesn't exist
    // Missing: public StatsTracker getStats() { return stats; }

    public void setCheatLowHealth(boolean value) {
        this.cheatLowHealth = value;
    }

    public void setCheatHighHealth(boolean value) {
        this.cheatHighHealth = value;
    }

    public void setMaxValue(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Max value must be at least 1");
        }
        board.setValueRange(0, max);
    }

}
