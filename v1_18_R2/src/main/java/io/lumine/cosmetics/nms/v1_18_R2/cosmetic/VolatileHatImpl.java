package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.gestures.Gesture;
import io.lumine.cosmetics.managers.hats.Hat;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.cosmetics.players.Profile;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VolatileHatImpl implements VolatileEquipmentHelper {

    @Getter private final MCCosmeticsPlugin plugin;
    private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
    private final Map<Integer, Player> playerTracker = Maps.newConcurrentMap();

    public VolatileHatImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
        this.plugin = plugin;
        this.nmsHandler = nmsHandler;
    }
    
    @Override
    public void apply(CosmeticProfile profile) {

        if (profile == null)
            return;

        Player player = profile.getPlayer();

        final var maybeEquipped = profile.getEquipped(Hat.class);
        if(maybeEquipped.isEmpty()) {
            return;
        }
        var opt = maybeEquipped.get().getCosmetic();
        
        if(!(opt instanceof Hat hat))
            return;

        var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic(maybeEquipped.get().getVariant()));

        playerTracker.put(player.getEntityId(), player);

        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), List.of(Pair.of(EquipmentSlot.HEAD, nmsHat)));

        nmsHandler.broadcastAroundAndSelf(player, equipmentPacket);
    }

    @Override
    public void unapply(CosmeticProfile profile) {
        final var nmsPlayer = ((CraftPlayer) profile.getPlayer()).getHandle();
        final var item = nmsPlayer.getItemBySlot(EquipmentSlot.HEAD);
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(nmsPlayer.getId(), List.of(Pair.of(EquipmentSlot.HEAD, item)));
        nmsHandler.broadcastAroundAndSelf(nmsPlayer.getBukkitEntity(), equipmentPacket);
    }

    @Override
    public boolean read(Player sender, Object packet, boolean isCanceled) {
        if(packet instanceof ServerboundAcceptTeleportationPacket) {
            final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(sender);
            if(profile == null || profile.isHidden(Hat.class))
                return true;

            final var list = handleSpawn(profile);
            if(list == null)
                return true;

            final var connection = ((CraftPlayer) sender).getHandle().connection;
            for(Object obj : list) {
                connection.send((Packet<?>) obj);
            }
        }
        return true;
    }

    @Override
    public List<Object> write(Player receiver, Object packet) {
        if(packet instanceof ClientboundAddPlayerPacket playerPacket) {
            int id = playerPacket.getEntityId();
            Profile profile = getProfile(receiver, id);
            if(profile != null && !profile.isHidden(Hat.class))
                return handleSpawn(profile);
        }else if(packet instanceof ClientboundSetEquipmentPacket equipmentPacket) {
            int id = equipmentPacket.getEntity();
            Profile profile = getProfile(receiver, id);
            if(profile != null && !profile.isHidden(Hat.class))
                return handleSpawn(profile);
        }

        return null;
    }

    private Profile getProfile(Player receiver, int id) {
        final var entity = nmsHandler.getEntity(receiver.getWorld(), id);
        if(!(entity instanceof Player player))
            return null;
        return plugin.getProfiles().getProfile(player);
    }

    public List<Object> handleSpawn(Profile profile) {
        final var maybeEquipped = profile.getEquipped(Hat.class);
        if(maybeEquipped.isEmpty()) {
            return null;
        }
        var equip = maybeEquipped.get();
        var opt = equip.getCosmetic();
        
        if(!(opt instanceof ItemCosmetic hat))
            return null;

        final var player = profile.getPlayer();
        final var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic(equip.getVariant()));
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), List.of(Pair.of(EquipmentSlot.HEAD, nmsHat)));

        return List.of(equipmentPacket);
    }

}
