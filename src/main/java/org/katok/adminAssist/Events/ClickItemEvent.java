package org.katok.adminAssist.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.katok.adminAssist.Main.instance;
import static org.katok.adminAssist.Main.item_command;

public class ClickItemEvent implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) return;

        if(e.getItem() == null) return;

        if(e.getItem().getItemMeta() == null) return;

        if(e.getItem().getItemMeta().getPersistentDataContainer().get(item_command, PersistentDataType.STRING) == null) return;

        String command = e.getItem().getItemMeta().getPersistentDataContainer().get(item_command, PersistentDataType.STRING);

        Bukkit.getScheduler().runTaskLater(instance, new Runnable() {
            @Override
            public void run() {
                e.getPlayer().chat(command);
            }
        }, 1);
    }
}
