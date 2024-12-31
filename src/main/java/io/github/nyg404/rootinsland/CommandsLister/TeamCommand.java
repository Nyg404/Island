package io.github.nyg404.rootinsland.CommandsLister;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.nyg404.rootinsland.Manager.TeamManager;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class TeamCommand implements CommandExecutor {

    private final TeamManager teamManager;

    
    public TeamCommand(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
        sender.sendMessage("Эту команду может использовать только игрок.");
        return true;
    }
    Player player = (Player) sender;

    if (args.length == 0) {
        player.sendMessage("/party <invite|accept|deny|kick>");
        return true;
    }

    switch (args[0].toLowerCase()) {
        case "invite":
            if (args.length < 2) {
                player.sendMessage(Component.text("Использование команды: /party invite <Игрок>"));
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                player.sendMessage(Component.text("Игрок не найден." + ChatColor.RED + args[1]));
                return true;
            }
            teamManager.invitePlayer(player, target);
            break;
        case "accept":
            teamManager.acceptInvite(player);
            break;

        case "deny":
            teamManager.denyInvite(player);
            break;

        case "kick":
            if (args.length < 2) {
                player.sendMessage("Использование команды: /party kick <Игрок>");
                return true;
            }
            Player targetToKick = Bukkit.getPlayerExact(args[1]);
            if (targetToKick == null) {
                player.sendMessage("Игрок не найден.");
                return true;
            }
            teamManager.kickMember(player, targetToKick);
            break;

        default:
            player.sendMessage("Неизвестная команда.");
            break;
    }
    return true;
}
}