package io.lumine.cosmetics.api.players;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface CosmeticProfile {

    public UUID getUniqueId();
    
    public String getName();
    
    public Player getPlayer();
    
    public CosmeticInventory getCosmeticInventory();
    
    public boolean has(Cosmetic cosmetic);
    
    public void equip(Cosmetic cosmetic);
    
    public boolean isEquipped(Cosmetic cosmetic);
    
}
