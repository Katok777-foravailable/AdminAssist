package org.katok.adminAssist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.katok.adminAssist.Commands.*;
import org.katok.adminAssist.Events.CheckCheaterEvents;
import org.katok.adminAssist.Events.ClickItemEvent;
import org.katok.adminAssist.Events.CollisionEvent;
import org.katok.adminAssist.Events.ReEvents;

import java.io.File;
import java.util.logging.Logger;

import static org.katok.adminAssist.Commands.spawnfakeore.ores;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static Logger logger;

    public static YamlConfiguration messages_cfg;
    public static File messages_file;

    public static FileConfiguration config;

    public static NamespacedKey item_command;

    @Override
    public void onEnable() {
        // обычные переменные
        instance = this;
        logger = getLogger();
        item_command = new NamespacedKey(instance, "item_command");

        // файлы конфигурации
        saveDefaultConfig();

        config = getConfig();

        messages_cfg = new YamlConfiguration();
        messages_file = new File(instance.getDataFolder(), "messages.yml");

        if(!messages_file.exists()) instance.saveResource("messages.yml", false);

        try {
            messages_cfg.load(messages_file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // загрузка эвентов
        getServer().getPluginManager().registerEvents(new CollisionEvent(), instance);
        getServer().getPluginManager().registerEvents(new ReEvents(), instance);
        getServer().getPluginManager().registerEvents(new ClickItemEvent(), instance);
        getServer().getPluginManager().registerEvents(new CheckCheaterEvents(), instance);

        // загрузка комманд
        getCommand("re").setExecutor(new re_utils());
        getCommand("unre").setExecutor(new unre());
        getCommand("throughwalls").setExecutor(new throughwalls());
        getCommand("checkcheater").setExecutor(new CheckCheater());
        getCommand("spawnfakeore").setExecutor(new spawnfakeore());

        // загрузка руд для spawnfakeore
        ores.put("iron", Material.IRON_ORE);
        ores.put("diamond", Material.DIAMOND_ORE);
        ores.put("gold", Material.GOLD_ORE);
        ores.put("emerald", Material.EMERALD_ORE);
        ores.put("coal", Material.COAL_ORE);
        ores.put("redstone", Material.REDSTONE_ORE);
        ores.put("lapis", Material.LAPIS_ORE);
    }
}
