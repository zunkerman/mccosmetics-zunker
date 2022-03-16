package io.lumine.cosmetics.nms;

import org.bukkit.entity.Player;

public class VolatileCodeDisabled implements VolatileCodeHandler {

    @Override
    public VolatileHatHelper getHatHelper() {
        return null;
    }

    @Override
    public void injectPlayer(Player player) {

    }

    @Override
    public void removePlayer(Player player) {

    }

}
