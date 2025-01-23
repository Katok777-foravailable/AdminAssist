package org.katok.adminAssist;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.katok.adminAssist.Commands.re_utils;
import org.katok.adminAssist.Commands.throughwalls;
import org.katok.adminAssist.Commands.unre;
import org.katok.adminAssist.Events.ClickItemEvent;
import org.katok.adminAssist.Events.CollisionEvent;
import org.katok.adminAssist.Events.ReEvents;

import java.io.File;
import java.util.logging.Logger;

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

        // загрузка комманд
        getCommand("re").setExecutor(new re_utils());
        getCommand("unre").setExecutor(new unre());
        getCommand("throughwalls").setExecutor(new throughwalls());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
