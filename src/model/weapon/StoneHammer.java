package model.weapon;

import model.Fill;
import model.Character;

public class StoneHammer implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.getCellCount() >= 10;
    }
    
    @Override
    public String getName() {
        return "Stone Hammer";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Character[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Hit all opponents at 80%
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].isAlive()) {
                targets.add(i);
                multipliers.add(0.8); // 80% damage
            }
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Stone Hammer hits all opponents.");
    }
}

