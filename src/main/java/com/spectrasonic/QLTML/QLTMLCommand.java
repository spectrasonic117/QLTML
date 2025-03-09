package com.spectrasonic.QLTML;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

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
}
