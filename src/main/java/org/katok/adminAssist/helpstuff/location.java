package org.katok.adminAssist.helpstuff;

import org.bukkit.Location;
import org.bukkit.World;

public class location {
    public static Location location_combination(Location first, Location second, World world) {
        return world.getBlockAt((int) Math.round(first.getX() + second.getX()),
                (int) Math.round(first.getBlockY() + second.getY()),
                (int) Math.round(first.getBlockZ() + second.getZ())).getLocation();

    }
}
