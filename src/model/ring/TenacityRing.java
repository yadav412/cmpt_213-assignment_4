package model.ring;

public class TenacityRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return strength % 10 == 0;
    }
    
    @Override
    public double getDamageMultiplier() {
        return 1.5; // 50% bonus
    }
    
    @Override
    public String getName() {
        return "Ring of Ten-acity";
    }
}

