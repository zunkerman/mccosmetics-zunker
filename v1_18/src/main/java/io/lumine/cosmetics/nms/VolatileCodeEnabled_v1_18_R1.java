package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.nms.v1_18_R1.VolatileHatImpl;
import lombok.Getter;

public class VolatileCodeEnabled_v1_18_R1 implements VolatileCodeHandler {

    @Getter private final MCCosmeticsPlugin plugin;
    @Getter private final VolatileHatHelper hatHelper;
    
    public VolatileCodeEnabled_v1_18_R1(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
        this.hatHelper = new VolatileHatImpl(plugin);
    }

}
