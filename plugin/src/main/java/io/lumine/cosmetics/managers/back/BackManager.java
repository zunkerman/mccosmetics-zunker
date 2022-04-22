package io.lumine.cosmetics.managers.back;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.events.CosmeticPlayerLoadedEvent;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.utils.Events;

import java.io.File;

public class BackManager extends MCCosmeticsManager<BackAccessory> {

    public BackManager(MCCosmeticsPlugin plugin) {
        super(plugin, BackAccessory.class);

        load(plugin);
    }

    @Override
    public BackAccessory build(File file, String node) {
        return new BackAccessory(this, file, node);
    }

    @Override
    public void equip(CosmeticProfile profile) {
        ((VolatileEquipmentHelper) getPlugin().getVolatileCodeHandler().getCosmeticHelper(BackAccessory.class)).apply(profile);
    }
}
