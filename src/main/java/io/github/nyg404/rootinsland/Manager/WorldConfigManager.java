package io.github.nyg404.rootinsland.Manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class WorldConfigManager {

        private final Plugin plugin;
        private File worldConfigFile;
        private YamlConfiguration worldConfig;

        public WorldConfigManager (Plugin plugin) {
            this.plugin = plugin;
            worldConfigFile = new File(plugin.getDataFolder(), "WorldAdvencet.yml");

        // Создание нового файла, если его нет
            if (!worldConfigFile.exists()) {
                try {
                    worldConfigFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }

        worldConfig = YamlConfiguration.loadConfiguration(worldConfigFile);
    }
        public String getOwner(String playerName){
            return worldConfig.getString("worlds." + playerName + ".owner", playerName);
        }

        public int getBarrierSize(String playerName) {
            return worldConfig.getInt("worlds." + playerName + ".barrierSize", 100);  // Размер по умолчанию
        }

        public void saveWorldInfo(String playerName, String owner, int barrierSize) {
            worldConfig.set("worlds." + playerName + ".owner", owner);
            worldConfig.set("worlds." + playerName + ".barrierSize", barrierSize);
    
            try {
                worldConfig.save(worldConfigFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

}
