package com.spectrasonic.QLTML.Utils;

import com.spectrasonic.QLTML.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigManager {

    private final Main plugin;
    private FileConfiguration messagesConfig = null;
    private File messagesFile = null;

    public void init() {
        saveDefaultMessagesConfig();
    }

    public void reloadConfigs() {
        plugin.reloadConfig();
        reloadMessagesConfig();
    }

    public FileConfiguration getMessagesConfig() {
        if (messagesConfig == null) {
            reloadMessagesConfig();
        }
        return messagesConfig;
    }

    public void reloadMessagesConfig() {
        if (messagesFile == null) {
            messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        // Look for defaults in the jar
        InputStream defConfigFileStream = plugin.getResource("messages.yml");
        if (defConfigFileStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigFileStream, StandardCharsets.UTF_8));
            messagesConfig.setDefaults(defConfig);
        }
    }

    public void saveDefaultMessagesConfig() {
        if (messagesFile == null) {
            messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        }
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }

    public String getMessage(String path) {
        String message = getMessagesConfig().getString(path, "Message not found: " + path);
        // Placeholder for prefix, will be handled in MessageUtils
        return message;
    }

    public List<String> getMessageList(String path) {
        return getMessagesConfig().getStringList(path);
    }
}
