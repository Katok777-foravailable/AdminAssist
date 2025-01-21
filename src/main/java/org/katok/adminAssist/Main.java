package org.katok.adminAssist;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.katok.adminAssist.Commands.re_utils;
import org.katok.adminAssist.Events.CollisionEvent;

import java.io.File;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static Logger logger;

    public static YamlConfiguration messages_cfg;
    public static File messages_file;

    @Override
    public void onEnable() {
        // обычные переменные
        instance = this;
        logger = getLogger();

        // файлы конфигурации
        saveDefaultConfig();

        messages_cfg = new YamlConfiguration();
        messages_file = new File(instance.getDataFolder(), "messages.yml");

        if(!messages_file.exists()) instance.saveResource("messages.yml", false);

        try {
            messages_cfg.load(messages_file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // загрузка эвентов
        getServer().getPluginManager().registerEvents(new CollisionEvent(), this);

        // загрузка комманд
        getCommand("re").setExecutor(new re_utils());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
