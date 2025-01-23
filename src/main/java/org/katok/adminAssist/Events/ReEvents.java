package org.katok.adminAssist.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.katok.adminAssist.Commands.re_utils.re_mode;

public class ReEvents implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(player_from_data == null || player_from_data.equals("")) {
            return;
        }

        player.chat("/unre");
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(player_from_data == null || player_from_data.equals("")) {
            return;
        }

        e.setCancelled(true);
    }
}
