package io.github.nyg404.rootinsland.Permission;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.entity.Player;
import io.github.nyg404.rootinsland.World.Utils.WorldConfigManager;

public class PlayerPermission {
    private final LuckPerms luckPerms;
    private final WorldConfigManager worldConfigManager;

    // Конструктор с передачей LuckPerms и WorldConfigManager
    public PlayerPermission(LuckPerms luckPerms, WorldConfigManager worldConfigManager) {
        this.luckPerms = luckPerms;
        this.worldConfigManager = worldConfigManager;
    }

    // Получаем донат-ранг игрока
    public String getDonationRank(Player player) {
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());

        if (user == null) {
            return "default"; // Если игрок не найден, возвращаем дефолтный ранг
        }

        String donationRank = user.getCachedData().getMetaData().getMetaValue("donationRank");
        return (donationRank != null) ? donationRank : "default"; // Если donationRank null, возвращаем "default"
    }

    // Получаем количество блоков для донат-ранга
    public int getBarrierIncreaseForRank(String donationRank) {
        switch (donationRank) {
            case "i":
                return 50; // Ранг "i" даёт +50 блоков
            case "i+":
                return 75; // Ранг "i+" даёт +75 блоков
            case "g":
                return 125; // Ранг "g" даёт +125 блоков
            case "g+":
                return 150; // Ранг "g+" даёт +150 блоков
            default:
                return 0; // Если не найдено, возвращаем 0
        }
    }

    // Проверка, добавлено ли уже увеличение барьера для текущего уровня доната
    public boolean hasPermissionForBarrierIncrease(Player player) {
        String donationRank = getDonationRank(player);
        
        // Проверка на наличие нужного разрешения
        return player.hasPermission("rootinsland.bordercheck") && 
               (donationRank.equals("i") || donationRank.equals("i+") || donationRank.equals("g") || donationRank.equals("g+"));
    }
    

    // Получаем общий размер барьера для игрока
    public int getTotalBarrierIncrease(Player player) {
        String donationRank = getDonationRank(player);
        
        // Получаем текущий размер барьера из YML через WorldConfigManager
        int currentBarrierSize = getCurrentBarrierSize(player);

        // Получаем количество блоков для текущего уровня доната
        int increaseForRank = getBarrierIncreaseForRank(donationRank);

        // Проверяем, если блоки для этого уровня доната ещё не были добавлены
        if (currentBarrierSize == 100) {  // Это значение предполагается как базовое
            return 100 + increaseForRank;  // Добавляем блоки для первого уровня
        } else {
            // Если у игрока уже есть бонусы, сравниваем их с новым уровнем
            return currentBarrierSize + increaseForRank;
        }
    }

    // Метод для получения текущего размера барьера из YML через WorldConfigManager
    private int getCurrentBarrierSize(Player player) {
        // Получаем размер барьера для игрока из конфигурации (например, из YML)
        return worldConfigManager.getBarrierSize(player.getName());  // Это пример, нужно убедиться, что метод getBarrierSize есть в WorldConfigManager
    }

    // Устанавливаем новый размер барьера
    public void setBarrierSize(Player player, int newSize) {
        // Получаем конфигурацию и устанавливаем новый размер барьера
        worldConfigManager.saveWorldInfo(player.getName(), worldConfigManager.getOwner(player.getName()), newSize);
    }
}
