package io.lumine.cosmetics.api.players;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

import java.util.Collection;
import java.util.Optional;

public interface CosmeticInventory {

    void initialize(CosmeticProfile profile);
    
    Collection<String> getUnlocked(String type);

    Optional<Cosmetic> getEquipped(Class<? extends Cosmetic> tClass);
    
    void equip(Cosmetic cosmetic);

    void unequip(Class<? extends Cosmetic> tClass);
}
