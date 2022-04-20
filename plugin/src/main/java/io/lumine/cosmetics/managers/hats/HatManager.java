package io.lumine.cosmetics.managers.hats;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.events.CosmeticPlayerLoadedEvent;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.Events;
import io.lumine.utils.events.extra.ArmorEquipEvent; 
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;

public class HatManager extends MCCosmeticsManager<Hat> {
    
    public HatManager(MCCosmeticsPlugin plugin) {
        super(plugin, Hat.class);   
        
        load(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        super.load(plugin);

        Events.subscribe(CosmeticPlayerLoadedEvent.class)
                .handler(event -> {
                    final var profile = event.getProfile();
                    equip(profile);
                }).bindWith(this);

        Events.subscribe(InventoryCloseEvent.class)
                .handler(event -> {
                    final Player player = (Player) event.getPlayer();
                    getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
                        if(maybeProfile.isEmpty())
                            return;
                        final Profile profile = maybeProfile.get();
                        equip(profile);
                    });
                }).bindWith(this);

        Events.subscribe(PlayerRespawnEvent.class)
                .handler(event -> {
                    final Player player = event.getPlayer();
                    getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
                        if(maybeProfile.isEmpty())
                            return;
                        final Profile profile = maybeProfile.get();
                        equip(profile);
                    });
                }).bindWith(this);

        Events.subscribe(ArmorEquipEvent.class)
                .handler(event -> {
                    final Player player = event.getPlayer();
                    getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
                        if(maybeProfile.isEmpty()) {
                            return;
                        }
                        final Profile profile = maybeProfile.get();
                        equip(profile);
                    });
                }).bindWith(this);
    }

    @Override
    public Hat build(File file, String node) {
        return new Hat(this, file, node);
    }

    @Override
    public void equip(CosmeticProfile profile) {
        ((VolatileEquipmentHelper) getPlugin().getVolatileCodeHandler().getCosmeticHelper(Hat.class)).apply(profile);
    }
}
