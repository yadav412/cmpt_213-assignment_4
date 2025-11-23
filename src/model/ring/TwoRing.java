package model.ring;

public class TwoRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return isPowerOfTwo(strength);
    }
    
    @Override
    public double getDamageMultiplier() {
        return 11.0; // 1000% bonus (11x = 1000% increase)
    }
    
    @Override
    public String getName() {
        return "The Two Ring";
    }
    
    private boolean isPowerOfTwo(int n) {
        if (n < 1) return false;
        return (n & (n - 1)) == 0;
    }
}

