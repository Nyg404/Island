package io.github.nyg404.rootinsland.Permission;

import org.bukkit.entity.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

public class Playerpermisson {
    private final LuckPerms luckPerms;

    public Playerpermisson(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    // Получаем максимальный размер барьера в зависимости от донат-ранга
    public int getMaxBarrierSize(Player player) {
        UserManager userManager = luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());
        
        if (user == null) {
            return 100;  // Если пользователя нет в LuckPerms, возвращаем значение по умолчанию
        }

        // Получаем мета-данные
        String donationRank = user.getCachedData().getMetaData().getMetaValue("donationRank");

        // Проверяем, если мета-данные пустые или отсутствуют, возвращаем значение по умолчанию
        if (donationRank == null || donationRank.isEmpty()) {
            return 100;  // Значение по умолчанию
        }

        switch (donationRank) {
            case "i":
                return 200;
            case "i+":
                return 250;
            case "g":
                return 300;
            case "g+":
                return 350;
            default:
                return 100;  // значение по умолчанию
        }
    }
}
