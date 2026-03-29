package com.narxoz.rpg.tournament;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.ArmorHandler;
import com.narxoz.rpg.chain.BlockHandler;
import com.narxoz.rpg.chain.DefenseHandler;
import com.narxoz.rpg.chain.DodgeHandler;
import com.narxoz.rpg.chain.HpHandler;
import com.narxoz.rpg.command.ActionQueue;
import com.narxoz.rpg.command.AttackCommand;
import com.narxoz.rpg.command.DefendCommand;
import com.narxoz.rpg.command.HealCommand;
import java.util.Random;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private Random random = new Random(1L);

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        int round = 0;
        final int maxRounds = 20;

        DefenseHandler dodge = new DodgeHandler(hero.getDodgeChance(), random.nextLong());
        DefenseHandler block = new BlockHandler(hero.getBlockRating() / 100.0);
        DefenseHandler armor = new ArmorHandler(hero.getArmorValue());
        DefenseHandler hp    = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        ActionQueue actionQueue = new ActionQueue();

        while (hero.isAlive() && opponent.isAlive() && round < maxRounds) {
            round++;
            System.out.println("Round " + round + " ───────────────────────────────");

            actionQueue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            actionQueue.enqueue(new HealCommand(hero, 20));
            actionQueue.enqueue(new DefendCommand(hero, 0.05));

            System.out.println("  │  Hero actions:");
            for (String desc : actionQueue.getCommandDescriptions()) {
                System.out.println("  │    - " + desc);
            }

            actionQueue.executeAll();

            if (opponent.isAlive()) {
                System.out.println("  │  Opponent attacks for " + opponent.getAttackPower() + " damage:");
                dodge.handle(opponent.getAttackPower(), hero);
            }

            String logLine = "Round " + round
                    + "  |  " + opponent.getName() + " HP: " + opponent.getHealth()
                    + "  |  " + hero.getName() + " HP: " + hero.getHealth();
            System.out.println(logLine);
            System.out.println();
            result.addLine(logLine);
        }

        result.setWinner(hero.isAlive() ? hero.getName() : opponent.getName());
        result.setRounds(round);
        return result;
    }
}
