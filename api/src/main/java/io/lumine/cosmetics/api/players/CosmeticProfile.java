package io.lumine.cosmetics.api.players;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

public interface CosmeticProfile {

    public UUID getUniqueId();
    
    public String getName();
    
    public Player getPlayer();
    
    public boolean has(Cosmetic cosmetic);
    
    public void equip(Cosmetic cosmetic);
    
    public boolean isEquipped(Cosmetic cosmetic);
    
}
