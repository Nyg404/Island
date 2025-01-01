package io.github.nyg404.rootinsland.Permission;

import net.luckperms.api.LuckPerms;
import io.github.nyg404.rootinsland.World.Utils.WorldConfigManager;

public class PlayerPermission {
    private final LuckPerms luckPerms;
    private final WorldConfigManager worldConfigManager;

    // Конструктор с передачей LuckPerms и WorldConfigManager
    public PlayerPermission(LuckPerms luckPerms, WorldConfigManager worldConfigManager) {
        this.luckPerms = luckPerms;
        this.worldConfigManager = worldConfigManager;
    }
}
