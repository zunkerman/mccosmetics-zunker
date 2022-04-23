package io.lumine.cosmetics.players;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.events.CosmeticPlayerLoadedEvent;
import io.lumine.utils.Events;
import io.lumine.utils.logging.Log;
import io.lumine.utils.storage.players.PlayerRepository;
import io.lumine.utils.storage.players.adapters.file.JsonPlayerStorageAdapter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ProfileManager extends PlayerRepository<MCCosmeticsPlugin,Profile> {

    public ProfileManager(MCCosmeticsPlugin plugin) {
        super(plugin, Profile.class);
        
        switch(plugin.getConfiguration().getStorageType()) {
            case LUMINE:
                this.initialize(plugin.getCompatibility().getLumineCore().get().getStorageDriver());
                break;
            default:
                this.initialize(new JsonPlayerStorageAdapter<>(plugin,Profile.class));
                break;
        }
    }

    @Override
    public Profile createProfile(UUID id, String name) {
        return new Profile(id,name);
    }

    @Override
    public void initProfile(Profile profile, Player player) {
        profile.initialize(player);
        Events.call(new CosmeticPlayerLoadedEvent(player,profile));
    }

    @Override
    public void unloadProfile(Profile profile, Player player) {}

    public void reloadAllCosmetics() {
        this.getKnownProfiles().forEach(profile -> profile.reloadCosmetics());
    }

}
