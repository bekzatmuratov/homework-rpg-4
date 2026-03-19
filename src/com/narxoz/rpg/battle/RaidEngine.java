package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private static final int MAX_ROUNDS = 20;
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        RaidResult result = new RaidResult();

        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setWinner("Invalid");
            result.setRounds(0);
            result.addLine("Raid cannot start: one or more required arguments are null.");
            return result;
        }

        if (!teamA.isAlive() || !teamB.isAlive()) {
            result.setWinner("Invalid");
            result.setRounds(0);
            result.addLine("Raid cannot start: one of the teams is already defeated.");
            return result;
        }

        int rounds = 0;
        result.addLine("Raid started: " + teamA.getName() + " vs " + teamB.getName());

        while (teamA.isAlive() && teamB.isAlive() && rounds < MAX_ROUNDS) {
            rounds++;
            result.addLine("");
            result.addLine("=== Round " + rounds + " ===");

            executeTurn(teamA, teamB, teamASkill, result);

            if (!teamB.isAlive()) {
                break;
            }

            executeTurn(teamB, teamA, teamBSkill, result);
        }

        result.setRounds(rounds);

        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner(teamA.getName());
            result.addLine("");
            result.addLine("Winner: " + teamA.getName());
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner(teamB.getName());
            result.addLine("");
            result.addLine("Winner: " + teamB.getName());
        } else {
            result.setWinner("Draw");
            result.addLine("");
            result.addLine("Battle ended in a draw (max rounds reached).");
        }

        return result;
    }

    private void executeTurn(CombatNode attacker, CombatNode defender, Skill skill, RaidResult result) {
        if (attacker == null || defender == null || skill == null) {
            return;
        }

        if (!attacker.isAlive()) {
            result.addLine(attacker.getName() + " cannot attack because the team is defeated.");
            return;
        }

        if (!defender.isAlive()) {
            result.addLine(defender.getName() + " is already defeated.");
            return;
        }

        int defenderHealthBefore = defender.getHealth();

        result.addLine(
                attacker.getName() + " uses " + skill.getSkillName() +
                        " [" + skill.getEffectName() + "] on " + defender.getName()
        );

        skill.cast(defender);

        boolean criticalStrike = random.nextInt(100) < 15;
        if (criticalStrike && defender.isAlive()) {
            int bonusDamage = Math.max(1, skill.getBasePower() / 2);
            defender.takeDamage(bonusDamage);
            result.addLine("Critical strike! Extra " + bonusDamage + " damage dealt to " + defender.getName());
        }

        int defenderHealthAfter = defender.getHealth();
        int dealtDamage = Math.max(0, defenderHealthBefore - defenderHealthAfter);

        result.addLine(
                defender.getName() + " HP: " + defenderHealthBefore + " -> " + defenderHealthAfter +
                        " (damage dealt: " + dealtDamage + ")"
        );

        if (!defender.isAlive()) {
            result.addLine(defender.getName() + " has been defeated.");
        }
    }
}