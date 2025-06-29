package com.spectrasonic.QLTML;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.RequiredArgsConstructor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.spectrasonic.QLTML.Utils.ItemBuilder;
import com.spectrasonic.QLTML.Utils.MessageUtils;
import com.spectrasonic.QLTML.Utils.TeamManager;
import com.spectrasonic.QLTML.Utils.TeamSelectorGUI;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import com.spectrasonic.QLTML.Utils.SoundUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@CommandAlias("qltml")
public class QLTMLCommand extends BaseCommand {

    private static final BlockVector3 PASTE_POSITION = BlockVector3.at(0, 64, 0);
    
    private final Main plugin;

    @Subcommand("game")
    @Syntax("<on|off>")
    @CommandCompletion("on|off")
    @CommandPermission("qltml.bypass")
    public void onGame(CommandSender sender, String state) {
        if (state.equalsIgnoreCase("on")) {
            plugin.setGameActive(true);
            MessageUtils.sendConfigMessage(sender, "game.activated");

            TeamManager.initialize();

            MessageUtils.broadcastConfigMessageList("game.start-broadcast");
            MessageUtils.broadcastConfigTitle("game.start-title.title", "game.start-title.subtitle", 1, 3, 1);
            MessageUtils.broadcastConfigActionBar("game.start-action-bar");
            SoundUtils.broadcastPlayerSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);

            Bukkit.getOnlinePlayers().forEach(player -> {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    TeamSelectorGUI.openTeamSelector(player);
                }, 40L);
            });
        } else if (state.equalsIgnoreCase("off")) {
            plugin.setGameActive(false);

            int count = TeamManager.clearAllTeams();

            MessageUtils.sendConfigMessage(sender, "game.deactivated");

            if (count > 0) {
                MessageUtils.sendConfigMessage(sender, "team.all-cleared");
                MessageUtils.sendBroadcastConfigMessage("team.all-cleared");
                SoundUtils.broadcastPlayerSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }
        } else {
            MessageUtils.sendConfigMessage(sender, "game.usage");
        }
    }

    @Subcommand("team")
    @CommandPermission("qltml.team")
    public void onTeam(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendConfigMessage(sender, "plugin.player-only");
            return;
        }

        Player player = (Player) sender;

        if (!plugin.isGameActive()) {
            MessageUtils.sendConfigMessage(player, "game.not-active");
            return;
        }

        TeamSelectorGUI.openTeamSelector(player);
        MessageUtils.sendConfigMessage(player, "team.select");
    }

    @Subcommand("give")
    @Syntax("<item>")
    @CommandCompletion("stick")
    @CommandPermission("qltml.bypass")
    public void onGive(CommandSender sender, String item) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendConfigMessage(sender, "plugin.player-only");
            return;
        }

        Player player = (Player) sender;

        if (item.equalsIgnoreCase("stick")) {
            ItemBuilder flyingStick = ItemBuilder.setMaterial("stick")
                    .setName("<gold><bold>Palo Volador")
                    .setLore(" ")
                    .setLore("<white>Un palo mágico que te hace volar",
                            "<white>¡Golpea a tus enemigos para verlos salir volando!")
                    .addEnchantment("knockback", 5)
                    .setCustomModelData(1);

            player.getInventory().addItem(flyingStick.build());
            MessageUtils.sendConfigMessage(player, "items.flying-stick-received");
        } else {
            MessageUtils.sendConfigMessage(player, "plugin.unknown-item", "{item}", item);
        }
    }

    @Subcommand("clearteams")
    @CommandPermission("qltml.bypass")
    public void onClearTeams(CommandSender sender) {
        if (!sender.hasPermission("qltml.bypass")) {
            MessageUtils.sendPermissionMessage(sender);
            return;
        }

        TeamManager.clearAllTeams();

        MessageUtils.sendConfigMessage(sender, "team.all-cleared");
        MessageUtils.sendBroadcastConfigMessage("team.all-cleared");
        SoundUtils.broadcastPlayerSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
    }

    @Subcommand("givefeather")
    @CommandPermission("qltml.bypass")
    public void onGiveFeather(CommandSender sender) {
        if (!sender.hasPermission("qltml.bypass")) {
            MessageUtils.sendPermissionMessage(sender);
            return;
        }

        ItemBuilder flightFeather = ItemBuilder.setMaterial("feather")
                .setName("<gradient:#DADCF5:#B6BBEC><b>Pluma de Vuelo</b></gradient>")
                .setLore(
                    "<gold>「Objeto Épico」",
                    "<#95b2b8>-----------------",
                    "<#b784b8>Mantén esta pluma",
                    "<#b784b8>en tu mano principal",
                    "<#b784b8>para volar",
                    "<#95b2b8>-----------------"
                )
                .setCustomModelData(100);

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.hasPermission("qltml.bypass")) {
                player.getInventory().addItem(flightFeather.build());
                MessageUtils.sendConfigMessage(player, "items.flight-feather-received");
            }
        });
        MessageUtils.sendConfigMessage(sender, "items.flight-feathers-given");
    }

    @Subcommand("resetmap")
    @CommandPermission("qltml.bypass")
    public void onResetMap(CommandSender sender) {
        if (!(sender instanceof Player) && !(sender.hasPermission("qltml.bypass"))) {
            MessageUtils.sendPermissionMessage(sender);
            return;
        }

        MessageUtils.sendConfigMessage(sender, "map.reset-start");

        CompletableFuture.runAsync(() -> {
            try {
                File schematicFile = new File("plugins/FastAsyncWorldEdit/schematics/qltml_map.schem");
                if (!schematicFile.exists()) {
                    MessageUtils.sendConfigMessage(sender, "map.schematic-not-found", "{file}", "qltml_map.schem");
                    return;
                }

                org.bukkit.World bukkitWorld = Bukkit.getWorlds().get(0);
                if (bukkitWorld == null) {
                    MessageUtils.sendConfigMessage(sender, "map.world-not-found");
                    return;
                }

                World weWorld = BukkitAdapter.adapt(bukkitWorld);
                ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                if (format == null) {
                    MessageUtils.sendConfigMessage(sender, "map.unsupported-format");
                    return;
                }

                try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                    Clipboard clipboard = reader.read();
                    try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
                        Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                                .to(PASTE_POSITION)
                                .ignoreAirBlocks(false)
                                .build();
                        Operations.complete(operation);
                        Bukkit.getScheduler().runTask(plugin, () -> MessageUtils.sendConfigMessage(sender, "map.reset-success"));
                    }
                }
            } catch (IOException | WorldEditException ex) {
                Bukkit.getScheduler().runTask(plugin, () -> MessageUtils.sendConfigMessage(sender, "map.reset-error", "{error}", ex.getMessage()));
                ex.printStackTrace();
            }
        });
    }

    @Subcommand("reload")
    @CommandPermission("qltml.reload")
    public void onReload(CommandSender sender) {
        plugin.reloadPluginConfigs();
        MessageUtils.sendConfigMessage(sender, "plugin.reload");
    }
}
