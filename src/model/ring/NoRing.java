package model.ring;

/**
 * Null Object pattern implementation for empty ring slot.
 */
public class NoRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return false;
    }
    
    @Override
    public double getDamageMultiplier() {
        return 1.0;
    }
    
    @Override
    public String getName() {
        return "None";
    }
    
    @Override
    public String getDescription() {
        return "No ring equipped";
    }
}

