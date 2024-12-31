package io.github.nyg404.rootinsland;

import io.github.nyg404.rootinsland.Chat.ChatListener;
import io.github.nyg404.rootinsland.CommandsLister.CommandsCreate;
import io.github.nyg404.rootinsland.CommandsLister.CommandsIsland;
import io.github.nyg404.rootinsland.CommandsLister.PlayerJoin;
import io.github.nyg404.rootinsland.CommandsLister.ReloadCommand;
import io.github.nyg404.rootinsland.CommandsLister.TeamCommand;
import io.github.nyg404.rootinsland.CommandsLister.TeamTabCompleter;
import io.github.nyg404.rootinsland.Manager.TeamManager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public final class Rootinsland extends JavaPlugin {

    private TeamManager teamManager;
    // Добавляем переменную customConfig
    private FileConfiguration customConfig;

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public double distance() {
        return customConfig.getDouble("distant-localchat", 100);  // Читаем значение из island/config/config.yml
    }

    @Override
    public void onEnable() {

        teamManager = new TeamManager(this);
        // Plugin startup logic
        File configDir = new File(getDataFolder(), "island/config");
        if (!configDir.exists()) {
            configDir.mkdirs();  // Создаём папку, если её нет
        }
    
        // Загружаем или сохраняем конфигурацию, если её нет
        File configFile = new File(configDir, "config.yml");
        if (!configFile.exists()) {
            // Сохраняем ресурс config.yml в корень плагина
            saveResource("config.yml", false);
    
            // Перемещаем файл в нужную директорию
            File movedFile = new File(configDir, "config.yml");
            if (configFile.renameTo(movedFile)) {
                getLogger().info("Config moved to island/config/");
            } else {
                getLogger().warning("Failed to move config to island/config/");
            }
        }
        // Загружаем customConfig
        customConfig = YamlConfiguration.loadConfiguration(configFile);

        getCommand("god").setExecutor(new CommandsCreate());
        getCommand("is").setExecutor(new CommandsIsland());
        getCommand("reloadconfig").setExecutor(new ReloadCommand(this));
        getCommand("party").setExecutor(new TeamCommand(teamManager));
        getCommand("party").setTabCompleter(new TeamTabCompleter());

        // Регистрируем обработчики событий
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);

        saveDefaultConfig();  // Создаёт config.yml, если его нет
        int distance = getConfig().getInt("distant-localchat", 50);  // 50 - значение по умолчанию
        getLogger().info("Distant Local Chat: " + distance);
    }

    @Override
    public void onDisable() {
        // Сохраняем миры в основном потоке
        Bukkit.getScheduler().runTask(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
        });
    }

    public void reloadCustomConfig() {
        // Перезагружаем customConfig, проверяя правильный путь
        File configFile = new File(getDataFolder(), "island/config/config.yml");
        if (configFile.exists()) {
            customConfig = YamlConfiguration.loadConfiguration(configFile);
            getLogger().info("Custom config reloaded!");
        } else {
            getLogger().warning("Config file not found!");
        }
    }
}
