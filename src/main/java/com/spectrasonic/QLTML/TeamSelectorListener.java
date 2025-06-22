package com.spectrasonic.QLTML;

import com.spectrasonic.QLTML.Utils.SoundUtils;
import com.spectrasonic.QLTML.Utils.TeamManager;
import com.spectrasonic.QLTML.Utils.TeamSelectorGUI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamSelectorListener implements Listener {
    
    private final Main plugin;
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        
        if (TeamSelectorGUI.isTeamSelectorGUI(inventory)) {
            event.setCancelled(true);
            
            int slot = event.getRawSlot();
            String teamName = TeamSelectorGUI.getTeamFromSlot(slot);
            
            if (teamName != null) {
                // Set player's team
                TeamManager.setPlayerTeam(player, teamName);
                
                // Send confirmation message
                String formattedTeamName = teamName.replace("_", " ");
                formattedTeamName = formattedTeamName.substring(0, 1).toUpperCase() + formattedTeamName.substring(1);
                
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Te has unido al equipo <" + 
                        getMinimessageColor(teamName) + ">" + formattedTeamName + "</green>!"));
                
                // Play sound
                SoundUtils.playerSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                
                // Close inventory
                player.closeInventory();
            }
        }
    }
    
    private String getMinimessageColor(String teamName) {
        switch (teamName) {
            case "white": return "white";
            case "orange": return "gold";
            case "magenta": return "light_purple";
            case "light_blue": return "aqua";
            case "yellow": return "yellow";
            case "lime": return "green";
            case "pink": return "light_purple";
            case "gray": return "dark_gray";
            case "light_gray": return "gray";
            case "cyan": return "dark_aqua";
            case "purple": return "dark_purple";
            case "blue": return "blue";
            case "brown": return "dark_red";
            case "green": return "dark_green";
            case "red": return "red";
            case "black": return "black";
            default: return "white";
        }
    }
}