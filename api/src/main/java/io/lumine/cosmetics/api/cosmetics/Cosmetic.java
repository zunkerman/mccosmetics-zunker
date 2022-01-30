package io.lumine.cosmetics.api.cosmetics;

import io.lumine.cosmetics.api.players.CosmeticProfile;

public interface Cosmetic {

    public boolean has(CosmeticProfile profile);
        
    public boolean equip(CosmeticProfile profile);
    
    public boolean isEquipped(CosmeticProfile profile);
    
}
