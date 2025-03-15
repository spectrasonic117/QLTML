package com.spectrasonic.QLTML.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;

public class TeamManager {
    private static final Map<String, ChatColor> TEAM_COLORS = new HashMap<>();
    private static final Map<String, Material> TEAM_WOOL = new HashMap<>();
    private static Scoreboard scoreboard;

    static {
        // Initialize team colors
        TEAM_COLORS.put("white", ChatColor.WHITE);
        TEAM_COLORS.put("orange", ChatColor.GOLD);
        TEAM_COLORS.put("magenta", ChatColor.LIGHT_PURPLE);
        TEAM_COLORS.put("light_blue", ChatColor.AQUA);
        TEAM_COLORS.put("yellow", ChatColor.YELLOW);
        TEAM_COLORS.put("lime", ChatColor.GREEN);
        TEAM_COLORS.put("pink", ChatColor.LIGHT_PURPLE);
        TEAM_COLORS.put("gray", ChatColor.DARK_GRAY);
        TEAM_COLORS.put("light_gray", ChatColor.GRAY);
        TEAM_COLORS.put("cyan", ChatColor.DARK_AQUA);
        TEAM_COLORS.put("purple", ChatColor.DARK_PURPLE);
        TEAM_COLORS.put("blue", ChatColor.BLUE);
        TEAM_COLORS.put("brown", ChatColor.DARK_RED);
        TEAM_COLORS.put("green", ChatColor.DARK_GREEN);
        TEAM_COLORS.put("red", ChatColor.RED);
        TEAM_COLORS.put("black", ChatColor.BLACK);

        // Initialize team wool blocks
        TEAM_WOOL.put("white", Material.WHITE_WOOL);
        TEAM_WOOL.put("orange", Material.ORANGE_WOOL);
        TEAM_WOOL.put("magenta", Material.MAGENTA_WOOL);
        TEAM_WOOL.put("light_blue", Material.LIGHT_BLUE_WOOL);
        TEAM_WOOL.put("yellow", Material.YELLOW_WOOL);
        TEAM_WOOL.put("lime", Material.LIME_WOOL);
        TEAM_WOOL.put("pink", Material.PINK_WOOL);
        TEAM_WOOL.put("gray", Material.GRAY_WOOL);
        TEAM_WOOL.put("light_gray", Material.LIGHT_GRAY_WOOL);
        TEAM_WOOL.put("cyan", Material.CYAN_WOOL);
        TEAM_WOOL.put("purple", Material.PURPLE_WOOL);
        TEAM_WOOL.put("blue", Material.BLUE_WOOL);
        TEAM_WOOL.put("brown", Material.BROWN_WOOL);
        TEAM_WOOL.put("green", Material.GREEN_WOOL);
        TEAM_WOOL.put("red", Material.RED_WOOL);
        TEAM_WOOL.put("black", Material.BLACK_WOOL);
    }

    public static void initialize() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getMainScoreboard();
        
        // Create teams if they don't exist
        for (String teamName : TEAM_COLORS.keySet()) {
            Team team = scoreboard.getTeam("qltml_" + teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam("qltml_" + teamName);
                team.setColor(TEAM_COLORS.get(teamName));
                team.setAllowFriendlyFire(true);
                team.setCanSeeFriendlyInvisibles(true);
            }
        }
    }

    public static void setPlayerTeam(Player player, String teamName) {
        // Remove player from current team if any
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
        
        // Add player to new team
        Team newTeam = scoreboard.getTeam("qltml_" + teamName);
        if (newTeam != null) {
            newTeam.addEntry(player.getName());
        }
    }

    public static String getPlayerTeam(Player player) {
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName()) && team.getName().startsWith("qltml_")) {
                return team.getName().substring(6); // Remove "qltml_" prefix
            }
        }
        return null;
    }

    public static Material getTeamWool(String teamName) {
        return TEAM_WOOL.getOrDefault(teamName, Material.WHITE_WOOL);
    }

    public static Material getPlayerTeamWool(Player player) {
        String teamName = getPlayerTeam(player);
        if (teamName != null) {
            return getTeamWool(teamName);
        }
        return Material.WHITE_WOOL;
    }

    public static ChatColor getTeamColor(String teamName) {
        return TEAM_COLORS.getOrDefault(teamName, ChatColor.WHITE);
    }

    public static Map<String, Material> getAllTeamWools() {
        return TEAM_WOOL;
    }

    /**
     * Removes all players from all QLTML teams
     * @return The number of players removed from teams
     */
    public static int clearAllTeams() {
        int count = 0;
        
        for (Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith("qltml_")) {
                count += team.getEntries().size();
                
                // Remove all entries from the team
                for (String entry : new HashSet<>(team.getEntries())) {
                    team.removeEntry(entry);
                }
            }
        }
        
        return count;
    }
}