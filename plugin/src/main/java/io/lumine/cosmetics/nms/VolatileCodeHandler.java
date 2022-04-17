package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import io.lumine.cosmetics.nms.cosmetic.VolatileHatHelper;
import org.bukkit.entity.Player;

public interface VolatileCodeHandler {

    VolatileHatHelper getHatHelper();
    VolatileBackHelper getBackHelper();

    void injectPlayer(Player player);
    void removePlayer(Player player);


}
