package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class HealCommand implements ActionCommand {
    private final ArenaFighter target;
    private final int healAmount;
    private int actualHealApplied;

    public HealCommand(ArenaFighter target, int healAmount) {
        this.target = target;
        this.healAmount = healAmount;
    }

    @Override
    public void execute() {
        if (target.getHealPotions() <= 0) {
            System.out.println("No potions left. Heal skipped.");
            return;
        }
        int missing = target.getMaxHealth() - target.getHealth();
        actualHealApplied = Math.min(healAmount, missing);
        target.heal(actualHealApplied);
        System.out.println("Restored " + actualHealApplied + " HP to " + target.getName()+ ". HP: " + target.getHealth());
    }

    @Override
    public void undo() {
        target.takeDamage(actualHealApplied);
        System.out.println("Removed " + actualHealApplied + " HP from " + target.getName());
    }

    @Override
    public String getDescription() {
        return "Heal for " + healAmount + " HP";
    }
}
