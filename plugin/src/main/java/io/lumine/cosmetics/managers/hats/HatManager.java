package io.lumine.cosmetics.managers.hats;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.events.CosmeticPlayerLoadedEvent;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.Events;
import io.lumine.utils.protocol.Protocol;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.File;

public class HatManager extends MCCosmeticsManager<Hat> {
    
    public HatManager(MCCosmeticsPlugin plugin) {
        super(plugin, Hat.class);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        super.load(plugin);

        Events.subscribe(CosmeticPlayerLoadedEvent.class)
                .handler(event -> {
                    final var profile = event.getProfile();

                    //if(profile.getHat().isPresent()) {
                    //    equipHat(profile);
                    //}
                }).bindWith(this);

        Events.subscribe(InventoryCloseEvent.class)
                .handler(event -> {
                    final Player player = (Player) event.getPlayer();
                    getProfiles().awaitProfile(player).thenAcceptAsync(maybeProfile -> {
                        if(maybeProfile.isEmpty()) {
                            return;
                        }
                        final Profile profile = maybeProfile.get();
                        //if(profile.getHat().isPresent()) {
                        //    profile.setHatIsActive(true);
                        //    Schedulers.async().runLater(() -> this.equipHat(profile), 1);
                        // }
                    });
                }).bindWith(this);

        Protocol.subscribe(PacketType.Play.Server.ENTITY_EQUIPMENT)
                .handler(event -> {
                    final PacketContainer packet = event.getPacket();
                    final Entity entity = packet.getEntityModifier(event.getPlayer().getWorld()).read(0);

                    if(!(entity instanceof Player)) {
                        return;
                    }
                    final Player player = (Player) entity;
                    final Profile profile = getProfiles().getProfile(player);

                    if(profile == null) {
                        return;
                    }
                    //if(profile.getHat().isPresent()) {
                    //    writeHeadItem(packet, profile.getEquippedHat());
                    //}
                }).bindWith(this);

        Events.subscribe(InventoryClickEvent.class)
                .handler(event -> {
                    if(event.getRawSlot() == 5) {
                        final Player player = (Player) event.getWhoClicked();
                        final Profile profile = getProfiles().getProfile(player);

                        //if(profile.getHat().isPresent() && profile.getHatIsActive()) {
                        //    player.updateInventory();
                        //    profile.setHatIsActive(false);
                        //}
                    }
                }).bindWith(this);
    }

    @Override
    public Hat build(File file, String node) {
        return new Hat(file, node);
    }

    @Override
    public void equip(Profile profile) {
        getPlugin().getVolatileCodeHandler().getHatHelper().applyHatPacket(profile);
    }
}
