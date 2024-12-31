package io.github.nyg404.rootinsland.Commands.Commandslistner;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.nyg404.rootinsland.Manager.WorldConfigManager;
import io.github.nyg404.rootinsland.Permission.Playerpermisson;

public class BorderCommand implements org.bukkit.command.CommandExecutor {

    private final Playerpermisson playerPermissions;
    private final WorldConfigManager configManager;

    public BorderCommand(Playerpermisson playerPermissions, WorldConfigManager configManager) {
        this.playerPermissions = playerPermissions;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Получаем максимальный размер барьера через PlayerPermissions
            int maxBarrierSize = playerPermissions.getMaxBarrierSize(player);
            int currentBarrierSize = configManager.getBarrierSize(player.getName());

            // Команда без аргументов: выводит информацию о барьере
            if (args.length == 0) {
                player.sendMessage("Текущий размер барьера: " + currentBarrierSize + "x" + currentBarrierSize);
                player.sendMessage("Максимальный размер барьера: " + maxBarrierSize + "x" + maxBarrierSize);
                return true;
            }

            // Команда увеличения барьера
            if (args.length == 1 && args[0].equalsIgnoreCase("increase")) {
                // Проверяем, можем ли увеличить барьер
                if (currentBarrierSize < maxBarrierSize) {
                    int newBarrierSize = currentBarrierSize + 1;
                    configManager.saveWorldInfo(player.getName(), player.getName(), newBarrierSize);

                    World world = Bukkit.getWorld("worlds/" + player.getName() + "_world");
                    if (world != null) {
                        world.getWorldBorder().setSize(newBarrierSize, newBarrierSize);
                    }

                    player.sendMessage("Размер вашего барьера увеличен на 1 блок! Новый размер: " + newBarrierSize + "x" + newBarrierSize);
                } else {
                    player.sendMessage("Вы не можете увеличить размер барьера.");
                }
                return true;
            }

            // Команда уменьшения барьера
            if (args.length == 1 && args[0].equalsIgnoreCase("decrease")) {
                if (currentBarrierSize > 100) {  // Минимальный размер барьера — 100
                    int newBarrierSize = currentBarrierSize - 1;
                    configManager.saveWorldInfo(player.getName(), player.getName(), newBarrierSize);

                    World world = Bukkit.getWorld("worlds/" + player.getName() + "_world");
                    if (world != null) {
                        world.getWorldBorder().setSize(newBarrierSize, newBarrierSize);
                    }

                    player.sendMessage("Размер вашего барьера уменьшен на 1 блок! Новый размер: " + newBarrierSize + "x" + newBarrierSize);
                } else {
                    player.sendMessage("Размер барьера не может быть меньше 100 блоков.");
                }
                return true;
            }
        }
        return false;
    }
}
