package model.ring;

public class LittleOneRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return strength <= 90;
    }
    
    @Override
    public double getDamageMultiplier() {
        return 1.5; // 50% bonus
    }
    
    @Override
    public String getName() {
        return "The Little One";
    }
    
    @Override
    public String getDescription() {
        return "50% damage bonus if strength <= 90";
    }
}

