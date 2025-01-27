package org.katok.adminAssist.Events;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;

import static org.katok.adminAssist.Commands.CheckCheater.cheater_check;
import static org.katok.adminAssist.Main.instance;
import static org.katok.adminAssist.Main.messages_cfg;
import static org.katok.adminAssist.utils.ConfigUtil.getString;

public class CheckCheaterEvents implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player verifiable = e.getPlayer();
        String admin_from_data = verifiable.getPersistentDataContainer().get(cheater_check, PersistentDataType.STRING);
        verifiable.getPersistentDataContainer().remove(cheater_check);

        if(StringUtils.isEmpty(admin_from_data)) return;

        Player admin = instance.getServer().getPlayer(admin_from_data);
        if(admin == null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MessageFormat.format(getString("checkCheat.commandOnLeave"), verifiable.getName()).substring(1));
            return;
        }

        admin.chat(MessageFormat.format(getString("checkCheater.commandOnLeave"), verifiable.getName()));
        admin.chat("/unre");
        admin.sendMessage(MessageFormat.format(getString("checkCheater.leave", messages_cfg), verifiable.getName()));
    }
}
