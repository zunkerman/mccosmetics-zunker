package io.lumine.cosmetics.managers.modelengine;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;

import java.io.File;

public class MEGManager extends MCCosmeticsManager<MEGAccessory> {

	public MEGManager(MCCosmeticsPlugin plugin, Class<MEGAccessory> megAccessoryClass) {
		super(plugin, megAccessoryClass);
	}

	@Override
	public MEGAccessory build(File file, String node) {
		return new MEGAccessory(file, node);
	}

	@Override
	public void equip(Profile profile) {
		// TODO: 15/3/2022 Impl meg equip
	}
}
