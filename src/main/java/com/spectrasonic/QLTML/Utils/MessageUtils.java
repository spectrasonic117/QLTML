package com.spectrasonic.QLTML.Utils;

import com.spectrasonic.QLTML.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public final class MessageUtils {

    private static Main plugin;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private MessageUtils() {
        // Private constructor to prevent instantiation
    }
    
    public static void setPlugin(Main main) {
        plugin = main;
    }

    private static String getPrefix() {
        return plugin.getConfigManager().getMessage("plugin.prefix");
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(miniMessage.deserialize(getPrefix() + message));
    }
    
    public static void sendMessage(CommandSender sender, String message, String... replacements) {
        sender.sendMessage(miniMessage.deserialize(getPrefix() + replace(message, replacements)));
    }

    public static void sendConfigMessage(CommandSender sender, String path) {
        sendMessage(sender, plugin.getConfigManager().getMessage(path));
    }
    
    public static void sendConfigMessage(CommandSender sender, String path, String... replacements) {
        sendMessage(sender, plugin.getConfigManager().getMessage(path), replacements);
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(miniMessage.deserialize(getPrefix() + message));
    }

    public static void sendPermissionMessage(CommandSender sender) {
        sendConfigMessage(sender, "plugin.no-permission");
    }

    public static void sendStartupMessage() {
        String prefix = getPrefix();
        String divider = "<gray>----------------------------------------</gray>";
        String[] messages = {
                divider,
                prefix + "<white>" + plugin.getPluginMeta().getName() + "</white> <green>Plugin Enabled!</green>",
                prefix + "<light_purple>Version: </light_purple>" + plugin.getPluginMeta().getVersion(),
                prefix + "<white>Developed by: </white><red>" + plugin.getPluginMeta().getAuthors().toString() + "</red>",
                divider
        };

        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(miniMessage.deserialize(message));
        }
    }
    
    public static void sendVeiMessage() {
        String prefix = getPrefix();
        String[] messages = {
                prefix + "⣇⣿⠘⣿⣿⣿⡿⡿⣟⣟⢟⢟⢝⠵⡝⣿⡿⢂⣼⣿⣷⣌⠩⡫⡻⣝⠹⢿⣿⣷",
                prefix + "⡆⣿⣆⠱⣝⡵⣝⢅⠙⣿⢕⢕⢕⢕⢝⣥⢒⠅⣿⣿⣿⡿⣳⣌⠪⡪⣡⢑⢝⣇",
                prefix + "⡆⣿⣿⣦⠹⣳⣳⣕⢅⠈⢗⢕⢕⢕⢕⢕⢈⢆⠟⠋⠉⠁⠉⠉⠁⠈⠼⢐⢕⢽",
                prefix + "⡗⢰⣶⣶⣦⣝⢝⢕⢕⠅⡆⢕⢕⢕⢕⢕⣴⠏⣠⡶⠛⡉⡉⡛⢶⣦⡀⠐⣕⢕",
                prefix + "⡝⡄⢻⢟⣿⣿⣷⣕⣕⣅⣿⣔⣕⣵⣵⣿⣿⢠⣿⢠⣮⡈⣌⠨⠅⠹⣷⡀⢱⢕",
                prefix + "⡝⡵⠟⠈⢀⣀⣀⡀⠉⢿⣿⣿⣿⣿⣿⣿⣿⣼⣿⢈⡋⠴⢿⡟⣡⡇⣿⡇⡀⢕",
                prefix + "⡝⠁⣠⣾⠟⡉⡉⡉⠻⣦⣻⣿⣿⣿⣿⣿⣿⣿⣿⣧⠸⣿⣦⣥⣿⡇⡿⣰⢗⢄",
                prefix + "⠁⢰⣿⡏⣴⣌⠈⣌⠡⠈⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣬⣉⣉⣁⣄⢖⢕⢕⢕",
                prefix + "⡀⢻⣿⡇⢙⠁⠴⢿⡟⣡⡆⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣵⣵⣿",
                prefix + "⡻⣄⣻⣿⣌⠘⢿⣷⣥⣿⠇⣿⣿⣿⣿⣿⣿⠛⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿",
                prefix + "⣷⢄⠻⣿⣟⠿⠦⠍⠉⣡⣾⣿⣿⣿⣿⣿⣿⢸⣿⣦⠙⣿⣿⣿⣿⣿⣿⣿⣿⠟",
                prefix + "⡕⡑⣑⣈⣻⢗⢟⢞⢝⣻⣿⣿⣿⣿⣿⣿⣿⣿⠸⣿⠿⠃⣿⣿⣿⣿⣿⣿⡿⠁⣠",
                prefix + "⡝⡵⡈⢟⢕⢕⢕⢕⣵⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣿⣿⣿⣿⣿⠿⠋⣀⣈⠙",
                prefix + "⡝⡵⡕⡀⠑⠳⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⢉⡠⡲⡫⡪⡪⡣",
        };

        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(miniMessage.deserialize(message));
        }
    }

    public static void sendBroadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player ->
            player.sendMessage(miniMessage.deserialize(message))
        );
    }
    
    public static void sendBroadcastConfigMessage(String path, String... replacements) {
        String message = plugin.getConfigManager().getMessage(path);
        final String finalMessage = replace(message, replacements);
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(miniMessage.deserialize(getPrefix() + finalMessage))
        );
    }

    public static void sendShutdownMessage() {
        String divider = "<gray>----------------------------------------</gray>";
        String[] messages = {
                divider,
                getPrefix() + "<red>" + plugin.getPluginMeta().getName() + " plugin Disabled!</red>",
                divider
        };

        for (String message : messages) {
            Bukkit.getConsoleSender().sendMessage(miniMessage.deserialize(message));
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final Component titleComponent = miniMessage.deserialize(title);
        final Component subtitleComponent = miniMessage.deserialize(subtitle);
        player.showTitle(Title.title(titleComponent, subtitleComponent, Times.times(
            Duration.ofSeconds(fadeIn),
            Duration.ofSeconds(stay),
            Duration.ofSeconds(fadeOut)
        )));
    }

    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(miniMessage.deserialize(message));
    }

    public static void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final Component titleComponent = miniMessage.deserialize(title);
        final Component subtitleComponent = miniMessage.deserialize(subtitle);
        final Title formattedTitle = Title.title(titleComponent, subtitleComponent, Times.times(
                Duration.ofSeconds(fadeIn),
                Duration.ofSeconds(stay),
                Duration.ofSeconds(fadeOut)
        ));

        Bukkit.getOnlinePlayers().forEach(player -> player.showTitle(formattedTitle));
    }
    
    public static void broadcastConfigTitle(String titlePath, String subtitlePath, int fadeIn, int stay, int fadeOut) {
        String title = plugin.getConfigManager().getMessage(titlePath);
        String subtitle = plugin.getConfigManager().getMessage(subtitlePath);
        broadcastTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void broadcastActionBar(String message) {
        final Component component = miniMessage.deserialize(message);
        Bukkit.getOnlinePlayers().forEach(player -> player.sendActionBar(component));
    }
    
    public static void broadcastConfigActionBar(String path) {
        String message = plugin.getConfigManager().getMessage(path);
        broadcastActionBar(message);
    }
    
    public static void broadcastConfigMessageList(String path, String... replacements) {
        List<String> messages = plugin.getConfigManager().getMessageList(path);
        List<String> processedMessages = messages.stream()
                .map(msg -> replace(msg, replacements))
                .collect(Collectors.toList());
        
        processedMessages.forEach(MessageUtils::sendBroadcastMessage);
    }

    private static String replace(String message, String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        return message;
    }
}
