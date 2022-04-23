package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.model.base.BaseEntity;
import com.ticxo.modelengine.model.MEModeledEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class MEGModel extends MEModeledEntity {

	private final Player player;

	public MEGModel(Player player, BaseEntity<?> entity) {
		super(entity);
		this.player = player;
	}

	@Override
	public List<Player> getPlayerInRange() {
		final var list = super.getPlayerInRange();
		list.add(player);
		return list;
	}


}
