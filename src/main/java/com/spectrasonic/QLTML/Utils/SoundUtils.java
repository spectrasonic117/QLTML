package com.spectrasonic.QLTML.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public final class SoundUtils {

    private SoundUtils() {
        // Private constructor to prevent instantiation
    }

    public static void playerSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player, sound, SoundCategory.MASTER, volume, pitch);
    }

    public static void broadcastPlayerSound(Sound sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> 
            player.playSound(player, sound, SoundCategory.MASTER, volume, pitch)
        );
    }
}
