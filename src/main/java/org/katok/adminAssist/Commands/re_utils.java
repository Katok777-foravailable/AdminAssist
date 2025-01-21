package org.katok.adminAssist.Commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static org.katok.adminAssist.Main.instance;

public class re_utils implements CommandExecutor {
    public static final NamespacedKey re_mode = new NamespacedKey(instance, "re_mode");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0) {
            return true;
        }
        sender.sendMessage(instance.getCommand(strings[0]) + "");
        return true;
    }
}
