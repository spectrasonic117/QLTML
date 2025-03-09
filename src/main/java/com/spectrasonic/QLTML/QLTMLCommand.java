package com.spectrasonic.QLTML;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.spectrasonic.QLTML.Utils.ItemBuilder;

@RequiredArgsConstructor
@CommandAlias("qltml")
public class QLTMLCommand extends BaseCommand {
    private final Main plugin;

    @Subcommand("game")
    @Syntax("<on|off>")
    @CommandCompletion("on|off")
    public void onGame(CommandSender sender, String state) {
        if (state.equalsIgnoreCase("on")) {
            plugin.setGameActive(true);
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Minijuego activado"));
        } else if (state.equalsIgnoreCase("off")) {
            plugin.setGameActive(false);
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Minijuego desactivado"));
        } else {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Uso: /qltml game <on|off>"));
        }
    }

    @Subcommand("give")
    @CommandCompletion("stick")
    public void onGiveStick(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Este comando solo puede ser usado por jugadores"));
            return;
        }
        
        Player player = (Player) sender;
        
        ItemBuilder flyingStick = ItemBuilder.setMaterial("stick")
                .setName("<gold><bold>Palo Volador")
                .setLore("<gray>Un palo mágico que te hace volar", 
                         "<gray>¡Golpea a tus enemigos para verlos salir volando!")
                .addEnchantment("knockback", 5)
                .setCustomModelData(1);
                
        player.getInventory().addItem(flyingStick.build());
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>¡Has recibido un <red>Palo Volador</red>!"));
    }
}
