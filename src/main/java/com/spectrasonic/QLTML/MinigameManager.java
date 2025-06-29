package com.spectrasonic.QLTML;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

public class MinigameManager {

    private static boolean isMinigameActive = false;

    public static void startMinigame() {
        isMinigameActive = true;
        disableFallDamage();
    }

    public static void stopMinigame() {
        isMinigameActive = false;
        enableFallDamage();
    }

    public static boolean isMinigameActive() {
        return isMinigameActive;
    }

    private static void disableFallDamage() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.FALL_DAMAGE, false);
        }
    }

    private static void enableFallDamage() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.FALL_DAMAGE, true);
        }
    }
}