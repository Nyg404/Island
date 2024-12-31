package io.github.nyg404.rootinsland.Commands.Commandslistner;

import io.github.nyg404.rootinsland.Permission.PlayerPermission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class BorderCheckCommand implements CommandExecutor {
    private final PlayerPermission playerPermission;

    public BorderCheckCommand(PlayerPermission playerPermission) {
        this.playerPermission = playerPermission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
            return false;
        }

        Player player = (Player) sender;

        // Проверяем, есть ли у игрока права на просмотр информации о барьере
        if (!playerPermission.hasPermissionForBarrierIncrease(player)) {
            player.sendMessage(ChatColor.RED + "У вас нет прав для просмотра информации о барьере.");
            return false;
        }

        // Получаем текущий размер барьера
        int currentSize = playerPermission.getTotalBarrierIncrease(player);

        // Отображаем текущий размер барьера игроку
        player.sendMessage(ChatColor.YELLOW + "Текущий размер барьера: " + currentSize + " блоков.");
        return true;
    }
}
