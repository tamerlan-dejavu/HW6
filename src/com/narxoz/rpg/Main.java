package com.narxoz.rpg;

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
import com.narxoz.rpg.tournament.TournamentEngine;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 6 Demo: Chain of Responsibility + Command ===\n");
        ArenaFighter hero    = new ArenaFighter("Hero", 100, 0.20, 25, 5, 18, 3);
        ArenaOpponent opponent = new ArenaOpponent("Champion", 90, 14);

        System.out.println(" ");
        System.out.println("  PART 1 - Command Queue Demo");
        System.out.println( " ");
        System.out.println("Hero: " + hero.getName() + "  |  HP: " + hero.getHealth() + "  |  Dodge: " + hero.getDodgeChance());
        System.out.println(" ");

        ActionQueue queue = new ActionQueue();

        queue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
        queue.enqueue(new HealCommand(hero, 20));
        queue.enqueue(new DefendCommand(hero, 0.15));

        System.out.println("Queued actions (" + queue.getCommandDescriptions().size() + " total):");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println(desc);
        }
        System.out.println(" ");

        System.out.println("undoLast() called - last command removed without executing");
        queue.undoLast();
        System.out.println(" ");

        System.out.println("Queue after undo (" + queue.getCommandDescriptions().size() + " total):");
        for (String desc : queue.getCommandDescriptions()) {
            System.out.println(desc);
        }
        System.out.println(" ");

        queue.enqueue(new DefendCommand(hero, 0.15));
        System.out.println("executeAll() - running all commands:");
        queue.executeAll();
        System.out.println("Queue size after executeAll(): " + queue.getCommandDescriptions().size());

        System.out.println(" ");
        System.out.println("  PART 2 - Defense Chain Demo");
        System.out.println( " ");

        
        DefenseHandler dodge = new DodgeHandler(0.50, 99L);
        DefenseHandler block = new BlockHandler(0.30);
        DefenseHandler armor = new ArmorHandler(5);
        DefenseHandler hp    = new HpHandler();
        dodge.setNext(block).setNext(armor).setNext(hp);

        System.out.println("Chain: Dodge(50%) -> Block(30%) -> Armor(5) -> HP");
        System.out.println("Incoming damage: 20");
        System.out.println("Hero HP before: " + hero.getHealth());
        System.out.println();
        dodge.handle(20, hero);
        System.out.println();
        System.out.println("Hero HP after:  " + hero.getHealth());

        System.out.println(" ");
        System.out.println("  PART 3 - Full Arena Tournament");
        System.out.println(" ");

        ArenaFighter tournamentHero = new ArenaFighter("Tamerlan",     120, 0.01, 10, 8, 13, 0);
        ArenaOpponent tournamentOpponent = new ArenaOpponent("Iron Vane", 100, 25);

        System.out.println(tournamentHero.getName() + " vs " + tournamentOpponent.getName() + "\n");

        TournamentResult result = new TournamentEngine(tournamentHero, tournamentOpponent).setRandomSeed(42L).runTournament();

        System.out.println(" ");
        System.out.println("RESULT");
        System.out.println(" ");
        System.out.println("Winner : " + result.getWinner());
        System.out.println("Rounds : " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println(line);
        }

        System.out.println(" ");
        System.out.println("\n=== Demo Complete ===");
    }
}
