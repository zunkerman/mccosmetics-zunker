package io.lumine.cosmetics.managers.hats;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;

import java.io.File;

public class Hat extends AbstractCosmetic {

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

}
