package io.lumine.cosmetics.nms.cosmetic;

import io.lumine.cosmetics.api.players.CosmeticProfile;

public interface VolatileBackHelper {

	void applyBackPacket(CosmeticProfile profile);

	static byte toByte(float val) {
		return (byte)((int)(val * 256.0F / 360.0F));
	}

}
