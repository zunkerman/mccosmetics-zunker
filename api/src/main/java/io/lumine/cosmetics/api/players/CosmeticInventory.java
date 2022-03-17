package io.lumine.cosmetics.api.players;

import java.util.Collection;
import java.util.Optional;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

public interface CosmeticInventory {

    public void initialize(CosmeticProfile profile);
    
    public Collection<String> getUnlocked(String type);
    
    public Optional<Cosmetic> getCustomEquipped(String type);

    public Optional<Cosmetic> getEquippedHat();
    
    public void equip(Cosmetic cosmetic);
}
