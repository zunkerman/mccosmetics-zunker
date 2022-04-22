package io.lumine.cosmetics.managers.back;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.manager.HideableCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.managers.gestures.Gesture;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;

import java.io.File;

public class BackManager extends MCCosmeticsManager<BackAccessory> implements HideableCosmetic {

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
        getHelper().apply(profile);
    }

    @Override
    public void unequip(CosmeticProfile profile) {
        getHelper().unapply(profile);
    }

    @Override
    public void hide(CosmeticProfile profile, Cosmetic request) {
        if(!(request instanceof Gesture))
            return;
        getHelper().unapply(profile);
    }

    @Override
    public void show(CosmeticProfile profile) {
        getHelper().apply(profile);
    }

    protected VolatileEquipmentHelper getHelper() {
        return (VolatileEquipmentHelper) getNMSHelper();
    }

}
