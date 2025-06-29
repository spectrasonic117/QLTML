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

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import com.spectrasonic.QLTML.Utils.SoundUtils;

@RequiredArgsConstructor
@CommandAlias("qltml")
public class QLTMLCommand extends BaseCommand {
    
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

            MinigameManager.startMinigame();

            Bukkit.getOnlinePlayers().forEach(player -> {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    TeamSelectorGUI.openTeamSelector(player);
                }, 40L);
            });
        } else if (state.equalsIgnoreCase("off")) {
            plugin.setGameActive(false);

            int count = TeamManager.clearAllTeams();

            MessageUtils.sendConfigMessage(sender, "game.deactivated");

            MinigameManager.stopMinigame();

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
        plugin.getMapResetService().resetMap(sender);
    }

    @Subcommand("reload")
    @CommandPermission("qltml.reload")
    public void onReload(CommandSender sender) {
        plugin.reloadPluginConfigs();
        MessageUtils.sendConfigMessage(sender, "plugin.reload");
    }
}
