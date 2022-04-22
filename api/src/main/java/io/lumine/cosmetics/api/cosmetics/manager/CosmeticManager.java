package io.lumine.cosmetics.api.cosmetics.manager;

import io.lumine.cosmetics.api.players.CosmeticProfile;

public interface CosmeticManager {

    void equip(CosmeticProfile profile);
    void unequip(CosmeticProfile profile);
    
}
