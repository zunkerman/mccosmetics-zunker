package io.lumine.cosmetics.nms.cosmetic;

import org.bukkit.entity.Player;

import java.util.List;

public interface VolatileCosmeticHelper {

	default void read(Player sender, Object packet) {

	}

	default List<Object> write(Player receiver, Object packet) {
		return null;
	}

}
