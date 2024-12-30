package io.github.nyg404.rootinsland.World.Utils;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;

public class PlayerWorldCreate {

    public void createplayerworld(Player player, String worldname) {
        // Попытка получить шаблонный мир
        World templateWorld = Bukkit.getWorld("template_world");

        String worldNamePlayer = "worlds/" + player.getName() + "_world";

        // Если шаблонный мир не существует, создаем его
        if (templateWorld == null) {
            templateWorld = Bukkit.createWorld(new WorldCreator("template_world"));
        }

        // Создание мира для игрока с кастомным генератором
        WorldCreator creator = new WorldCreator(worldNamePlayer);
        creator.copy(templateWorld); // Копируем данные из шаблонного мира

        World world = creator.createWorld(); // Создание мира с этим генератором

        if (world != null) {
            world.setAutoSave(true);
            world.setSpawnFlags(false, false); // Отключаем появление мобов
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false); // Отключаем смену дня/ночи
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false); // Отключаем спавн мобов
            world.getWorldBorder().setCenter(0, 0); // Центр мира
            world.getWorldBorder().setSize(50, 50); // Устанавливаем размер мира 100x100

            // Телепортируем игрока в центр мира
            Location playerLocation = new Location(world, 0, 82, 0);
            player.teleport(playerLocation);

            // Показываем титул игроку
            player.showTitle(Title.title(Component.text(ChatColor.DARK_RED + "Ваш мир создан!"), Component.text("")));

        } else {
            player.sendMessage(Component.text(ChatColor.DARK_RED + "Не удалось загрузить мир. Обратитесь в администрацию"));
        }
    }
}
