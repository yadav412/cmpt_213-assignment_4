package model.ring;

public class PrimeDirectiveRing implements Ring {
    @Override
    public boolean activates(int strength) {
        return isPrime(strength);
    }
    
    @Override
    public double getDamageMultiplier() {
        return 2.0; // 100% bonus
    }
    
    @Override
    public String getName() {
        return "The Prime Directive";
    }
    
    private boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}

