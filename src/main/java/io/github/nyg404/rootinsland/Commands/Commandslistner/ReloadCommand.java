package io.github.nyg404.rootinsland.Commands.Commandslistner;

import io.github.nyg404.rootinsland.Rootinsland;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final Rootinsland plugin;

    public ReloadCommand(Rootinsland plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("rootinsland.reloadconfig")) {
            plugin.reloadCustomConfig();
            sender.sendMessage("Configuration reloaded successfully!");
            return true;
        }
        sender.sendMessage("You don't have permission to reload the config.");
        return false;
    }
}
