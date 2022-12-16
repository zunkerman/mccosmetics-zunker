package io.lumine.cosmetics.managers.gestures;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ticxo.playeranimator.api.PlayerAnimator;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.Events;
import io.lumine.utils.files.Files;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public class GestureManager extends MCCosmeticsManager<Gesture> {

	@Getter
	private final Map<Player, CustomPlayerModel> ticking = Maps.newConcurrentMap();

	public GestureManager(MCCosmeticsPlugin plugin) {
		super(plugin, Gesture.class);

		load(plugin);
	}

	@Override
	public void load(MCCosmeticsPlugin plugin) {
		loadGestures();
		super.load(plugin);

		Events.subscribe(PlayerJoinEvent.class).handler(event -> PlayerAnimator.api.injectPlayer(event.getPlayer())).bindWith(this);
		Events.subscribe(PlayerQuitEvent.class).handler(event -> {
			PlayerAnimator.api.removePlayer(event.getPlayer());
			quit(event.getPlayer(), null);
		}).bindWith(this);
		Events.subscribe(PlayerToggleSneakEvent.class).handler(event -> quit(event.getPlayer(), QuitMethod.SNEAK)).bindWith(this);
		Events.subscribe(PlayerJumpEvent.class).handler(event -> quit(event.getPlayer(), QuitMethod.JUMP)).bindWith(this);

	}

	public void quit(Player player, QuitMethod method) {
		Profile profile = this.plugin.getProfiles().getProfile(player);
		Optional<EquippedCosmetic> maybeEquipped = profile.getEquipped(Gesture.class);
		if (!maybeEquipped.isEmpty()) {
			Cosmetic opt = maybeEquipped.get().getCosmetic();
			if (opt instanceof Gesture) {
				Gesture gesture = (Gesture)opt;
				stopGestureSound(profile.getPlayer(), gesture);
			}
		}
		if (!this.ticking.containsKey(player))
			return;
		CustomPlayerModel model = this.ticking.get(player);
		model.stopAnimation(method);
	}

	@Override
	public Gesture build(File file, String node) {
		return new Gesture(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		// Gestures aren't really equipped
	}

	@Override
	public void unequip(CosmeticProfile profile) {
		stopGesture(profile);
	}

	public void playGesture(CosmeticProfile profile) {
		if (!profile.getPlayer().isOnGround()) return;

		if (ticking.containsKey(profile.getPlayer())) {
			return;
		}

		final var maybeEquipped = profile.getEquipped(Gesture.class);

		if (maybeEquipped.isEmpty()) {
			return;
		}
		var opt = maybeEquipped.get().getCosmetic();

		if (!(opt instanceof Gesture gesture)) {
			return;
		}

		final var player = profile.getPlayer();
		CustomPlayerModel model = new CustomPlayerModel(player, gesture.getQuitMethod(), gesture.isCanLook(), () -> profile.unequip(gesture));
		ticking.put(player, model);
		final var animation = model.getTexture().isSlim() ? gesture.getSlimGesture() : gesture.getDefaultGesture();
		model.playAnimation(animation);
		playGestureSound(player, gesture);

		((VolatileEquipmentHelper) getNMSHelper()).apply(profile);
	}

	public void stopGesture(CosmeticProfile profile) {
		Optional<EquippedCosmetic> maybeEquipped = profile.getEquipped(Gesture.class);
		if (!maybeEquipped.isEmpty()) {
			Cosmetic opt = maybeEquipped.get().getCosmetic();
			if (opt instanceof Gesture) {
				Gesture gesture = (Gesture) opt;
				stopGestureSound(profile.getPlayer(), gesture);
			}
		}
		CustomPlayerModel model = this.ticking.remove(profile.getPlayer());
		if (model == null)
			return;
		model.despawn();
		((VolatileEquipmentHelper) getNMSHelper()).unapply(profile);
	}

	private void loadGestures() {
		PlayerAnimator.api.getAnimationManager().clearRegistry();

		final String type = CosmeticType.folder(cosmeticClass);
		for (var packFolder : plugin.getConfiguration().getPackFolders()) {
			final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
			if (!confFolder.exists() || !confFolder.isDirectory()) {
				continue;
			}

			final var files = Files.getAll(confFolder.getAbsolutePath() + System.getProperty("file.separator") + "animations", Lists.newArrayList("bbmodel"));
			for (final var file : files) {
				String key = packFolder.getName();
				PlayerAnimator.api.getAnimationManager().importAnimations(key, file);
			}
		}

	}

	private void playGestureSound(Player player, Gesture gesture) {
		player.playSound(player.getLocation(), gesture.getSoundName(), 1.0F, 1.0F);
		double maxDist = 13.0D;
		for (Player nearPlayer : Bukkit.getOnlinePlayers()) {
			if (nearPlayer.getLocation().distance(player.getLocation()) <= maxDist)
				nearPlayer.playSound(player.getLocation(), gesture.getSoundName(), 1.0F, 1.0F);
		}
	}

	private void stopGestureSound(Player player, Gesture gesture) {
		player.stopSound(gesture.getSoundName());
		double maxDist = 20.0D;
		for (Player nearPlayer : Bukkit.getOnlinePlayers()) {
			if (nearPlayer.getLocation().distance(player.getLocation()) <= maxDist)
				nearPlayer.stopSound(gesture.getSoundName());
		}
	}
}