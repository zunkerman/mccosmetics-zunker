package io.lumine.cosmetics.managers.gestures;

import com.ticxo.playeranimator.api.model.player.PlayerModel;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CustomPlayerModel extends PlayerModel {

	@Getter private final QuitMethod quitMethod;
	private final Runnable onEnd;
	private boolean isPlaying;

	public CustomPlayerModel(Player player, QuitMethod quitMethod, Runnable onEnd) {
		super(player);
		this.quitMethod = quitMethod;
		this.onEnd = onEnd;
	}

	public void stopAnimation(QuitMethod method) {
		isPlaying = method == null || quitMethod != method;
	}

	@Override
	public void playAnimation(String name) {
		super.playAnimation(name);
		isPlaying = true;
	}

	@Override
	public boolean update() {
		boolean update = super.update();
		if(!update || !isPlaying) {
			onEnd.run();
			return false;
		}
		return true;
	}

}
