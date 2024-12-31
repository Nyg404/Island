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

public final class Rootinsland extends JavaPlugin {

    private TeamManager teamManager;
    private FileConfiguration customConfig;

    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    public double distance() {
        return customConfig.getDouble("distant-localchat", 100);
    }

    @Override
    public void onEnable() {
        teamManager = new TeamManager(this);

        File configFile = new File(getDataFolder(), "island/config/config.yml");
        if (!configFile.exists()) {
            saveResource("island/config/config.yml", false);
            getLogger().info("Default config created at island/config/config.yml");
        }

        reloadCustomConfig();

        getCommand("god").setExecutor(new CommandsCreate());
        getCommand("is").setExecutor(new CommandsIsland());
        getCommand("reloadconfig").setExecutor(new ReloadCommand(this));
        getCommand("party").setExecutor(new TeamCommand(teamManager));
        getCommand("party").setTabCompleter(new TeamTabCompleter());

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().runTask(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
        });
    }

    public void reloadCustomConfig() {
        File configFile = new File(getDataFolder(), "island/config/config.yml");
        if (configFile.exists()) {
            customConfig = YamlConfiguration.loadConfiguration(configFile);
            getLogger().info("Custom config reloaded!");
        } else {
            getLogger().warning("Config file not found!");
        }
    }

    public void saveCustomConfig() {
        try {
            customConfig.save(new File(getDataFolder(), "island/config/config.yml"));
            getLogger().info("Custom config saved!");
        } catch (Exception e) {
            getLogger().warning("Failed to save custom config: " + e.getMessage());
        }
    }
}
