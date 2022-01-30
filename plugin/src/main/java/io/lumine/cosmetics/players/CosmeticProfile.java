package io.lumine.cosmetics.players;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.lumine.utils.storage.players.Profile;
import lombok.Getter;

public class CosmeticProfile implements Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();
    
    @Getter private transient Player player;
    
    public CosmeticProfile() {}
    
    public CosmeticProfile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(final Player player)  {
        this.player = player;
    }

}
