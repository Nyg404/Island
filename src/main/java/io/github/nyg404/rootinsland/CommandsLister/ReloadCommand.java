package io.github.nyg404.rootinsland.CommandsLister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import io.github.nyg404.rootinsland.Rootinsland;

public class ReloadCommand implements CommandExecutor {

    private final Rootinsland plugin;

    public ReloadCommand(Rootinsland plugin) {
        this.plugin = plugin;
    }

    @Override
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadconfig")) {
            plugin.reloadCustomConfig();  // Перезагружаем customConfig
            sender.sendMessage("Конфигурация островов перезагружена!");
            return true;
    }
    return false;
}

}
