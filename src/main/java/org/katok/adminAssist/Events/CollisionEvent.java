package org.katok.adminAssist.Events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

import static org.katok.adminAssist.Main.config;
import static org.katok.adminAssist.Main.instance;

public class CollisionEvent implements Listener {
    public static final NamespacedKey walkThroughWalls = new NamespacedKey(instance, "walkThroughWalls");

    @EventHandler
    public void onSneak(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean player_can_pass_wall = Boolean.TRUE.equals(player.getPersistentDataContainer().get(walkThroughWalls, PersistentDataType.BOOLEAN));

        if((!player.getWorld().getBlockAt(player.getLocation().getBlockX(), //если под челиком если земля, либо он смотрит в блок
                player.getLocation().getBlockY() - 1,
                player.getLocation().getBlockZ())
                .getBlockData().getMaterial().equals(Material.AIR)
                && player.isSneaking()

            ||

            !player.getWorld().getBlockAt(
                    (int) Math.round(player.getLocation().getDirection().getX() + player.getLocation().getX()),
                    (int) Math.round(player.getLocation().getDirection().getY() + player.getLocation().getY()),
                    (int) Math.round(player.getLocation().getDirection().getZ() + player.getLocation().getZ()))
                    .getBlockData().getMaterial().equals(Material.AIR)
            && player.isFlying()
            ) && player_can_pass_wall)
        {
            player.setGameMode(GameMode.SPECTATOR);
        }
        else if(((player.getWorld().getBlockAt( // если под челиком нету земли, и он не смотрит в блок
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ())
                .getBlockData().getMaterial().equals(Material.AIR)
                &&
                player.getWorld().getBlockAt(
                        player.getLocation().getBlockX(),
                        player.getLocation().getBlockY() + 1,
                        player.getLocation().getBlockZ())
                        .getBlockData().getMaterial().equals(Material.AIR))
                && player.isFlying()
                ) && player_can_pass_wall)
        {
            player.setGameMode(GameMode.valueOf(config.getString("re.gamemode")));
        }
    }
}
