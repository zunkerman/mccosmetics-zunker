package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import io.lumine.cosmetics.nms.cosmetic.VolatileHatHelper;
import io.lumine.cosmetics.nms.cosmetic.VolatileSprayHelper;

import org.bukkit.entity.Player;

public class VolatileCodeDisabled implements VolatileCodeHandler {

    @Override
    public VolatileHatHelper getHatHelper() {
        return null;
    }

    @Override
    public VolatileBackHelper getBackHelper() {
        return null;
    }
    
    @Override
    public VolatileSprayHelper getSprayHelper() {
        return null;
    }

    @Override
    public void injectPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void removeFakeEntity(int id) {

    }

}
