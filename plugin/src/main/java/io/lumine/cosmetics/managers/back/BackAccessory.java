package io.lumine.cosmetics.managers.back;

import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;
import io.lumine.xikage.mythicmobs.utils.items.ItemFactory;

import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BackAccessory extends AbstractCosmetic implements ItemCosmetic {

	public BackAccessory(BackManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(BackAccessory.class), key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("back accessory");
	}

    @Override
    public ItemStack getCosmetic(CosmeticVariant variant) {
        var item = getMenuItem();
        
        if(variant != null) {
            item = ItemFactory.of(item).color(variant.getColor()).build();
        }

        return item;
    }
}
