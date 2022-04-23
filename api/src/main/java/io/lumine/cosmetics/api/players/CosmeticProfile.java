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

    public void unequip(Cosmetic cosmetic);

    void setHidden(Class<? extends Cosmetic> cosmetic, boolean flag);
    
    public boolean isEquipped(Cosmetic cosmetic);

    boolean isHidden(Class<? extends Cosmetic> cosmetic);
    
}
