package io.github.nyg404.rootinsland.CommandsLister;

import io.github.nyg404.rootinsland.World.Utils.PlayerWorldCreate;
import io.github.nyg404.rootinsland.Manager.WorldConfigManager;


import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoin implements Listener {

    private final Plugin plugin;
    private final WorldConfigManager configManager;  // Добавляем ссылку на WorldСonfigManager

    public PlayerJoin(Plugin plugin, WorldConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String worldName = "worlds/" + player.getName() + "_world";

        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            // Передаем configManager в конструктор PlayerWorldCreate
            PlayerWorldCreate worldCreator = new PlayerWorldCreate(configManager);
            worldCreator.createplayerworld(player, worldName);
        }
    }
}
