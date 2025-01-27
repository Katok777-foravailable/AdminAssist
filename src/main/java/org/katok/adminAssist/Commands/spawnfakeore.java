package org.katok.adminAssist.Commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class spawnfakeore implements CommandExecutor, TabCompleter {
    public static final HashMap<String, Material> ores = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }

        if(strings.length < 2) {
            sender.sendMessage(getString("technicalPeculiarities.needMoreArguments", messages_cfg));
            return true;
        }

        if(!ores.containsKey(strings[0].toLowerCase())) {
            player.sendMessage(getString("spawnfakeore.blockdontfind", messages_cfg));
            return true;
        }

        Material ore = ores.get(strings[0]);
        if(!strings[1].chars().allMatch(Character::isDigit)) {
            player.sendMessage(getString("spawnfakeore.inSecondArgumentMustBeFigure", messages_cfg));
            return true;
        }
        int count_of_ore = Integer.parseInt(strings[1]);

        if(count_of_ore > 10) {
            player.sendMessage(getString("spawnfakeore.limitOfCountOfOres", messages_cfg));
            return true;
        }

        World playerWorld = player.getLocation().getWorld();
        Location playerLocation = player.getLocation();

        int stage = 0; // 0 стадия +1 к x, 1 стадия это +1 к z, 2 стадия это +1 к x и z
        int bias = 0; // + к блоку

        for(int i = 0; i < count_of_ore; i += 2) {
            // TODO: генерация руди, i это какая по счету руда
            // и сделать так чтобы через минут 10 блоки пропадали
            playerWorld.getBlockAt(playerLocation.getBlockX() + (stage == 0 || stage == 2? bias: 0), playerLocation.getBlockY(), playerLocation.getBlockZ() + (stage == 1 || stage == 2? bias: 0)).setType(ore);
            playerWorld.getBlockAt(playerLocation.getBlockX() + (stage == 0 || stage == 2? bias: 0), playerLocation.getBlockY() + (i + 1 == count_of_ore ? 0 : 1), playerLocation.getBlockZ()  + (stage == 1 || stage == 2? bias: 0)).setType(ore);
            if(stage == 2 || bias == 0) {
                stage = 0;
                bias++;
            } else {
                stage++;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            return ores.keySet().stream().toList();
        }
        return List.of();
    }
}
