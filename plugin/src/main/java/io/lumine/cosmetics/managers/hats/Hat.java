package io.lumine.cosmetics.managers.hats;

import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Hat extends AbstractCosmetic implements ItemCosmetic {

	public Hat(File file, String key) {
	    super(file, CosmeticType.type(Hat.class), key);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

    @Override
    public Icon<CosmeticProfile> getIcon() {
        return buildIcon("hat");
    }

	@Override
	public ItemStack getCosmetic() {
		return getMenuItem();
	}
}
