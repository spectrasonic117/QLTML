package com.spectrasonic.QLTML;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.spectrasonic.QLTML.Utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MapResetService {

    private static final BlockVector3 PASTE_POSITION = BlockVector3.at(0, 64, 0);
    private final Main plugin;

    public MapResetService(Main plugin) {
        this.plugin = plugin;
    }

    public void resetMap(CommandSender sender) {
        MessageUtils.sendConfigMessage(sender, "map.reset-start");

        CompletableFuture.runAsync(() -> {
            try {
                File schematicFile = new File(plugin.getDataFolder().getParent(), "FastAsyncWorldEdit/schematics/qltml_map.schem");
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
}
