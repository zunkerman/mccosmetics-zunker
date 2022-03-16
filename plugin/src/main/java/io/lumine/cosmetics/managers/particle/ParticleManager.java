package io.lumine.cosmetics.managers.particle;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;

import java.io.File;

public class ParticleManager extends MCCosmeticsManager<ParticleAccessory> {

	public ParticleManager(MCCosmeticsPlugin plugin, Class<ParticleAccessory> megAccessoryClass) {
		super(plugin, megAccessoryClass);
	}

	@Override
	public ParticleAccessory build(File file, String node) {
		return new ParticleAccessory(file, node);
	}

	@Override
	public void equip(Profile profile) {
		// TODO: 15/3/2022 Impl particle accessory
	}
}
