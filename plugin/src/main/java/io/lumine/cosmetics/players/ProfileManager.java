package io.lumine.cosmetics.players;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.storage.players.adapters.file.JsonPlayerStorageAdapter;
import io.lumine.utils.storage.players.PlayerRepository;

public class ProfileManager extends PlayerRepository<MCCosmeticsPlugin,CosmeticProfile> {

    public ProfileManager(MCCosmeticsPlugin plugin) {
        super(plugin, CosmeticProfile.class, new JsonPlayerStorageAdapter<>(plugin,CosmeticProfile.class));
    }

    @Override
    public CosmeticProfile createProfile(UUID id, String name) {
        return new CosmeticProfile(id,name);
    }

    @Override
    public void initProfile(CosmeticProfile profile, Player player) {
        profile.initialize(player);
    }

    @Override
    public void unloadProfile(CosmeticProfile profile, Player player) {}

}
