package model.ring;

public class BigOneRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return strength >= 160;
    }
    
    @Override
    public double getDamageMultiplier() {
        return 1.5; // 50% bonus
    }
    
    @Override
    public String getName() {
        return "The Big One";
    }
}

