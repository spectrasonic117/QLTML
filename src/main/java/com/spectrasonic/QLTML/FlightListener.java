package com.spectrasonic.QLTML;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlightListener implements Listener {

    private final Main plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        checkAndSetFlight(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying() && !player.getAllowFlight()) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        checkAndSetFlight(player);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
            event.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    private void checkAndSetFlight(Player player) {
        if (player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
            player.setAllowFlight(true);
        } else {
            player.setAllowFlight(false);
            if (player.isFlying()) {
                player.setFlying(false);
            }
        }
    }
}