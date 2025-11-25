package model.ring;

public class MehRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return strength % 5 == 0;
    }
    
    @Override
    public double getDamageMultiplier() {
        return 1.1; // 10% bonus
    }
    
    @Override
    public String getName() {
        return "Ring of Meh";
    }
    
    @Override
    public String getDescription() {
        return "10% damage bonus if strength is a multiple of 5";
    }
}

