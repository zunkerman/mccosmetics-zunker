package io.lumine.cosmetics.managers.gestures;

import com.google.common.collect.Lists;
import com.ticxo.playeranimator.api.PlayerAnimator;
import com.ticxo.playeranimator.api.model.player.PlayerModel;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.utils.files.Files;

import java.io.File;

public class GestureManager extends MCCosmeticsManager<Gesture> {

	public GestureManager(MCCosmeticsPlugin plugin) {
		super(plugin, Gesture.class);

		load(plugin);
	}

	@Override
	public void load(MCCosmeticsPlugin plugin) {
		loadGestures();
		super.load(plugin);
	}

	@Override
	public Gesture build(File file, String node) {
		return new Gesture(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		final var opt = profile.getCosmeticInventory().getEquipped(Gesture.class);
		if(opt.isEmpty() || !(opt.get() instanceof Gesture gesture))
			return;

		final var player = profile.getPlayer();
		PlayerModel model = new PlayerModel(player);
		final var animation = model.getTexture().isSlim() ? gesture.getSlimGesture() : gesture.getDefaultGesture();
		model.playAnimation(animation);
	}

	@Override
	public void unequip(CosmeticProfile profile) {

	}

	private void loadGestures() {

		final String type = CosmeticType.folder(cosmeticClass);
		for(var packFolder : plugin.getConfiguration().getPackFolders()) {
			final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
			if(!confFolder.exists() || !confFolder.isDirectory())
				continue;

			final var files = Files.getAll(confFolder.getAbsolutePath() + System.getProperty("file.separator") + "animations", Lists.newArrayList("bbmodel"));
			for(final var file : files) {
				String key = packFolder.getName();
				PlayerAnimator.api.getAnimationManager().importAnimations(key, file);
			}
		}

	}

}
