package io.lumine.cosmetics.nms.v1_18_R1.cosmetic;

import java.util.List;

import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.nms.cosmetic.VolatileHatHelper;
import io.lumine.utils.protocol.Protocol;
import lombok.Getter;
import net.minecraft.world.entity.EquipmentSlot;

public class VolatileHatImpl implements VolatileHatHelper {

    @Getter private final MCCosmeticsPlugin plugin;
    
    public VolatileHatImpl(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void applyHatPacket(CosmeticProfile profile) {
        if(profile == null) {
            return;
        }
        final var player = profile.getPlayer();
        final var packet = Protocol.manager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        
        packet.getEntityModifier(player.getWorld()).write(0, player);

        /*
        if(profile.getHat().isPresent()) {
            profile.setHatIsActive(true);
            writeHeadItem(packet, profile.getEquippedHat());
            Protocol.broadcastPacket(packet);
        } else if(profile.getHatIsActive()) {
            writeHeadItem(packet, player.getInventory().getHelmet());
            Protocol.broadcastPacket(packet);
        }*/
    }

    public boolean writeHeadItem(PacketContainer packet, ItemStack item) {
        List<Pair<EquipmentSlot,net.minecraft.world.item.ItemStack>> slots = (List<Pair<EquipmentSlot,net.minecraft.world.item.ItemStack>>) packet.getModifier().read(1);
        List<Pair<EquipmentSlot,net.minecraft.world.item.ItemStack>> newSlots = Lists.newArrayList();

        boolean foundHead = false;
        for(Pair<EquipmentSlot,net.minecraft.world.item.ItemStack> pair : slots) {
            final EquipmentSlot slot = pair.getFirst();
            
            if(slot == EquipmentSlot.HEAD) {
                foundHead = true;
                newSlots.add(Pair.of(pair.getFirst(), CraftItemStack.asNMSCopy(item)));
            } else {
                newSlots.add(pair);
            }
        }
        if(!foundHead) {
            newSlots.add(Pair.of(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(item)));
        }
        packet.getModifier().write(1, newSlots);
        return true;
    }

}
