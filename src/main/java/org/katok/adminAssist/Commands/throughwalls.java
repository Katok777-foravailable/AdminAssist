package org.katok.adminAssist.Commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import static org.katok.adminAssist.Events.CollisionEvent.walkThroughWalls;
import static org.katok.adminAssist.Main.config;
import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getBoolean;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class throughwalls implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }
        if(!getBoolean("optimization.throughwalls")) return true;

        if(player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.INTEGER) == null || player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.INTEGER) == 0) {
            player.getPersistentDataContainer().set(walkThroughWalls, PersistentDataType.INTEGER, 1);
        } else {
            player.getPersistentDataContainer().set(walkThroughWalls, PersistentDataType.INTEGER, 0);
        }

        if (player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.INTEGER) == 1) {
            player.sendMessage(MessageFormat.format(getString("walkThroughWalls.walk", messages_cfg), getString("walkThroughWalls.start", messages_cfg)));
        } else {
            player.sendMessage(MessageFormat.format(getString("walkThroughWalls.walk", messages_cfg), getString("walkThroughWalls.stop", messages_cfg)));
            player.setGameMode(GameMode.valueOf(config.getString("re.gamemode")));
        }
        return true;
    }
}
