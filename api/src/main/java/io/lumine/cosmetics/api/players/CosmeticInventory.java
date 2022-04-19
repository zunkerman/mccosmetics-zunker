package io.lumine.cosmetics.api.players;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

import java.util.Collection;
import java.util.Optional;

public interface CosmeticInventory {

    void initialize(CosmeticProfile profile);
    
    Collection<String> getUnlocked(String type);
    
    Optional<Cosmetic> getCustomEquipped(String type);

    Optional<Cosmetic> getEquippedHat();

    Optional<Cosmetic> getEquippedBack();
    
    void equip(Cosmetic cosmetic);
}
