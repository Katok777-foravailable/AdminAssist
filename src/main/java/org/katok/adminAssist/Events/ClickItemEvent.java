package org.katok.adminAssist.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.katok.adminAssist.Main.item_command;

public class ClickItemEvent implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(!e.getAction().isRightClick()) return;

        if(e.getItem() == null) return;

        if(e.getItem().getItemMeta() == null) return;

        if(e.getItem().getItemMeta().getPersistentDataContainer().get(item_command, PersistentDataType.STRING) == null) return;

        e.getPlayer().chat(e.getItem().getItemMeta().getPersistentDataContainer().get(item_command, PersistentDataType.STRING));
    }
}
