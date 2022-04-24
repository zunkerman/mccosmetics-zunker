package io.lumine.cosmetics.compat;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPICompat extends PlaceholderExpansion {

    private final MCCosmeticsPlugin plugin;

    public PlaceholderAPICompat(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        Profile profile = plugin.getProfiles().getProfile(player);

        switch (identifier) {
            case "is_gesturing":
                return profile.getEquippedCosmetics().containsKey("GESTURE") ? "true" : "false";
            case "active_gesture":
                if(!profile.getEquippedCosmetics().containsKey("GESTURE")) {
                    return "None";
                } else {
                    return profile.getEquippedCosmetics().get("GESTURE").getId();
                }
            default:
                return null;
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mcc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lumine Studios";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}
