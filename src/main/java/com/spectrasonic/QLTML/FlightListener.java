package com.spectrasonic.QLTML;

import org.bukkit.GameMode;
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
        // Los operadores (OPs) y los jugadores en modo creativo no deben ser afectados por la restricción de vuelo del plugin.
        if (player.isOp() || player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
            event.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    private void checkAndSetFlight(Player player) {
        // Si el jugador es un operador (OP) o está en modo creativo, siempre se le debe permitir volar.
        if (player.isOp() || player.getGameMode() == GameMode.CREATIVE) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
            // No es necesario forzar player.setFlying(true) aquí, ya que el jugador puede elegir volar o no.
            return; // Los OPs y jugadores en creativo no están sujetos a la lógica de la pluma.
        }

        // Lógica para jugadores no-OPs y no en creativo: el vuelo depende de tener una pluma en la mano principal.
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
