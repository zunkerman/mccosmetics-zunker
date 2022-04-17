package io.lumine.cosmetics.nms.v1_18_R1.cosmetic;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import lombok.Getter;

public class VolatileBackImpl implements VolatileBackHelper {

	@Getter private final MCCosmeticsPlugin plugin;

	public VolatileBackImpl(MCCosmeticsPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void applyBackPacket(CosmeticProfile profile) {

	}

}
