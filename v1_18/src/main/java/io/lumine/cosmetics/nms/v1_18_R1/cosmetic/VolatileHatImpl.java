package io.lumine.cosmetics.nms.v1_18_R1.cosmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R1;
import io.lumine.cosmetics.nms.cosmetic.VolatileHatHelper;
import io.lumine.utils.protocol.Protocol;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;

public class VolatileHatImpl implements VolatileHatHelper {

    @Getter private final MCCosmeticsPlugin plugin;
    
    @Getter private final MCCosmeticsPlugin plugin;
    private final VolatileCodeEnabled_v1_18_R1 nmsHandler;

    public VolatileHatImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R1 nmsHandler) {
        this.plugin = plugin;
        this.nmsHandler = nmsHandler;
    }
    
    @Override
    public void applyHatPacket(CosmeticProfile profile) {
        try {
            if (profile == null)
                return;
            Player player = profile.getPlayer();
            Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquippedHat();

            if (cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic hat))
                return;

            var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic());

            List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> slots = new ArrayList<>();
            slots.add(Pair.of(EquipmentSlot.HEAD, nmsHat));
            ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), slots);

            nmsHandler.broadcast(player.getWorld(), equipmentPacket);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
