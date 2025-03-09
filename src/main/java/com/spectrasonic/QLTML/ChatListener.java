package com.spectrasonic.QLTML;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Main plugin;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        // Permitir bypass para jugadores con el permiso "qltml.bypass"
        if (player.hasPermission("qltml.bypass")) return;
        if (!plugin.isGameActive()) return;
        
        String message = event.getMessage();
        if (message == null || message.isEmpty()) return;
        event.setCancelled(true);
        
        Bukkit.getScheduler().runTask(plugin, () -> {
            // Ubicación actual del bloque del jugador (última letra)
            Location base = player.getLocation().getBlock().getLocation();
            int length = message.length();

            // Construir el pilar: la última letra se sitúa en la posición original y las 
            // demás se colocan hacia arriba
            for (int i = 0; i < length; i++) {
                char letter = message.charAt(length - 1 - i);
                Location letterLoc = base.clone().add(0, i, 0);
                Material mat = plugin.getMaterialForLetter(letter);
                letterLoc.getBlock().setType(mat);
            }
            
            // Bloque extra (separador) en la parte superior del pilar
            Location topLoc = base.clone().add(0, length, 0);
            topLoc.getBlock().setType(Material.GREEN_STAINED_GLASS);
            
            // Teletransportar al jugador para que quede sobre el bloque separador 
            Location teleportLoc = topLoc.clone().add(0.5, 1, 0.5);
            player.teleport(teleportLoc);
        });
    }
}
