package io.lumine.cosmetics.players.inventory;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import io.lumine.cosmetics.api.players.CosmeticInventory;
import io.lumine.cosmetics.managers.hats.Hat;
import io.lumine.utils.serialize.Optl;
import lombok.Getter;

public class DigitalInventory implements CosmeticInventory {

    @Getter private Map<String,List<String>> unlockedCosmetics = Maps.newConcurrentMap();
    @Getter private Map<String,String> equippedCosmetics = Maps.newConcurrentMap();
    
    private Optl<Hat> hat;
    
    private final Map<String,String> custom = Maps.newConcurrentMap();

    @Override
    public void initialize() {
        for(var entry : equippedCosmetics.entrySet()) {
            
        }
    }

}
