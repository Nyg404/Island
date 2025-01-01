package io.github.nyg404.rootinsland;

import io.github.nyg404.rootinsland.Chat.ChatListener;
import io.github.nyg404.rootinsland.Commands.Commandslistner.CommandsCreate;
import io.github.nyg404.rootinsland.Commands.Commandslistner.CommandsIsland;
import io.github.nyg404.rootinsland.Commands.Commandslistner.ReloadCommand;

import io.github.nyg404.rootinsland.Commands.TabCommands.TeamTabCompleter;
import io.github.nyg404.rootinsland.CommandsLister.PlayerJoin;
import io.github.nyg404.rootinsland.CommandsLister.TeamCommand;
import io.github.nyg404.rootinsland.Manager.TeamManager;
import io.github.nyg404.rootinsland.World.Utils.WorldConfigManager;
import io.github.nyg404.rootinsland.Permission.PlayerPermission;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;

public final class Rootinsland extends JavaPlugin {

    private TeamManager teamManager;
    private WorldConfigManager worldConfigManager;
    private FileConfiguration customConfig;
    private LuckPerms luckPerms;
    private PlayerPermission playerPermissions;
    private WorldConfigManager configManager = new WorldConfigManager(this);  // 'this' — это экземпляр плагина

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public double distance() {
        return customConfig.getDouble("distant-localchat", 100);
    }

    @Override
    public void onEnable() {
        // Ожидание загрузки LuckPerms
        if (!setupLuckPerms()) {
            getLogger().warning("LuckPerms не найден на сервере! Плагин не будет работать корректно.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Ожидание загрузки LuckPerms, если он ещё не доступен
        int attemptCount = 0;
        while (luckPerms == null && attemptCount < 10) {
            try {
                Thread.sleep(500); // Подождать 500 мс
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            luckPerms = getLuckPermsInstance();
            attemptCount++;
        }

        if (luckPerms == null) {
            getLogger().warning("LuckPerms не был найден после 5 секунд ожидания, плагин не будет работать.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Инициализация после загрузки LuckPerms
        teamManager = new TeamManager(this);
        worldConfigManager = new WorldConfigManager(this);

        // Создание конфигурационного файла, если он не существует
        File configFile = new File(getDataFolder(), "island/config/config.yml");
        if (!configFile.exists()) {
            saveResource("island/config/config.yml", false);
            getLogger().info("Default config created at island/config/config.yml");
        }

        reloadCustomConfig();

        // Регистрация команд
        getCommand("god").setExecutor(new CommandsCreate());
        getCommand("is").setExecutor(new CommandsIsland());
        getCommand("reloadconfig").setExecutor(new ReloadCommand(this));
        getCommand("party").setExecutor(new TeamCommand(teamManager));
        getCommand("party").setTabCompleter(new TeamTabCompleter());

        // Регистрация новых команд


        // Регистрация событий
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this, worldConfigManager), this);

        getLogger().info("Rootinsland plugin initialized after waiting for LuckPerms.");
    }

    @Override
    public void onDisable() {
        // Сохранение мира при отключении плагина
        Bukkit.getScheduler().runTask(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
        });
    }

    // Метод для установки LuckPerms
    private boolean setupLuckPerms() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
            playerPermissions = new PlayerPermission(luckPerms, worldConfigManager);  // Инициализируем Playerpermisson с luckPerms
            return true;
        }
        return false;
    }

    // Метод для получения экземпляра LuckPerms
    private LuckPerms getLuckPermsInstance() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        return provider != null ? provider.getProvider() : null;
    }

    // Перезагрузка конфигурации
    public void reloadCustomConfig() {
        File configFile = new File(getDataFolder(), "island/config/config.yml");
        if (configFile.exists()) {
            customConfig = YamlConfiguration.loadConfiguration(configFile);
            getLogger().info("Custom config reloaded!");
        } else {
            getLogger().warning("Config file not found!");
        }
    }

    // Сохранение конфигурации
    public void saveCustomConfig() {
        try {
            customConfig.save(new File(getDataFolder(), "island/config/config.yml"));
            getLogger().info("Custom config saved!");
        } catch (Exception e) {
            getLogger().warning("Failed to save custom config: " + e.getMessage());
        }
    }
}
