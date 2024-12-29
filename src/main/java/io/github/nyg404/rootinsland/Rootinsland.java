package io.github.nyg404.rootinsland;

import io.github.nyg404.rootinsland.CommandsLister.CommandsCreate;
import io.github.nyg404.rootinsland.CommandsLister.CommandsIsland;
import io.github.nyg404.rootinsland.CommandsLister.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rootinsland extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("god").setExecutor(new CommandsCreate());
        getCommand("is").setExecutor(new CommandsIsland());

        // Передаем экземпляр плагина в обработчик событий PlayerJoin
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
}
