package io.lumine.cosmetics.managers.back;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;

import java.io.File;

public class BackAccessory extends AbstractCosmetic {

	public BackAccessory(File file, String key) {
		super(file, CosmeticType.type(BackAccessory.class), key);
	}

	@Override
	public String getPropertyNode() {
		return "Backs." + this.key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("back accessory");
	}

}
