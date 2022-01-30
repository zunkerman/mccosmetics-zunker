package io.lumine.cosmetics.managers;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.ProfileManager;
import io.lumine.utils.plugin.ReloadableModule;

public abstract class MCCosmeticsManager extends ReloadableModule<MCCosmeticsPlugin> {

    public MCCosmeticsManager(MCCosmeticsPlugin plugin) {
        super(plugin,false);
    }
    
    protected ProfileManager getProfiles() {
        return plugin.getProfiles();
    }

}
