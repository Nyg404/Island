package io.github.nyg404.rootinsland.Chat;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import io.github.nyg404.rootinsland.Rootinsland;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;



public class ChatListener implements Listener {

    private final Rootinsland plugin;

    public ChatListener(Rootinsland plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // Получаем радиус чата из конфигурации
        double chatRadius = plugin.getCustomConfig().getDouble("distant-localchat", 100);


        // Проверяем, начинается ли сообщение с префикса "!" для глобального чата
        if (message.startsWith("!")) {
            // Убираем префикс "!" из сообщения
            message = message.substring(1);
            // Отправляем сообщение всем игрокам на сервере (глобальный чат)
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                String text = ChatColor.BLUE + "Г" + ChatColor.RESET + " [%luckperms_groups%] %player_name% : " + message ;
                text = PlaceholderAPI.setPlaceholders(event.getPlayer(), text);
                otherPlayer.sendMessage(text);
            }
        } else {
            // Локальный чат (радиус видимости)
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                if (otherPlayer.getWorld().equals(player.getWorld()) && player.getLocation().distance(otherPlayer.getLocation()) <= chatRadius) {
                    String text = ChatColor.YELLOW + "Л" + ChatColor.RESET + " [%luckperms_groups%] %player_name% : " + message ;
                    text = PlaceholderAPI.setPlaceholders(event.getPlayer(), text);
                    otherPlayer.sendMessage(text);
                }
            }
        }

        // Отменяем стандартное поведение чата, чтобы сообщения не распространялись по умолчанию
        event.setCancelled(true);
    }
}
