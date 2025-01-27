package org.katok.adminAssist.Commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static org.katok.adminAssist.Main.instance;
import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class CheckCheater implements CommandExecutor {
    public static final NamespacedKey cheater_check = new NamespacedKey(instance, "cheater_check");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }

        if(strings.length == 0) {
            sender.sendMessage(getString("technicalPeculiarities.needMoreArguments", messages_cfg));
            return true;
        }

        Player spec_player = instance.getServer().getPlayer(strings[0]);

        if(spec_player == null) {
            sender.sendMessage(getString("technicalPeculiarities.specifyToPlayer", messages_cfg));
            return true;
        }

        spec_player.sendMessage(getString("checkCheater.check", messages_cfg));
        // TODO: вешаем data на читера, и при выходе писать от лица админа, комманду которая прописана в конфиге
        spec_player.getPersistentDataContainer().set(cheater_check, PersistentDataType.STRING, player.getName());


        return true;
    }
}
