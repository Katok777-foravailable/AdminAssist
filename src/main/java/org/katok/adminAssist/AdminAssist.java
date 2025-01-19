package org.katok.adminAssist;

import org.bukkit.plugin.java.JavaPlugin;
import org.katok.adminAssist.Events.CollisionEvent;

public final class AdminAssist extends JavaPlugin {

    public static AdminAssist instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new CollisionEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
