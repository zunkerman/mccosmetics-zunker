package io.lumine.cosmetics.players;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import lombok.Getter;

public class Profile implements CosmeticProfile,io.lumine.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    @Getter private EquippedCosmetics cosmeticInventory;
        
    @Getter private transient Player player;
    
    public Profile() {}
    
    public Profile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(final Player player)  {
        this.player = player;
        this.equippedCosmetics = new EquippedCosmetics(this);
    }

    @Override
    public boolean has(Cosmetic cosmetic) {
        return unlockedCosmetics.getOrDefault(cosmetic.getType(), Collections.emptyList()).contains(cosmetic.getKey());
    }

    @Override
    public void equip(Cosmetic cosmetic) {
         if(this)
    }

    @Override
    public boolean isEquipped(Cosmetic cosmetic) {
        // TODO Auto-generated method stub
        return false;
    }


}
