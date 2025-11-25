package view;

import model.*;
import model.ring.Ring;
import model.weapon.Weapon;

import java.util.Map;
import java.util.Scanner;

/**
 * Text-based user interface for the game.
 * Handles all input/output and implements the Observer pattern to display game
 * events.
 */
public class TextUI implements GameObserver {
    private final GameEngine gameEngine;
    private final Scanner scanner;
    private boolean gameRunning;

    public TextUI() {
        this.scanner = new Scanner(System.in);
        this.gameEngine = new GameEngine();
        this.gameRunning = true;

        // Register as observer to receive game events
        gameEngine.registerObserver(this);
    }

    // Main game loop. This displays state, gets input, processes commands
    public void run() {
        displayWelcome();

        while (gameRunning) {
            displayGameState();
            String input = getUserInput();
            processCommand(input);
        }

        displayFarewell();
        scanner.close();
    }

    // observer pattern. this handles events from the game model
    @Override
    public void notify(GameEvent event) {
        switch (event.getType()) {
            case ATTACK_PERFORMED:
                GameEvent.AttackData attackData = (GameEvent.AttackData) event.getData();
                displayAttack(attackData);
                break;

            case CELL_ADDED_TO_FILL:
                break; // Silently update, the board will show on next turn

            case FILL_COMPLETED:
                break; // Attack display handles this

            case MATCH_WON:
                System.out.println("\nVictory, you have defeated all opponents!");
                offerNewEquipment();
                askToContinue();
                break;

            case MATCH_LOST:
                System.out.println("\nDEFEAT, your health is zero.");
                askToContinue();
                break;

            case TURN_FAILED:
                System.out.println("Invalid sum. No matching cells with this value.");
                break;

            case OPPONENT_ATTACKED:
                GameEvent.OpponentAttackData oppData = (GameEvent.OpponentAttackData) event.getData();
                System.out.println("Opponent " + (oppData.attackerIndex + 1) +
                        " attacks for " + oppData.damage + " damage!");
                break;

            case FILL_STARTED:
                break; // silently starting a new fill

            case CHARACTER_DAMAGED:
            case CHARACTER_KILLED:
            case EQUIPMENT_ACTIVATED:
                break; // tracked by StatsTracker, not displayed directly
        }
    }

    // command processing

    private void processCommand(String input) {
        input = input.trim();

        if (input.isEmpty()) {
            return;
        }

        if (input.equals("gear")) {
            displayGear();
        } else if (input.equals("stats")) {
            displayStats();
        } else if (input.startsWith("cheat ")) {
            processCheat(input);
        } else if (input.equals("new")) {
            gameEngine.startNewMatch();
            gameRunning = true;
            System.out.println("Starting new match!\n");
        } else {
            // Try to parse as sum
            try {
                int sum = Integer.parseInt(input);
                gameEngine.processSum(sum);
            } catch (NumberFormatException e) {
                System.out.println("Invalid command. Type a number, 'gear', 'stats', 'cheat', or 'new'.");
            }
        }
    }

    private void processCheat(String input) {
        String[] parts = input.split(" ");

        if (parts.length < 2) {
            System.out.println(
                    "Invalid cheat. Available: lowhealth, highhealth, weapon <number>, rings <number> <number> <number>, max <number>");
            return;
        }

        String cheatType = parts[1].toLowerCase();

        try {
            switch (cheatType) {
                case "lowhealth":
                    gameEngine.setCheatLowHealth(true);
                    System.out.println("Cheat activated: Opponents have low health");
                    break;

                case "highhealth":
                    gameEngine.setCheatHighHealth(true);
                    System.out.println("Cheat activated: Opponents have high health");
                    break;

                case "weapon":
                    if (parts.length < 3) {
                        System.out.println("Usage: cheat weapon <number>");
                        return;
                    }
                    int weaponNum = Integer.parseInt(parts[2]);
                    gameEngine.equipPlayerWeapon(weaponNum);
                    System.out.println(" Equipped weapon " + weaponNum);
                    break;

                case "rings":
                    if (parts.length < 5) {
                        System.out.println("Usage: cheat rings <number> <number> <number>");
                        return;
                    }
                    int ring1 = Integer.parseInt(parts[2]);
                    int ring2 = Integer.parseInt(parts[3]);
                    int ring3 = Integer.parseInt(parts[4]);
                    gameEngine.equipPlayerRings(ring1, ring2, ring3);
                    System.out.println("Equipped rings: " + ring1 + ", " + ring2 + ", " + ring3);
                    break;

                case "max":
                    if (parts.length < 3) {
                        System.out.println("Usage: cheat max <number>");
                        return;
                    }
                    int max = Integer.parseInt(parts[2]);
                    gameEngine.setMaxValue(max);
                    System.out.println("Max cell value set to " + max);
                    break;

                default:
                    System.out.println("Unknown cheat: " + cheatType);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // display methods

    private void displayGameState() {
        Opponent[] opponents = gameEngine.getOpponents();
        GameBoard board = gameEngine.getBoard();
        Player player = gameEngine.getPlayer();
        Fill fill = gameEngine.getCurrentFill();

        System.out.println("\n" + "=".repeat(50));

        // display opponent health
        System.out.printf("[%d]\t\t[%d]\t\t[%d]%n",
                opponents[0].getHealth(),
                opponents[1].getHealth(),
                opponents[2].getHealth());

        // display 3 x 3 board
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Cell cell = board.getCell(row, col);
                int value = cell.getValue();

                // Format: underline if in fill, tab-separate
                if (cell.isInFill()) {
                    System.out.print("_" + value + "_\t\t");
                } else {
                    System.out.print(value + "\t\t");
                }
            }
            System.out.println();
        }

        // display player health and fill strength
        System.out.printf("\t\t[%d]\t\tFill: %d%n", player.getHealth(), fill.getStrength());
        System.out.println("=".repeat(50));
    }

    private void displayAttack(GameEvent.AttackData data) {
        System.out.println("\nFill complete! Strength is " + data.strength + ".");

        // display ring activations with bonus percentages
        for (String ringName : data.activeRings) {
            int bonusPercent = getRingBonusPercent(ringName);
            System.out.println(ringName + " adds " + bonusPercent + "% bonus damage.");
        }

        // display damage to each target
        // Primary target is first, then weapon targets
        int primaryTargetIndex = data.targets.isEmpty() ? -1 : data.targets.get(0).targetIndex;

        for (GameEvent.TargetDamage target : data.targets) {
            String position = getPositionName(target.targetIndex);

            // Show weapon targeting for additional targets (not the primary target)
            if (data.weaponActivated && target.targetIndex != primaryTargetIndex) {
                System.out.println(data.weaponName + " targets " + position + " character.");
            }

            if (target.missed) {
                System.out.println("Missed " + position + " character.");
            } else {
                System.out.println("Hit " + position + " character for " +
                        target.damage + " damage.");
                if (target.killed) {
                    System.out.println("Kills " + position + " character!");
                }
            }
        }
        System.out.println();
    }

    private int getRingBonusPercent(String ringName) {
        // Look up the ring from player's equipment to get its multiplier
        Ring[] rings = gameEngine.getPlayer().getRings();
        for (Ring ring : rings) {
            if (ring.getName().equals(ringName)) {
                // Convert multiplier to percentage: (multiplier - 1.0) * 100
                return (int) Math.round((ring.getDamageMultiplier() - 1.0) * 100);
            }
        }
        return 0; // Default if ring not found
    }

    private void displayGear() {
        Player player = gameEngine.getPlayer();
        Weapon weapon = player.getWeapon();
        Ring[] rings = player.getRings();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Current equipment");
        System.out.println("=".repeat(50));

        System.out.println("Weapon: " + weapon.getName());
        System.out.println(weapon.getDescription());

        System.out.println("\nRings:");
        for (int i = 0; i < rings.length; i++) {
            System.out.println("  " + (i + 1) + ". " + rings[i].getName());
            System.out.println(rings[i].getDescription());
        }
        System.out.println("=".repeat(50) + "\n");
    }

    private void displayStats() {
        StatsTracker stats = gameEngine.getStats();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("Statistics");
        System.out.println("=".repeat(50));

        // Equipment activations
        System.out.println("Equipment Activations:");
        Map<String, Integer> weapons = stats.getWeaponActivations();
        Map<String, Integer> rings = stats.getRingActivations();

        if (weapons.isEmpty() && rings.isEmpty()) {
            System.out.println("  (none yet)");
        } else {
            for (Map.Entry<String, Integer> entry : weapons.entrySet()) {
                System.out.printf("  %-25s %d%n", entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Integer> entry : rings.entrySet()) {
                System.out.printf("  %-25s %d%n", entry.getKey(), entry.getValue());
            }
        }

        // Match statistics
        System.out.println("\nMatches:");
        System.out.printf("Won:  %d%n", stats.getMatchesWon());
        System.out.printf("lost: %d%n", stats.getMatchesLost());

        // Damage statistics
        System.out.println("\nTotal damage:");
        System.out.printf("Done:     %,d%n", stats.getTotalDamageDone());
        System.out.printf("Received: %,d%n", stats.getTotalDamageReceived());

        System.out.printf("%nFills Completed: %d%n", stats.getFillsCompleted());
        System.out.println("=".repeat(50) + "\n");
    }

    // User Interaction
    private void offerNewEquipment() {
        // randomly choose weapon or ring
        boolean isWeapon = Math.random() < 0.5;

        if (isWeapon) {
            offerWeapon();
        } else {
            offerRing();
        }
    }

    private void offerWeapon() {
        Weapon newWeapon = gameEngine.getRandomWeapon();

        System.out.println("\nYou received: " + newWeapon.getName());
        System.out.println(newWeapon.getDescription());
        System.out.print("\nEquip this weapon: ");

        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            gameEngine.getPlayer().setWeapon(newWeapon);
            System.out.println("Weapon equipped!");
        } else {
            System.out.println("Weapon discarded.");
        }
    }

    private void offerRing() {
        Ring newRing = gameEngine.getRandomRing();
        Ring[] currentRings = gameEngine.getPlayer().getRings();

        System.out.println("\nYou received: " + newRing.getName());
        System.out.println(newRing.getDescription());
        System.out.print("\nEquip this ring: ");

        String response = scanner.nextLine().trim().toLowerCase();

        if (!response.equals("yes") && !response.equals("y")) {
            System.out.println("Ring discarded.");
            return;
        }

        // Check for empty slot
        int emptySlot = findEmptyRingSlot(currentRings);

        if (emptySlot >= 0) {
            // Equip in empty slot
            Ring[] newRings = currentRings.clone();
            newRings[emptySlot] = newRing;
            gameEngine.getPlayer().setRings(newRings[0], newRings[1], newRings[2]);
            System.out.println("Ring equipped in slot " + (emptySlot + 1) + "!");
        } else {
            // need to replace a ring
            replaceRing(newRing, currentRings);
        }
    }

    private int findEmptyRingSlot(Ring[] rings) {
        for (int i = 0; i < rings.length; i++) {
            if (rings[i].getName().equals("None") || rings[i].getName().equals("No Ring")) {
                return i;
            }
        }
        return -1;
    }

    private void replaceRing(Ring newRing, Ring[] currentRings) {
        System.out.println("\nAll ring slots full. Current rings:");
        for (int i = 0; i < currentRings.length; i++) {
            System.out.println("  " + (i + 1) + ". " + currentRings[i].getName());
        }

        System.out.print("Which ring to replace (1-3, or 0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());

            if (choice == 0) {
                System.out.println("Ring discarded.");
                return;
            }

            if (choice >= 1 && choice <= 3) {
                Ring[] newRings = currentRings.clone();
                newRings[choice - 1] = newRing;
                gameEngine.getPlayer().setRings(newRings[0], newRings[1], newRings[2]);
                System.out.println("Ring replaced in slot " + choice + "!");
            } else {
                System.out.println("Invalid slot. Ring discarded.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Ring discarded.");
        }
    }

    private void askToContinue() {
        System.out.print("\nPlay another match (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes") || response.equals("y")) {
            gameEngine.startNewMatch();
            gameRunning = true;
            System.out.println("\nStarting new match!\n");
        } else {
            gameRunning = false;
        }
    }

    // helper methods
    private String getUserInput() {
        System.out.print("\nEnter command: ");
        return scanner.nextLine();
    }

    private String getPositionName(int index) {
        return switch (index) {
            case 0 -> "left";
            case 1 -> "middle";
            case 2 -> "right";
            default -> "unknown";
        };
    }

    private void displayWelcome() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Welcome to Yadav & Orla's Awesome Sum Game");
        System.out.println("=".repeat(50));
        System.out.println("Commands:");
        System.out.println("[number]  - Enter sum to make a move");
        System.out.println("gear      - View your equipment");
        System.out.println("stats     - View game statistics");
        System.out.println("new       - Start a new match");
        System.out.println("cheat ... - Use cheat commands");
        System.out.println("=".repeat(50) + "\n");
    }

    private void displayFarewell() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("end game statistics:");
        System.out.println("=".repeat(50));
        displayStats();
    }

    public static void main(String[] args) {
        TextUI game = new TextUI();
        game.run();
    }
}