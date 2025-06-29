package com.spectrasonic.QLTML;

import com.spectrasonic.QLTML.Utils.ConfigManager;
import com.spectrasonic.QLTML.Utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import co.aikar.commands.PaperCommandManager;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    private boolean gameActive = false;
    private final Map<Character, Material> letterMapping = new HashMap<>();
    private ConfigManager configManager;
    private MapResetService mapResetService;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.init();
        MessageUtils.setPlugin(this);
        loadMappings();
        registerCommands();
        registerEvents();
        
        mapResetService = new MapResetService(this);
        
        MessageUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        MessageUtils.sendShutdownMessage();
    }
    
    public void reloadPluginConfigs() {
        configManager.reloadConfigs();
        loadMappings();
    }


    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new QLTMLCommand(this));
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new TeamSelectorListener(this), this);
        getServer().getPluginManager().registerEvents(new FlightListener(this), this);
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MapResetService getMapResetService() {
        return mapResetService;
    }

    public Material getMaterialForLetter(char letter) {
        return letterMapping.getOrDefault(Character.toLowerCase(letter), Material.STONE);
    }

    private void loadMappings() {
        ConfigurationSection section = getConfig().getConfigurationSection("mappings");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                if (key.length() != 1) {
                    getLogger().warning("Clave inválida en mappings: " + key + " (solo se permite una letra)");
                    continue;
                }
                try {
                    Material mat = Material.valueOf(section.getString(key).toUpperCase());
                    letterMapping.put(key.toLowerCase().charAt(0), mat);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Material inválido para la letra " + key);
                }
            }
        } else {
            getLogger().warning("No se encontró la sección 'mappings' en el config.yml");
        }
    }
}
