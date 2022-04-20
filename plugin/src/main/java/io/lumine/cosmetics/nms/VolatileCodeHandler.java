package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.nms.cosmetic.VolatileCosmeticHelper;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface VolatileCodeHandler {

    VolatileCosmeticHelper getCosmeticHelper(Class<? extends Cosmetic> tClass);
    Collection<VolatileCosmeticHelper> getCosmeticHelpers();

    void injectPlayer(Player player);
    
    void removePlayer(Player player);

    void removeFakeEntity(int id);

}
