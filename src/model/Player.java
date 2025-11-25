package model;

import model.ring.NoRing;
import model.ring.Ring;
import model.weapon.NoWeapon;
import model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int health;
    private Weapon weapon = new NoWeapon();
    private Ring[] rings = new Ring[]{new NoRing(), new NoRing(), new NoRing()};

    public Player(int startingHealth) {
        this.health = startingHealth;
        this.weapon = new NoWeapon();
        this.rings = new Ring[]{new NoRing(), new NoRing(), new NoRing()};
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setRings(Ring ring1, Ring ring2, Ring ring3) {
        rings[0] = ring1;
        rings[1] = ring2;
        rings[2] = ring3;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public GameEvent.AttackData attack(Opponent[] opponents, Fill fill) {
        // Get last cell to determine target
        int[] lastCell = fill.getLastCell();
        if (lastCell == null) {
            // No cells in fill? Shouldn't happen, but handle gracefully
            return new GameEvent.AttackData(0, new ArrayList<>(), "", new ArrayList<>());
        }

        int targetIndex = determineTargetIndex(lastCell[0], lastCell[1]);
        int strength = fill.getStrength();
        int baseDamage = 100 + strength;  // basePlayerDamage + strength

        // Check ring activations
        double ringMultiplier = 1.0;
        List<String> activeRings = new ArrayList<>();
        for (Ring ring : rings) {
            if (ring.activates(fill)) {  // Pass Fill, not int!
                ringMultiplier *= ring.getDamageMultiplier();
                activeRings.add(ring.getName());
            }
        }

        // Check weapon activation
        Weapon.AttackResult weaponResult;
        if (weapon.activates(fill)) {
            weaponResult = weapon.applyEffect(fill, opponents, baseDamage, targetIndex);
        } else {
            weaponResult = new Weapon.AttackResult(new int[]{targetIndex}, new double[]{1.0});
        }

        // Apply damage to targets
        List<GameEvent.TargetDamage> targets = new ArrayList<>();
        for (int i = 0; i < weaponResult.targetIndices.length; i++) {
            int targetIdx = weaponResult.targetIndices[i];
            double multiplier = weaponResult.damageMultipliers[i] * ringMultiplier;
            int damage = (int) Math.round(baseDamage * multiplier);

            if (opponents[targetIdx].isAlive()) {
                opponents[targetIdx].takeDamage(damage);
                boolean killed = !opponents[targetIdx].isAlive();
                targets.add(new GameEvent.TargetDamage(targetIdx, damage, killed, false));
            } else {
                // Target already dead - missed
                targets.add(new GameEvent.TargetDamage(targetIdx, 0, false, true));
            }
        }

        // Return attack data
        return new GameEvent.AttackData(strength, activeRings, weaponResult.description, targets);
    }

    private int determineTargetIndex(int row, int col) {
        if (col == 0) return 0;  // Left
        if (col == 2) return 2;  // Right
        return 1;  // Middle
    }

    public Weapon getWeapon() { return weapon; }
    public Ring[] getRings() { return rings; }
    public int getHealth() { return health; }
}

