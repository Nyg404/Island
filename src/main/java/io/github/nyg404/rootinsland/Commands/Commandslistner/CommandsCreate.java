package io.github.nyg404.rootinsland.Commands.Commandslistner;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsCreate implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            Player p = (Player) commandSender;

            if (p.isInvulnerable()){

                p.setInvulnerable(false);
            //  p.sendTitle(ChatColor.AQUA + "Режим бога выключен", "");
                p.showTitle(Title.title(Component.text(ChatColor.DARK_RED + "Режим бога выключен"), Component.text("")));
            }else{
                p.setInvulnerable(true);
            //    p.sendTitle(ChatColor.AQUA + "Режим бога включен", "");
                p.showTitle(Title.title(Component.text(ChatColor.DARK_RED + "Режим бога включен"), Component.text("")));
            }
        }
        return true;
    }
}
