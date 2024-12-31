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

import io.github.nyg404.rootinsland.BredKakoito.TeamYml;

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
    
        // Проверка, является ли игрок уже владельцем другой команды
        if (parties.containsKey(target.getUniqueId())) {
            inviter.sendMessage(target.getName() + " уже является владельцем другой команды.");
            return;
        }
    
        // Проверка, является ли игрок уже частью команды
        if (parties.values().stream().anyMatch(team -> team.getMembers().contains(target.getUniqueId()))) {
            inviter.sendMessage(target.getName() + " уже является членом другой команды.");
            return;
        }
    
        // Проверка на наличие приглашения
        if (pendparties.containsKey(target.getUniqueId())) {
            inviter.sendMessage(target.getName() + " уже имеет приглашение.");
            return;
        }
    
        pendparties.put(target.getUniqueId(), inviter.getUniqueId());
        inviter.sendMessage("Вы пригласили игрока " + target.getName() + " в пати.");
        target.sendMessage(inviter.getName() + " пригласил вас в пати. Введите /party accept для принятия или /party deny для отклонения.");
    
        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Rootinsland"), () -> {
            if (pendparties.containsKey(target.getUniqueId())) {
                pendparties.remove(target.getUniqueId());
                target.sendMessage("Ваше приглашение в пати от " + inviter.getName() + " истекло.");
                inviter.sendMessage("Приглашение для " + target.getName() + " истекло.");
            }
        }, 6000L);  // 5 минут (6000 тиков)
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
    
        // Проверка, является ли игрок уже частью команды
        if (parties.values().stream().anyMatch(team -> team.getMembers().contains(player.getUniqueId()))) {
            player.sendMessage("Вы уже являетесь членом другой команды.");
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
        // Проверка, является ли игрок кикнувшим владельцем пати или имеет права
        TeamYml team = getPlayerTeam(kicker);
        if (team == null) {
            kicker.sendMessage("Вы не являетесь участником пати.");
            return;
        }
    
        // Если цель не является членом пати
        if (!team.getMembers().contains(target.getUniqueId())) {
            kicker.sendMessage(target.getName() + " не является членом вашей пати.");
            return;
        }
    
        // Удаляем игрока из пати (передаем true, чтобы указать, что это кик)
        team.removeMember(target.getUniqueId(), true);
        saveParty(kicker.getUniqueId());  // Сохраняем изменения в конфигурации
    
        kicker.sendMessage(target.getName() + " был кикнут из вашей пати.");
        target.sendMessage("Вы были кикнуты из пати " + kicker.getName() + ".");
    }

    public void saveParty(UUID ownerUUID) {
    TeamYml team = parties.get(ownerUUID);
    if (team != null) {
        // Сохраняем команду в файл конфигурации
        List<String> members = new ArrayList<>();
        for (UUID memberUUID : team.getMembers()) {
            members.add(memberUUID.toString());
        }

        // Добавляем данные в конфиг
        config.set(ownerUUID.toString() + ".ownername", Bukkit.getPlayer(ownerUUID).getName());  // Имя владельца
        config.set(ownerUUID.toString() + ".members", members);  // Список членов
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


private void loadParties() {
    for (String key : config.getKeys(false)) {
        UUID ownerUUID = UUID.fromString(key);
        
        // Загружаем данные из конфигурации
        String ownerName = config.getString(key + ".ownername");  // Имя владельца
        List<String> membersList = config.getStringList(key + ".members");  // Список членов

        Set<UUID> members = new HashSet<>();
        for (String memberUUID : membersList) {
            members.add(UUID.fromString(memberUUID));
        }

        // Создаем объект команды
        TeamYml team = new TeamYml(ownerUUID, ownerName, members);
        parties.put(ownerUUID, team);
    }
}


    public TeamYml getPlayerTeam(Player player) {
        return parties.get(player.getUniqueId());
    }
}
