package com.spectrasonic.QLTML;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;

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
            {
                plugin.setGameActive(true);
                MessageUtils.sendMessage(sender, "<green>Minijuego activado");

                TeamManager.initialize();

                plugin.getConfig().getStringList("Start_Messages")
                .forEach(message -> Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message)));
                MessageUtils.broadcastTitle("<yellow><bold>QLTML", "<#C6C7E5>Aleah Aldebaran <white>& <#F48ED8>YumiYui", 1, 3, 1);
                MessageUtils.broadcastActionBar("<b><white>Developed by <#CB032C>Spectrasonic</b>");
                SoundUtils.broadcastPlayerSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        TeamSelectorGUI.openTeamSelector(player);
                    }, 40L);
                });
            }
        } else if (state.equalsIgnoreCase("off")) {
            plugin.setGameActive(false);
            
            int count = TeamManager.clearAllTeams();
            
            MessageUtils.sendMessage(sender, "<red>Minijuego desactivado");
            
            if (count > 0) {
                MessageUtils.sendMessage(sender, "<yellow>Todos los equipos han sido reiniciados");
                MessageUtils.sendBroadcastMessage("<yellow>Todos los equipos han sido reiniciados");
                
                SoundUtils.broadcastPlayerSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }
        } else {
            MessageUtils.sendMessage(sender, "<yellow>Uso: /qltml game <on|off>");
        }
    }

    @Subcommand("team")
    @CommandPermission("qltml.team")
    public void onTeam(CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>Este comando solo puede ser usado por jugadores");
            return;
        }
        
        Player player = (Player) sender;
        
        if (!plugin.isGameActive()) {
            MessageUtils.sendMessage(player, "<red>El minijuego no está activo");
            return;
        }
        
        TeamSelectorGUI.openTeamSelector(player);
        MessageUtils.sendMessage(player, "<green>Selecciona un equipo");

    }

    @Subcommand("give")
    @Syntax("<item>")
    @CommandCompletion("stick")
    @CommandPermission("qltml.bypass")
    public void onGive(CommandSender sender, String item) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendMessage(sender, "<red>Este comando solo puede ser usado por jugadores");
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
            MessageUtils.sendMessage(player, "<green>¡Has recibido un <red>Palo Volador</red>!");
        } else {
            MessageUtils.sendMessage(player, "<red>Item desconocido: " + item);
        }
    }
    
    @Subcommand("clearteams")
    @CommandPermission("qltml.bypass")
    public void onClearTeams(CommandSender sender) {
        if (!sender.hasPermission("qltml.bypass")) {
            MessageUtils.sendMessage(sender, "<red>No tienes permiso para usar este comando");
            return;
        }

        int count = TeamManager.clearAllTeams();

        MessageUtils.sendMessage(sender, "<green>Se han eliminado " + count + " jugadores de todos los equipos");                
        MessageUtils.sendBroadcastMessage("<yellow>Todos los equipos han sido reiniciados");
        SoundUtils.broadcastPlayerSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
    }

    @Subcommand("givefeather")
    @CommandPermission("qltml.bypass")
    public void onGiveFeather(CommandSender sender) {
        if (!sender.hasPermission("qltml.bypass")) {
            MessageUtils.sendMessage(sender, "<red>No tienes permiso para usar este comando");
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
                MessageUtils.sendMessage(player, "<green>Has recibido una <gradient:#DADCF5:#B6BBEC>Pluma de Vuelo</gradient>!");
            }
        });
        MessageUtils.sendMessage(sender, "<green>Plumas de Vuelo han sido dadas a todos los jugadores");
    }

    @Subcommand("resetmap")
    @CommandPermission("qltml.bypass")
    public void onResetMap(CommandSender sender) {
        // Permisos opcionales, si quieres puedes agregar chequeo aquí
        if (!(sender instanceof Player) && !(sender.hasPermission("qltml.bypass"))) {
            MessageUtils.sendMessage(sender, "<red>No tienes permiso para usar este comando");
            return;
        }

        MessageUtils.sendMessage(sender, "<yellow>Iniciando reseteo del mapa, por favor espera...");

        // Ejecutar la carga y pegado async para no bloquear el servidor
        CompletableFuture.runAsync(() -> {
            try {
                // Ruta del schematic
                File schematicFile = new File("plugins/FastAsyncWorldEdit/schematics/qltml_map.schem");
                if (!schematicFile.exists()) {
                    MessageUtils.sendMessage(sender, "<red>No se encontró el archivo schematic: qltml_map.schem");
                    return;
                }

                // Obtener mundo del servidor (usar el primer mundo cargado, o cambiar según necesidad)
                org.bukkit.World bukkitWorld = Bukkit.getWorlds().get(0);
                if (bukkitWorld == null) {

                    MessageUtils.sendMessage(sender, "<red>No se pudo obtener el mundo para pegar el schematic");
                    return;
                }

                // Adaptar el mundo a WorldEdit
                World weWorld = BukkitAdapter.adapt(bukkitWorld);

                // Leer schematic con WorldEdit API
                ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
                if (format == null) {
                    MessageUtils.sendMessage(sender, "<red>Formato de schematic no soportado");
                    return;
                }

                try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                    Clipboard clipboard = reader.read();

                    // Crear EditSession con un límite alto para permitir el pegado completo
                    try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
                        ClipboardHolder holder = new ClipboardHolder(clipboard);

                        // Crear operación de pegado en la posición definida, con reemplazo de bloques (no solo aire)
                        Operation operation = holder.createPaste(editSession)
                                .to(PASTE_POSITION)
                                .ignoreAirBlocks(false)
                                .build();

                        // Ejecutar la operación (bloqueante, pero estamos en async)
                        Operations.complete(operation);

                        // Cerrar sesión para aplicar cambios
                        editSession.flushQueue();

                        // Informar éxito al jugador (en hilo principal)
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            MessageUtils.sendMessage(sender, "<green>Mapa reseteado correctamente!");
                        });
                    }
                }
            } catch (IOException | WorldEditException ex) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    MessageUtils.sendMessage(sender, "<red>Error al cargar o pegar el schematic: " + ex.getMessage());
                });
                ex.printStackTrace();
            }
        });
    }
}