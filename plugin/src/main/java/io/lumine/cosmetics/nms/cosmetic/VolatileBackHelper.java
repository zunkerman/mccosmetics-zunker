package io.lumine.cosmetics.nms.cosmetic;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import org.bukkit.entity.Player;

public interface VolatileBackHelper {

	void applyBackPacket(CosmeticProfile profile);
	void respawnForPlayer(Player wearer, Player observer);
	void despawnForPlayer(Player wearer, Player observer);

}
