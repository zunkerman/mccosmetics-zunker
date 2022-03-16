package io.lumine.cosmetics.managers.particle;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.menu.Icon;

import java.io.File;

public class ParticleAccessory extends AbstractCosmetic {

	public ParticleAccessory(File file, String key) {
		super(file, CosmeticType.type(ParticleAccessory.class), key);
	}

	@Override
	public String getPropertyNode() {
		return "Particle." + key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("particle effects");
	}
}
