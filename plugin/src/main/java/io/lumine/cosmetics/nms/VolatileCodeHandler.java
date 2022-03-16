package io.lumine.cosmetics.nms;

import org.bukkit.entity.Player;

public interface VolatileCodeHandler {

    public VolatileHatHelper getHatHelper();

    void injectPlayer(Player player);
    void removePlayer(Player player);


}
