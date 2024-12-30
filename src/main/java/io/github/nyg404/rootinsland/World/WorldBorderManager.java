// package io.github.nyg404.rootinsland.World;

// import org.bukkit.Bukkit;
// import org.bukkit.ChatColor;
// import org.bukkit.Location;
// import org.bukkit.Material;
// import org.bukkit.World;
// import org.bukkit.block.Block;
// import org.bukkit.entity.Player;
// import org.bukkit.event.EventHandler;
// import org.bukkit.event.Listener;
// import org.bukkit.event.player.PlayerMoveEvent;

// public class WorldBorderManager implements Listener {

//     private final int BORDER_SIZE = 50;  // Половина от 100x100, граница от -50 до +50

//     @EventHandler
//     public void onPlayerMove(PlayerMoveEvent event) {
//         Player player = event.getPlayer();
//         World world = player.getWorld();
//         String worldName = world.getName();

//         // Проверяем, является ли мир островом (формат: worlds/playername_world)
//         if (!worldName.startsWith("worlds/")) return;

//         // Получаем имя владельца мира
//         String ownerName = worldName.split("/")[1].replace("_world", "");

//         Location loc = event.getTo();
//         double x = loc.getX();
//         double z = loc.getZ();

//         // Центр барьера - это (0, 0)
//         double centerX = 0.0;
//         double centerZ = 0.0;


//     }

//     // Телепортация на свой остров
//     private void teleportToOwnIsland(Player player) {
//         String playerWorldName = "worlds/" + player.getName() + "_world";
//         World playerWorld = Bukkit.getWorld(playerWorldName);

//         if (playerWorld != null) {
//             player.teleport(playerWorld.getSpawnLocation());
//             player.sendMessage(ChatColor.YELLOW + "Вы были телепортированы на свой остров.");
//         } else {
//             // Если по какой-то причине мир не загружен
//             player.sendMessage(ChatColor.RED + "Ваш остров не найден. Обратитесь к администратору.");
//         }
//     }


//         // Отправляем сообщение

// }

