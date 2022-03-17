package io.lumine.cosmetics.api.cosmetics;

import io.lumine.cosmetics.api.players.CosmeticInventory;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.menu.MenuData;
import lombok.Getter;

public abstract class Cosmetic implements PropertyHolder,MenuData<CosmeticProfile> {

    @Getter private final CosmeticManager manager;
    @Getter private final String type;
    @Getter private final String key;
    
    public Cosmetic(CosmeticManager manager, String type, String key) {
        this.manager = manager;
        this.type = type;
        this.key = key;
    }

    public boolean has(CosmeticProfile profile) {
        return has(profile.getCosmeticInventory());
    }
        
    public boolean has(CosmeticInventory inventory) {
        return inventory.getUnlocked(type).contains(key);
    }
    
    public void equip(CosmeticProfile profile) {
        profile.equip(this);
    }
    
    public void equip(CosmeticInventory inventory) {
        inventory.equip(this);
    }
    
    public boolean isEquipped(CosmeticProfile profile) {
        return profile.isEquipped(this);
    }
    
    public boolean isEquipped(CosmeticInventory inventory) {
        var maybeCosmetic = inventory.getCustomEquipped(type);
        
        if(maybeCosmetic.isEmpty()) {
            return false;
        }
        return maybeCosmetic.get().equals(this);
    }
    
}
