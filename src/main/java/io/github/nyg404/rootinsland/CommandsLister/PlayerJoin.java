package io.github.nyg404.rootinsland.CommandsLister;

import io.github.nyg404.rootinsland.World.Utils.PlayerWorldCreate;


import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;




public class PlayerJoin implements Listener {

    private final Plugin plugin;  // Добавим поле для хранения плагина

    
    public PlayerJoin(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String worldName = "worlds/" + player.getName() + "_world";

        
        World world = Bukkit.getWorld(worldName);

        if (world == null) {    
            PlayerWorldCreate worldCreator = new PlayerWorldCreate();
            worldCreator.createplayerworld(player, worldName);
        }
    }
}