package com.narxoz.rpg;

import com.narxoz.rpg.battle.RaidEngine;
import com.narxoz.rpg.battle.RaidResult;
import com.narxoz.rpg.bridge.AreaSkill;
import com.narxoz.rpg.bridge.FireEffect;
import com.narxoz.rpg.bridge.IceEffect;
import com.narxoz.rpg.bridge.PhysicalEffect;
import com.narxoz.rpg.bridge.ShadowEffect;
import com.narxoz.rpg.bridge.SingleTargetSkill;
import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.EnemyUnit;
import com.narxoz.rpg.composite.HeroUnit;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Homework 4 Demo: Bridge + Composite ===\n");

        HeroUnit warrior = new HeroUnit("Arthas", 140, 30);
        HeroUnit mage = new HeroUnit("Jaina", 90, 40);
        HeroUnit priest = new HeroUnit("Anduin", 100, 20);

        EnemyUnit goblin = new EnemyUnit("Goblin", 70, 20);
        EnemyUnit orc = new EnemyUnit("Orc", 120, 25);
        EnemyUnit necromancer = new EnemyUnit("Necromancer", 110, 35);
        EnemyUnit skeleton = new EnemyUnit("Skeleton", 60, 15);

        PartyComposite heroes = new PartyComposite("Heroes");
        heroes.add(warrior);
        heroes.add(mage);
        heroes.add(priest);

        PartyComposite frontline = new PartyComposite("Frontline");
        frontline.add(goblin);
        frontline.add(orc);

        PartyComposite backline = new PartyComposite("Backline");
        backline.add(necromancer);
        backline.add(skeleton);

        RaidGroup enemies = new RaidGroup("Enemy Raid");
        enemies.add(frontline);
        enemies.add(backline);

        System.out.println("--- Team Structures ---");
        heroes.printTree("");
        enemies.printTree("");

        Skill slashFire = new SingleTargetSkill("Slash", 20, new FireEffect());
        Skill slashIce = new SingleTargetSkill("Slash", 20, new IceEffect());

        Skill stormFire = new AreaSkill("Storm", 15, new FireEffect());
        Skill strikeFire = new SingleTargetSkill("Strike", 25, new FireEffect());

        Skill shadowNova = new AreaSkill("Shadow Nova", 18, new ShadowEffect());
        Skill shieldBreak = new SingleTargetSkill("Shield Break", 22, new PhysicalEffect());

        System.out.println("\n--- Bridge Preview ---");
        System.out.println("Same skill, different effects:");
        System.out.println("- " + slashFire.getSkillName() + " with " + slashFire.getEffectName());
        System.out.println("- " + slashIce.getSkillName() + " with " + slashIce.getEffectName());

        System.out.println("\nSame effect, different skills:");
        System.out.println("- " + stormFire.getSkillName() + " with " + stormFire.getEffectName());
        System.out.println("- " + strikeFire.getSkillName() + " with " + strikeFire.getEffectName());

        System.out.println("\nAdditional skills:");
        System.out.println("- " + shadowNova.getSkillName() + " with " + shadowNova.getEffectName());
        System.out.println("- " + shieldBreak.getSkillName() + " with " + shieldBreak.getEffectName());

        System.out.println("\n--- Raid Simulation ---");
        RaidEngine engine = new RaidEngine().setRandomSeed(42L);
        RaidResult result = engine.runRaid(heroes, enemies, strikeFire, shadowNova);

        System.out.println("\n--- Raid Result ---");
        System.out.println("Winner: " + result.getWinner());
        System.out.println("Rounds: " + result.getRounds());
        System.out.println("Battle log:");
        for (String line : result.getLog()) {
            System.out.println(line);
        }

        System.out.println("\n--- Final Team States ---");
        heroes.printTree("");
        enemies.printTree("");

        System.out.println("\n=== Demo Complete ===");
    }
}