package io.lumine.cosmetics.players;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements CosmeticProfile,io.lumine.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    @Getter private ProfileInventory cosmeticInventory = new ProfileInventory();
        
    @Getter private transient Player player;
    
    public Profile() {}
    
    public Profile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(final Player player)  {
        this.player = player;
        this.cosmeticInventory.initialize(this);
    }

    @Override
    public boolean has(Cosmetic cosmetic) {
        return cosmeticInventory.hasUnlocked(cosmetic);
    }

    @Override
    public void equip(Cosmetic cosmetic) {
        cosmeticInventory.equip(cosmetic);
    }

    @Override
    public boolean isEquipped(Cosmetic cosmetic) {
        return cosmeticInventory.isEquipped(cosmetic);
    }

}
