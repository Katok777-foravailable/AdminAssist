package org.katok.adminAssist.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Objects;

import static org.katok.adminAssist.Events.CollisionEvent.walkThroughWalls;
import static org.katok.adminAssist.Main.config;
import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class throughwalls implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }
        player.getPersistentDataContainer().set(walkThroughWalls, PersistentDataType.BOOLEAN, !Boolean.TRUE.equals(player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.BOOLEAN)));
        if(Boolean.TRUE.equals(player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.BOOLEAN))) {
            player.sendMessage(MessageFormat.format(getString("walkThroughWalls.walk", messages_cfg), getString("walkThroughWalls.start", messages_cfg)));
        } else {
            player.sendMessage(MessageFormat.format(getString("walkThroughWalls.walk", messages_cfg), getString("walkThroughWalls.stop", messages_cfg)));
            player.setGameMode(GameMode.valueOf(config.getString("re.gamemode")));
        }
        return true;
    }
}
