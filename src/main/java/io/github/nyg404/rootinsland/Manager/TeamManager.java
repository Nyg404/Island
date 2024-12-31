package io.github.nyg404.rootinsland.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.nyg404.rootinsland.Rootinsland;
import io.github.nyg404.rootinsland.BredKakoito.TeamYml;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class TeamManager {



    private final Map<UUID, TeamYml> parties = new HashMap<>();
    private final Map<UUID, UUID> pendparties = new HashMap<>();
    private final File file;
    private final FileConfiguration config;

    public TeamManager(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder(), "parties.yml");
    
        if (!file.exists()) {
            // Если файл не существует, создаем его
            try {
                file.getParentFile().mkdirs();  // Создаем папки, если их нет
                if (file.createNewFile()) {  // Создаем новый файл
                    plugin.getLogger().info("Создан файл parties.yml");
                } else {
                    plugin.getLogger().warning("Не удалось создать файл parties.yml.");
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Ошибка при создании parties.yml.");
                e.printStackTrace();
            }
        }
    
        // Загружаем конфигурацию
        config = YamlConfiguration.loadConfiguration(file);
        loadParties();  // Загружаем данные о пати
    }

    public void invitePlayer(Player inviter, Player target) {
        if (inviter.getUniqueId().equals(target.getUniqueId())) {
            inviter.sendMessage("Вы не можете пригласить сами себя.");
            return;
        }
    
        // Проверка, является ли игрок уже частью команды
        if (parties.values().stream().anyMatch(team -> team.getMembers().contains(target.getUniqueId()))) {
            inviter.sendMessage(target.getName() + " уже является членом другой команды.");
            return;
        }
    
        if (pendparties.containsKey(target.getUniqueId())) {
            inviter.sendMessage(target.getName() + " уже имеет приглашение.");
            return;
        }
    
        pendparties.put(target.getUniqueId(), inviter.getUniqueId());
        inviter.sendMessage(Component.text("Вы пригласили игрока" + ChatColor.BLUE + target.getName() + ChatColor.RESET + "в пати"));
        target.sendMessage(inviter.getName() + " пригласил вас в пати. Введите" + ChatColor.BLUE + " /party accept" + ChatColor.RESET + " или" + ChatColor.DARK_RED + " /party deny." + ChatColor.RED + " У вас есть 5 минут.");
        Bukkit.getScheduler().runTaskLater(Rootinsland.getPlugin(Rootinsland.class), () -> {
            if (pendparties.containsKey(target.getUniqueId())) {
                pendparties.remove(target.getUniqueId());
                target.sendMessage("Ваше приглашение в пати от " + inviter.getName() + " истекло.");
                inviter.sendMessage("Приглашение для " + target.getName() + " истекло.");
            }
        }, 6000L);
    }
    

    public void acceptInvite(Player player) {
        UUID inviterUUID = pendparties.get(player.getUniqueId());
        if (inviterUUID == null) {
            player.sendMessage("У вас нет активных приглашений.");
            return;
        }
    
        // Получаем инвайтера
        Player inviter = Bukkit.getPlayer(inviterUUID);
        if (inviter == null) {
            player.sendMessage("Инвайтер больше не в игре.");
            return;
        }
    
        // Получаем команду инвайтера (или создаем новую, если её нет)
        TeamYml team = parties.computeIfAbsent(inviterUUID, k -> new TeamYml(inviterUUID, inviter.getName(), new HashSet<UUID>()));
    
        // Добавляем нового участника
        team.addMember(player.getUniqueId());
    
        // Сохраняем обновленную команду
        saveParty(inviterUUID);  // Сохранение изменений в конфигурации
    
        // Отправляем сообщения игрокам
        player.sendMessage("Вы присоединились к пати.");
        inviter.sendMessage(player.getName() + " присоединился к вашей пати.");
    
        // Удаляем приглашение
        pendparties.remove(player.getUniqueId());
    }
    
    public void denyInvite(Player player) {
        if (pendparties.remove(player.getUniqueId()) != null) {
            player.sendMessage("Вы отклонили приглашение.");
        } else {
            player.sendMessage("У вас нет активных приглашений.");
        }
    }

    public void kickMember(Player kicker, Player target) {
        TeamYml team = getPlayerTeam(kicker);
        if (team == null) {
            kicker.sendMessage("Вы не состоите в пати.");
            return;
        }
    
        if (!team.getMembers().contains(target.getUniqueId())) {
            kicker.sendMessage("Игрок не является участником вашей пати.");
            return;
        }
    
        if (!team.getOwnerUUID().equals(kicker.getUniqueId())) {
            kicker.sendMessage("Только владелец команды может кикать участников.");
            return;
        }
    
        try {
            team.removeMember(target.getUniqueId(), true);  // true - это кик
        } catch (IllegalArgumentException e) {
            kicker.sendMessage(e.getMessage());  // Сообщаем ошибку, если пытаются кикнуть владельца
            return;
        }
    
        // Отправляем уведомления
        kicker.sendMessage(target.getName() + " был кикнут из вашей пати.");
        target.sendMessage("Вы были кикнуты из пати " + kicker.getName());
    
        // Сохраняем изменения
        saveParty(team.getOwnerUUID());
    }

    public TeamYml getPlayerTeam(Player player) {
        for (TeamYml team : parties.values()) {
            if (team.getMembers().contains(player.getUniqueId())) {
                return team;  // Возвращаем команду, если игрок является ее участником
            }
        }
        return null;  // Если игрок не найден в ни одной команде
    }
    
    

    private void loadParties() {
    for (String owner : config.getKeys(false)) {
        UUID ownerUUID = UUID.fromString(owner);
        Set<UUID> members = new HashSet<>();
        for (String member : config.getStringList(owner)) {
            members.add(UUID.fromString(member));
        }
        String ownerName = config.getString(owner + ".ownerName");

        // Добавляем владельца в список участников
        members.add(ownerUUID);

        // Добавляем команду в Map
        parties.put(ownerUUID, new TeamYml(ownerUUID, ownerName, members));
    }
}




    
    private void saveParty(UUID ownerUUID) {
        TeamYml team = parties.get(ownerUUID);
        List<String> memberUUIDs = new ArrayList<>();
    
    // Сохраняем всех участников, включая владельца
        team.getMembers().forEach(uuid -> memberUUIDs.add(uuid.toString()));
    
        config.set(ownerUUID.toString(), memberUUIDs);  // Сохранение всех участников
        config.set(ownerUUID.toString() + ".ownerName", team.getOwnerName()); // Сохранение имени владельца
        saveConfig();
}

    
    
        

    public void saveParties() {
        for (UUID owner : parties.keySet()) {
            saveParty(owner);
        }
    }
     private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    



