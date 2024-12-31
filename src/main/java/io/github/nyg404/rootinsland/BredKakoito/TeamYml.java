package io.github.nyg404.rootinsland.BredKakoito;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeamYml {
    private final UUID ownerUUID;
    private final String ownerName;
    private final Set<UUID> members = new HashSet<>();

    public TeamYml(UUID ownerUUID, String ownerName, Set<UUID> members) {
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.members.addAll(members);
    }

    public UUID getOwnerUUID(){
        return ownerUUID;
    }
    
    public String getOwnerName(){
        return ownerName;
    }

    public Set<UUID> getMembers(){
        return members;
    }

    public void addMember(UUID member) {
        members.add(member);
    }

    public void removeMember(UUID member, boolean isKick) {
        // Запрещаем удалять владельца пати
        if (member.equals(ownerUUID)) {
            if (isKick) {
                throw new IllegalArgumentException("Невозможно кикнуть владельца пати.");
            } else {
                throw new IllegalArgumentException("Невозможно удалить владельца пати.");
            }
        }
        members.remove(member);
    }
}
