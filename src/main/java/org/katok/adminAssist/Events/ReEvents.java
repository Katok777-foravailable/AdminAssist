package org.katok.adminAssist.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.StringUtil;
import org.codehaus.plexus.util.StringUtils;

import static org.katok.adminAssist.Commands.re_utils.re_mode;
import static org.katok.adminAssist.Main.config;
import static org.katok.adminAssist.Main.instance;

public class ReEvents implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(StringUtils.isEmpty(player_from_data)) {
            return;
        }

        player.chat("/unre");
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(StringUtils.isEmpty(player_from_data)) {
            return;
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onMoveForCompass(PlayerMoveEvent e) {
        if(!config.getBoolean("optimization.re_compass_track")) return;

        String player_from_data = e.getPlayer().getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(StringUtils.isEmpty(player_from_data)) {
            return;
        }

        Player p = instance.getServer().getPlayer(player_from_data);

        if(p == null) return;

        for(ItemStack item: e.getPlayer().getInventory().getContents()) {
            if(item == null || !item.getType().equals(Material.COMPASS)) {
                continue;
            }
            CompassMeta itemMeta = (CompassMeta) item.getItemMeta();
            itemMeta.setLodestone(p.getLocation());
            item.setItemMeta(itemMeta);

        }
    }
}
