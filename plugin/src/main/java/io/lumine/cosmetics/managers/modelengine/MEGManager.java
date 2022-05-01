package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.StateProperty;
import com.ticxo.modelengine.api.model.ModeledEntity;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.logging.MCLogger;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.utils.Events;
import io.lumine.utils.Schedulers;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;

public class MEGManager extends MCCosmeticsManager<MEGAccessory> {

	public MEGManager(MCCosmeticsPlugin plugin) {
		super(plugin, MEGAccessory.class);

		load(plugin);
	}

	@Override
	public void load(MCCosmeticsPlugin plugin) {
		super.load(plugin);

		Events.subscribe(PlayerDeathEvent.class)
            .handler(event -> {
                final var player = event.getEntity();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile.getEquipped(MEGAccessory.class).isPresent()) {
                    unequip(profile);
                }
            })
            .bindWith(this);

        Events.subscribe(PlayerRespawnEvent.class)
            .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                Schedulers.sync().runLater(() -> {
                    if(profile.getEquipped(MEGAccessory.class).isPresent()) {
                        equip(profile);
                    }
                }, 5);
            })
            .bindWith(this);
		
		Events.subscribe(PlayerChangedWorldEvent.class)
		    .handler(event -> {
                final var player = event.getPlayer();
                final var profile = plugin.getProfiles().getProfile(player);
                
                if(profile.getEquipped(MEGAccessory.class).isPresent()) {
                    unequip(profile);
                    
                    Schedulers.sync().runLater(() -> {
                        equip(profile);
                    }, 5);
                }
		    })
		    .bindWith(this);
		
		Events.subscribe(PlayerQuitEvent.class)
			.handler(event -> {
				final Player player = event.getPlayer();
				getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
					if(maybeProfile.isEmpty()) {
						return;
					}
					unequip(maybeProfile.get());
				});
			}).bindWith(this);
	}
	
	@Override
	public void unload() {
	    getPlugin().getProfiles().getKnownProfiles().forEach(profile -> {
	        unequip(profile);
	    });
	    super.unload();
	}

	@Override
	public MEGAccessory build(File file, String node) {
		return new MEGAccessory(this, file, node);
	}

	@Override
	public void equip(CosmeticProfile profile) {
		final var maybeEquipped = profile.getEquipped(MEGAccessory.class);
		if(maybeEquipped.isEmpty()) {
		    return;
		}
		var opt = maybeEquipped.get().getCosmetic();
		
		if(!(opt instanceof MEGAccessory meg)) {
			return;
		}

		final var blueprint = ModelEngineAPI.getModelBlueprint(meg.getModelId());
		if(blueprint == null)
			return;

		final var animation = blueprint.getAnimation(meg.getState());
		if(animation == null)
			return;

		final var player = profile.getPlayer();
		final var activeModel = ModelEngineAPI.createActiveModel(meg.getModelId());
		final var property = new StateProperty(meg.getState(), animation, 1, 0, 1);
		property.setLoop(true);
		property.setOverride(true);
		activeModel.addState(property);
		activeModel.setDamageTint(false);
		activeModel.setAnimationMode(meg.getMode());

		ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(player.getUniqueId());
		if(modeledEntity != null) {
			modeledEntity.clearModels();
			modeledEntity.getAllActiveModel().clear();
		}

		modeledEntity = new MEGModel(player, new FakeEntity(player, meg.getOffset(), meg.getAnchor()));
		modeledEntity.addActiveModel(activeModel);
		modeledEntity.setInvisible(false);
		modeledEntity.addPlayerAsync(player);
	}

	public void unequip(CosmeticProfile profile) {
        final var maybeEquipped = profile.getEquipped(MEGAccessory.class);
        if(maybeEquipped.isEmpty()) {
            return;
        }
        var opt = maybeEquipped.get().getCosmetic();
        
        if(!(opt instanceof MEGAccessory meg)) {
            return;
        }

		final var player = profile.getPlayer();
		ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(player.getUniqueId());
		if(modeledEntity != null) {
			modeledEntity.clearModels();
			modeledEntity.getAllActiveModel().clear();
			ModelEngineAPI.api.getModelManager().removeModeledEntity(player.getUniqueId());
		}
	}

}
