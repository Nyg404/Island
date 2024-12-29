package io.github.nyg404.rootinsland.CommandsLister;


import io.github.nyg404.rootinsland.World.Utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;



public class PlayerJoin implements Listener {

    private final Plugin plugin;  // Добавим поле для хранения плагина

    // Конструктор, принимающий плагин
    public PlayerJoin(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String worldName = "worlds/" + player.getName() + "_world";

        // Проверяем, существует ли мир игрока
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            // Если мира нет — создаем новый остров
            createPlayerWorld(player, worldName);
        } else {
            // Если мир уже существует, телепортируем сразу
            player.teleport(world.getSpawnLocation());
        }
    }

    // Метод для создания мира
    public void createPlayerWorld(Player player, String worldName) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            World templateWorld = Bukkit.getWorld("template_world");

            if (templateWorld == null) {
                templateWorld = Bukkit.createWorld(new WorldCreator("template_world"));
            }

            if (templateWorld == null) {
                player.sendMessage(ChatColor.RED + "Не удалось загрузить шаблонный мир!");
                return;
            }

            WorldCreator creator = new WorldCreator(worldName);
            creator.copy(templateWorld);

            World world = creator.createWorld();

            if (world != null) {
                world.setAutoSave(true);
                world.setSpawnFlags(true, false);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);


                Location playerlocation = new Location(world, 0, 85,0);
                player.teleport(playerlocation);
                world.getWorldBorder().setCenter(0,0);
                world.getWorldBorder().setSize(100);
                // Вызов метода сохранения мира из утилитного класса
                WorldUtils.saveWorld(world);

                player.sendTitle(ChatColor.GREEN + "Ваш мир создан!", "");

            } else {
                player.sendMessage(ChatColor.RED + "Не удалось создать ваш мир.");
            }
        });
    }
}