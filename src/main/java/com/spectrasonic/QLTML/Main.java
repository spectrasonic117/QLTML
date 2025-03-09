package com.spectrasonic.QLTML;

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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadMappings();
        // Registrar comando utilizando ACF de Aikar
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new QLTMLCommand(this));
        // Registrar listener para capturar el chat
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        MessageUtils.sendStartupMessage(this);
    }

    @Override
    public void onDisable() {
        MessageUtils.sendShutdownMessage(this);
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameActive(boolean active) {
        this.gameActive = active;
    }

    public Material getMaterialForLetter(char letter) {
        // Se tratan las letras de forma insensible a mayúsculas/minúsculas
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
                    // Convertir la letra a minúscula para almacenarla siempre de la misma forma
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
