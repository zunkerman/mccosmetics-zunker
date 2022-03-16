package io.lumine.cosmetics.api.cosmetics;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.menu.MenuData;
import lombok.Getter;

public abstract class Cosmetic implements PropertyHolder,MenuData<CosmeticProfile> {

    @Getter private final String type;
    @Getter private final String key;
    
    public Cosmetic(String type, String key) {
        this.type = type;
        this.key = key;
    }
    
    public boolean has(CosmeticProfile profile) {
        return profile.has(this);
    }
        
    public void equip(CosmeticProfile profile) {
        profile.equip(this);
    }
    
    public boolean isEquipped(CosmeticProfile profile) {
        return profile.isEquipped(this);
    }
    
}
