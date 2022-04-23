package io.lumine.cosmetics.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticInventory;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.back.BackAccessory;
import io.lumine.cosmetics.managers.hats.Hat;
import io.lumine.cosmetics.managers.sprays.Spray;
import io.lumine.utils.serialize.Optl;
import lombok.Getter;

import java.util.*;

public class ProfileInventory implements CosmeticInventory {

    @Getter private transient CosmeticProfile profile;

    @Getter private transient final Map<Class<? extends Cosmetic>, Cosmetic> equipped = Maps.newConcurrentMap();
    @Getter private transient final Set<Class<? extends Cosmetic>> hidden = Sets.newConcurrentHashSet();
    @Getter private final Map<String,List<String>> unlockedCosmetics = Maps.newConcurrentMap();
    @Getter private final Map<String,String> equippedCosmetics = Maps.newConcurrentMap();
    
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
        equipped.put(cosmetic.getClass(), cosmetic);
        cosmetic.getManager().equip(profile);
    }

    @Override
    public void unequip(Class<? extends Cosmetic> tClass) {
        equippedCosmetics.remove(CosmeticType.type(tClass));
        final var pCos = equipped.remove(tClass);
        if(pCos != null)
            pCos.getManager().unequip(profile);
    }

    public boolean isEquipped(Cosmetic cosmetic) {
        return cosmetic.isEquipped(this);
    }

    @Override
    public Collection<String> getUnlocked(String type) {
        return unlockedCosmetics.getOrDefault(type, Lists.newArrayList());
    }

    @Override
    public Optional<Cosmetic> getEquipped(Class<? extends Cosmetic> tClass) {
        return Optional.ofNullable(equipped.get(tClass));
    }

}
