package model;

import model.ring.NoRing;
import model.ring.Ring;
import model.weapon.NoWeapon;
import model.weapon.Weapon;

import java.util.List;

public class Player {
    private int health;
    private Weapon weapon = new NoWeapon();
    private Ring[] rings = new Ring[]{new NoRing(), new NoRing(), new NoRing()};
    private boolean cheatLowHealth;
    private boolean cheatHighHealth;
    private int basePlayerHealth;
    private int basePlayerDamage;

    public Player(int startingHealth) {
        this.health = startingHealth;
        this.weapon = new NoWeapon();
        this.rings = new Ring[]{new NoRing(), new NoRing(), new NoRing()};
    }

    public void takeDamage(int damage) {

    }

    public void attack(Opponent[] opponents, Fill fill) {

    }

    public void equipWeapon(Weapon w) {

    }

    public Weapon getWeapon() { return weapon; }
    public Ring[] getRings() { return rings; }
    public int getHealth() { return health; }
}

