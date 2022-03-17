package io.lumine.cosmetics.players.inventory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.api.players.CosmeticInventory;
import io.lumine.cosmetics.managers.hats.Hat;
import io.lumine.utils.serialize.Optl;
import lombok.Getter;

public class DigitalInventory implements CosmeticInventory {

    @Getter private transient CosmeticProfile profile;
    
    private transient Optl<Hat> equippedHat;
    private transient final Map<String,Cosmetic> equippedCustom = Maps.newConcurrentMap();
    
    @Getter private Map<String,List<String>> unlockedCosmetics = Maps.newConcurrentMap();
    @Getter private Map<String,String> equippedCosmetics = Maps.newConcurrentMap();
    
    @Override
    public void initialize(CosmeticProfile profile) {
        this.profile = profile;
        
        for(var entry : equippedCosmetics.entrySet()) {
            final var type = entry.getKey();    
            final var name = entry.getValue();
            
            MCCosmeticsPlugin.inst().getCosmetics().getManager(type).ifPresent(manager -> {
                manager.getCosmetic(name).ifPresent(cosmetic -> {
                    equip((Cosmetic) cosmetic);
                });
            });
        }
    }

    public boolean hasUnlocked(Cosmetic cosmetic) {
        return cosmetic.has(this);
    }

    public void equip(Cosmetic cosmetic) {
        equippedCosmetics.put(cosmetic.getType(), cosmetic.getKey());
        
        if(cosmetic instanceof Hat hat) {
            equippedHat = Optl.of(hat);
        } else {
            
        }
        cosmetic.getManager().equip(profile);
    }

    public boolean isEquipped(Cosmetic cosmetic) {
        return cosmetic.isEquipped(this);
    }

    @Override
    public Optional<Cosmetic> getCustomEquipped(String type) {
        return Optional.ofNullable(equippedCustom.getOrDefault(type, null));
    }

    @Override
    public Optional<Cosmetic> getEquippedHat() {
        return Optional.ofNullable(equippedHat.orElseGet(null));
    }

    @Override
    public Collection<String> getUnlocked(String type) {
        return unlockedCosmetics.getOrDefault(type, Lists.newArrayList());
    }
    
}
