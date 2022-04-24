package io.lumine.cosmetics.players;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.events.CosmeticPlayerLoadedEvent;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.utils.Events;
import io.lumine.utils.logging.Log;
import io.lumine.utils.storage.players.PlayerRepository;
import io.lumine.utils.storage.players.adapters.file.JsonPlayerStorageAdapter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.Optional;
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

        Events.subscribe(PlayerGameModeChangeEvent.class).handler(event -> {
           if(event.getNewGameMode().equals(GameMode.SPECTATOR)){
               Optional<Profile> profile = this.getProfile(event.getPlayer().getUniqueId());

               if(profile.isEmpty()){
                   return;
               }

                profile.get().getEquipped().forEach(((aClass, equippedCosmetic) -> {
                    equippedCosmetic.getCosmetic().getManager().unequip(profile.get());
                }));

           }
        });
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
