package io.github.nyg404.rootinsland.World.Utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class WorldConfigManager {

    private final Plugin plugin;
    private File worldConfigFile;
    private YamlConfiguration worldConfig;

    public WorldConfigManager(Plugin plugin) {
        this.plugin = plugin;
        worldConfigFile = new File(plugin.getDataFolder(), "worlds.yml");

        // Создание нового файла, если его нет
        if (!worldConfigFile.exists()) {
            try {
                worldConfigFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Ошибка при создании конфигурационного файла worlds.yml: " + e.getMessage());
            }
        }

        worldConfig = YamlConfiguration.loadConfiguration(worldConfigFile);
    }

    // Получаем информацию о владельце мира
    public String getOwner(String playerName) {
        return worldConfig.getString("worlds." + playerName + ".owner", playerName);
    }

    // Получаем размер барьера для игрока
    public int getBarrierSize(String playerName) {
        return worldConfig.getInt("worlds." + playerName + ".barrierSize", 100); // По умолчанию 100
    }

    // Сохраняем информацию о мире
    public void saveWorldInfo(String playerName, String owner, int barrierSize) {
        worldConfig.set("worlds." + playerName + ".owner", owner);
        worldConfig.set("worlds." + playerName + ".barrierSize", barrierSize);

        try {
            worldConfig.save(worldConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при сохранении конфигурационного файла worlds.yml: " + e.getMessage());
        }
    }

    // Увеличиваем размер барьера
    public void increaseBarrierSize(String playerName, int increaseBy) {
        int currentSize = getBarrierSize(playerName);
        int newSize = currentSize + increaseBy;
        saveWorldInfo(playerName, getOwner(playerName), newSize);
    }

    // Удаляем информацию о мире
    public void removeWorldInfo(String playerName) {
        worldConfig.set("worlds." + playerName, null);

        try {
            worldConfig.save(worldConfigFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при удалении информации о мире для " + playerName + ": " + e.getMessage());
        }
    }
}
