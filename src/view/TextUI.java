package view;

import model.GameEngine;

public class TextUI {
    private GameEngine gameEngine;
    private Scanner scanner;

    public TestUI() {
        this.scanner = new Scanner(System.in);
        this.gameEngine = new GameEngine(new Player());
        gameEngine.registerObserver(this);  // Register UI as observer
    }

    private String getUserInput() {
        System.out.print("Enter a sum: ");
        return scanner.nextLine();
    }

    @Override
    public void onEvent(GameEvent event) {
        // Handle different event types
        if (event instanceof AttackEvent) {
            displayAttack((AttackEvent) event);
        } else if (event instanceof FailedMoveEvent) {
            System.out.println("Invalid move!");
        } else if (event instanceof OpponentDefeatedEvent) {
            displayOpponentDefeated((OpponentDefeatedEvent) event);
        }
        // ... handle other events
    }

    // Main game loop - get commands and process them
    public void run() {
        // Display welcome message?

        while (gameIsRunning) {
            // Display current game state (board, health, fill strength)
            displayGameState();

            // Get user input
            String input = getUserInput();

            // Process the command
            processCommand(input);

            // Check win/loss conditions
        }

        // Game over, display final stats
    }

    private void processCommand(String input) {
        input = input.trim();

        if (input.equals("gear")) {
            displayGear();
        } else if (input.equals("stats")) {
            displayStats();
        } else if (input.startsWith("cheat ")) {
            processCheat(input);
        } else if (input.equals("new")) {
            startNewMatch();
        } else {
            // Try to parse as integer (sum)
            try {
                int sum = Integer.parseInt(input);
                processMove(sum);
            } catch (NumberFormatException e) {
                System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private void displayAttack(AttackEvent event) {
        System.out.println("Fill complete! Strength is " + event.getStrength() + ".");

        // Display ring activations
        for (RingActivation ring : event.getRingActivations()) {
            System.out.println(ring.getRingName() + " adds " +
                    ring.getBonusPercent() + "% bonus damage.");
        }

        // Display damage dealt
        for (DamageResult damage : event.getDamageResults()) {
            System.out.println("Hit " + damage.getTargetPosition() +
                    " character for " + damage.getDamage() + " damage.");
            if (damage.wasKilled()) {
                System.out.println("Kills " + damage.getTargetPosition() + " character!");
            }
        }
    }

    private void displayGear() {
        Player player = gameEngine.getPlayer();

        System.out.println("Current Equipment:");
        System.out.println("Weapon: " + player.getWeapon().getName());
        System.out.println("  Ability: " + player.getWeapon().getDescription());

        System.out.println("Rings:");
        Ring[] rings = player.getRings();
        for (int i = 0; i < rings.length; i++) {
            System.out.println("  " + (i+1) + ". " + rings[i].getName());
            System.out.println("     " + rings[i].getDescription());
        }
    }

}
