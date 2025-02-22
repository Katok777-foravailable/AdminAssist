package org.katok.adminAssist.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static org.katok.adminAssist.Commands.re_utils.*;
import static org.katok.adminAssist.Events.CollisionEvent.walkThroughWalls;
import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class unre implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(StringUtils.isEmpty(player_from_data)) {
            player.sendMessage(getString("unre.youDoNotSpectate", messages_cfg));
            return true;
        }

        if(inventories.containsKey(player.getName())) {
            player.getInventory().setContents(inventories.get(player.getName()));
            inventories.remove(player.getName());
        }
        if(locations.containsKey(player.getName())) {
            player.teleport(locations.get(player.getName()));
            locations.remove(player.getName());
        }
        if(gamemodes.containsKey(player.getName())) {
            player.setGameMode(gamemodes.get(player.getName()));
            gamemodes.remove(player.getName());
        }
        player.chat(getString("re.vanish"));
        player.getPersistentDataContainer().remove(re_mode);
        player.getPersistentDataContainer().remove(walkThroughWalls);
        return true;
    }
}
