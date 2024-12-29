package io.github.nyg404.rootinsland.CommandsLister;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsIsland implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может выполнить только игрок.");
            return true;
        }

        Player player = (Player) sender;

        // Проверка на права
        if (!player.hasPermission("rootplaginisland.tp")) {
            player.sendMessage(ChatColor.RED + "У вас нет прав на выполнение этой команды.");
            return true;
        }

        if (args.length != 2 || !args[0].equalsIgnoreCase("tp")) {
            player.sendMessage(ChatColor.YELLOW + "Использование: /is tp <игрок>");
            return true;
        }

        String targetPlayerName = args[1];
        String worldName = "worlds/" + targetPlayerName + "_world";
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            player.teleport(world.getSpawnLocation());
            player.sendMessage(ChatColor.GREEN + "Телепортация в мир " + targetPlayerName);
        } else {
            player.sendMessage(ChatColor.RED + "Мир игрока " + targetPlayerName + " не найден.");
        }

        return true;
    }
}