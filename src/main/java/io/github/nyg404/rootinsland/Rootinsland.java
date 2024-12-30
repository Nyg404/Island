package io.github.nyg404.rootinsland;

import io.github.nyg404.rootinsland.Chat.ChatListener;
import io.github.nyg404.rootinsland.CommandsLister.CommandsCreate;
import io.github.nyg404.rootinsland.CommandsLister.CommandsIsland;
import io.github.nyg404.rootinsland.CommandsLister.PlayerJoin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public final class Rootinsland extends JavaPlugin {


    private FileConfiguration customConfig;


    @Override
    public void onEnable() {
        // Plugin startup logic
        File configDir = new File(getDataFolder(), "island/config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        // Загружаем или сохраняем конфигурацию, если ее нет
        File configFile = new File(configDir, "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false); // Сохраняем файл конфигурации, если его нет
        }


        customConfig = YamlConfiguration.loadConfiguration(configFile);


        getCommand("god").setExecutor(new CommandsCreate());
        getCommand("is").setExecutor(new CommandsIsland());

        // Передаем экземпляр плагина в обработчик событий PlayerJoin
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);;
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
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

    public double getChatRadius() {
        return customConfig.getDouble("chat-radius", 100.0); // 100.0 - значение по умолчанию, если ключ не найден
    }
}
