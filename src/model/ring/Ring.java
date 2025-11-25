package model.ring;

/**
 * Base interface for rings.
 * Uses Null Object pattern - NoRing implements this.
 */
public interface Ring {

    boolean activates(int strength);

    double getDamageMultiplier();

    String getName();

    String getDescription();
}
