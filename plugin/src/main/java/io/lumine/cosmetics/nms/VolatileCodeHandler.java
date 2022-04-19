package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.nms.cosmetic.*;
import org.bukkit.entity.Player;

public interface VolatileCodeHandler {

    VolatileHatHelper getHatHelper();
    
    VolatileBackHelper getBackHelper();
    
    VolatileSprayHelper getSprayHelper();

    void injectPlayer(Player player);
    
    void removePlayer(Player player);

    void removeFakeEntity(int id);

}
