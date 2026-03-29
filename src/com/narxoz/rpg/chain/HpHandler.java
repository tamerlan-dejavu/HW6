package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class HpHandler extends DefenseHandler {

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        target.takeDamage(incomingDamage);
        System.out.println(target.getName() + " takes " + incomingDamage + " damage. HP: " + target.getHealth());
    }
}
