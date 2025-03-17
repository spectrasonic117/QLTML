package com.spectrasonic.QLTML.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TeamSelectorGUI {
    private static final String GUI_TITLE = "Selección de Equipo";
    private static final int[] WOOL_SLOTS = {11, 12, 14, 15, 20, 21, 23, 24, 29, 30, 32, 33, 38, 39, 41, 42};
    private static final Map<Integer, String> SLOT_TEAMS = new HashMap<>();

    static {
        // Map slots to team names
        String[] teamNames = {
            "white", "orange", "magenta", "light_blue", 
            "yellow", "lime", "pink", "gray", 
            "light_gray", "cyan", "purple", "blue", 
            "brown", "green", "red", "black"
        };
        
        for (int i = 0; i < WOOL_SLOTS.length; i++) {
            SLOT_TEAMS.put(WOOL_SLOTS[i], teamNames[i]);
        }
    }

    public static Inventory createTeamSelectorGUI() {
        Inventory inventory = Bukkit.createInventory(null, 54, GUI_TITLE);
        
        // Fill with glass panes
        ItemStack filler = ItemBuilder.setMaterial("black_stained_glass_pane")
                .setName(" ")
                .build();
        
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, filler);
        }
        
        // Add wool blocks for team selection
        for (int slot : WOOL_SLOTS) {
            String teamName = SLOT_TEAMS.get(slot);
            Material woolMaterial = TeamManager.getTeamWool(teamName);
            
            // Convert team name for display (e.g., "light_blue" -> "Light Blue")
            String displayName = formatTeamName(teamName);
            
            // Get the appropriate color code for the team
            String colorCode = getColorCodeForTeam(teamName);
            
            ItemStack teamWool = ItemBuilder.setMaterial(woolMaterial.toString())
                    .setName(colorCode + "Seleccionar color " + displayName)
                    .setLore(" ", "<white>Haz clic para unirte a este equipo")
                    .build();
            
            inventory.setItem(slot, teamWool);
        }
        
        return inventory;
    }
    
    private static String formatTeamName(String teamName) {
        String[] parts = teamName.split("_");
        StringBuilder formatted = new StringBuilder();
        
        for (String part : parts) {
            formatted.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1))
                    .append(" ");
        }
        
        return formatted.toString().trim();
    }
    
    private static String getColorCodeForTeam(String teamName) {
        switch (teamName) {
            case "white": return "<white>";
            case "orange": return "<gold>";
            case "magenta": return "<light_purple>";
            case "light_blue": return "<aqua>";
            case "yellow": return "<yellow>";
            case "lime": return "<green>";
            case "pink": return "<light_purple>";
            case "gray": return "<dark_gray>";
            case "light_gray": return "<gray>";
            case "cyan": return "<dark_aqua>";
            case "purple": return "<dark_purple>";
            case "blue": return "<blue>";
            case "brown": return "<dark_red>";
            case "green": return "<dark_green>";
            case "red": return "<red>";
            case "black": return "<black>";
            default: return "<white>";
        }
    }
    
    public static void openTeamSelector(Player player) {
        player.openInventory(createTeamSelectorGUI());
    }
    
    public static boolean isTeamSelectorGUI(Inventory inventory) {
        return inventory != null && inventory.getSize() == 54 && GUI_TITLE.equals(inventory.getViewers().size() > 0 ? inventory.getViewers().get(0).getOpenInventory().getTitle() : "");
    }
    
    public static String getTeamFromSlot(int slot) {
        return SLOT_TEAMS.get(slot);
    }
}