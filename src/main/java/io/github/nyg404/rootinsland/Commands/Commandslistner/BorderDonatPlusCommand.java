package io.github.nyg404.rootinsland.Commands.Commandslistner;

import io.github.nyg404.rootinsland.Permission.PlayerPermission;
import io.github.nyg404.rootinsland.World.Utils.WorldConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.io.IOException;

public class BorderDonatPlusCommand implements CommandExecutor {
    private final PlayerPermission playerPermission;
    private final WorldConfigManager worldConfigManager;

    public BorderDonatPlusCommand(PlayerPermission playerPermission, WorldConfigManager worldConfigManager) {
        this.playerPermission = playerPermission;
        this.worldConfigManager = worldConfigManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок!");
            return false;
        }

        Player player = (Player) sender;

        // Проверяем, есть ли у игрока права на увеличение барьера
        if (!playerPermission.hasPermissionForBarrierIncrease(player)) {
            player.sendMessage(ChatColor.RED + "У вас нет прав для использования этой команды.");
            return false;
        }

        // Получаем текущий размер барьера с учётом доната
        int newSize = playerPermission.getTotalBarrierIncrease(player);

        worldConfigManager.saveWorldInfo(player.getName(), player.getName(), newSize);

        // Обновляем барьер мира
        player.getWorld().getWorldBorder().setSize(newSize);

        // Информируем игрока о результате
        player.sendMessage(ChatColor.GREEN + "Размер барьера увеличен до " + newSize + " блоков.");
        return true;
    }
}
