package io.github.nyg404.rootinsland.CommandsLister;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamTabCompleter implements TabCompleter {  // Название класса исправлено

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {  // Если первый аргумент (подкоманда)
            if (sender instanceof Player) {
                // Добавляем возможные подкоманды
                suggestions.add("invite");
                suggestions.add("accept");
                suggestions.add("deny");
                suggestions.add("disband");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
            // Подкоманда invite, добавляем имена игроков для автозаполнения
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.getName().equalsIgnoreCase(sender.getName())) {
                    suggestions.add(onlinePlayer.getName());
                }
            }
        }

        return suggestions;
    }
}
