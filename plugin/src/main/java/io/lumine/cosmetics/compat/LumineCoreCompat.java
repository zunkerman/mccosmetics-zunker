package io.lumine.cosmetics.compat;

import java.util.Optional;
import java.util.UUID;

import io.lumine.core.LumineCore;
import io.lumine.core.players.PlayerProfile;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.plugin.PluginModule;
import io.lumine.utils.promise.Promise;
import io.lumine.utils.storage.players.PlayerStorageAdapter;

public class LumineCoreCompat {

    private final MCCosmeticsPlugin plugin;
    
    public LumineCoreCompat(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public LumineCoreStorageDriver getStorageDriver() {
        return new LumineCoreStorageDriver(plugin);
    }
    
    public class LumineCoreStorageDriver extends PluginModule<MCCosmeticsPlugin> implements PlayerStorageAdapter<Profile> {

        private final LumineCore core;
        
        public LumineCoreStorageDriver(MCCosmeticsPlugin plugin) {
            super(plugin, false);
            this.core = LumineCore.inst();
            
            load(plugin);
        }

        @Override
        public void load(MCCosmeticsPlugin plugin) {}

        @Override
        public void unload() {}
        
        @Override
        public Promise<Optional<Profile>> load(UUID uuid) {
            final Promise<Optional<Profile>> promise = Promise.empty();

            core.getProfiles().getProfile(uuid).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get()));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }

        @Override
        public Promise<Optional<Profile>> loadByName(String name) {
            final Promise<Optional<Profile>> promise = Promise.empty();

            core.getProfiles().getProfile(name).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get()));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }

        @Override
        public Promise save(UUID uuid, Profile arg1) {
            return Promise.completed(true);
        }

        private Optional<Profile> getFromCoreProfile(PlayerProfile coreProfile) {
            var maybeProfile = coreProfile.getMetadata("MCCOSMETICS", Profile.class);
            
            if(maybeProfile.isPresent()) {
                return Optional.of(maybeProfile.get());
            } else {
                return Optional.empty();
            }
        }


    }
    
}
