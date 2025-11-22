package model.weapon;

import model.Fill;
import model.Character;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LightningWand implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.getElapsedTime() < 10000; // Less than 10 seconds
    }
    
    @Override
    public String getName() {
        return "Lightning Wand";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Character[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Normal attack
        targets.add(targetIndex);
        multipliers.add(1.0);
        
        // Additional random target
        Random random = new Random();
        List<Integer> aliveIndices = new ArrayList<>();
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].isAlive()) {
                aliveIndices.add(i);
            }
        }
        
        if (!aliveIndices.isEmpty()) {
            int randomTarget = aliveIndices.get(random.nextInt(aliveIndices.size()));
            targets.add(randomTarget);
            multipliers.add(1.0); // 100% damage
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Lightning Wand targets an additional random opponent.");
    }
}

