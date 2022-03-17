package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.VolatileHatHelper;
import io.lumine.cosmetics.players.Profile;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VolatileHatImpl implements VolatileHatHelper {

    @Getter private final MCCosmeticsPlugin plugin;
    private final VolatileCodeEnabled_v1_18_R2 nmsHandler;

    public VolatileHatImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
        this.plugin = plugin;
        this.nmsHandler = nmsHandler;
    }
    
    @Override
    public void applyHatPacket(CosmeticProfile profile) {
        if(profile == null)
            return;
        Player player = profile.getPlayer();
        Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquippedHat();

        if(cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic hat))
            return;

        var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic());

        List<Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> slots = new ArrayList<>();
        slots.add(Pair.of(EquipmentSlot.HEAD, nmsHat));
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), slots);

        nmsHandler.broadcast(player.getWorld(), equipmentPacket);

    }

    public ClientboundSetEquipmentPacket replacePlayerPacket(Player owner, ClientboundAddPlayerPacket playerPacket) {
        Player player = (Player) nmsHandler.getEntity(owner.getWorld(), playerPacket.getEntityId());

        Profile profile = plugin.getProfiles().getProfile(player);
        Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquippedHat();

        if(cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic hat))
            return null;

        var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic());

        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(playerPacket.getEntityId(), new ArrayList<>());
        equipmentPacket.getSlots().add(Pair.of(EquipmentSlot.HEAD, nmsHat));

        return equipmentPacket;

    }

    public ClientboundSetEquipmentPacket replaceEquipmentPacket(Player owner, ClientboundSetEquipmentPacket equipmentPacket) {

        Entity entity = nmsHandler.getEntity(owner.getWorld(), equipmentPacket.getEntity());
        if(!(entity instanceof Player player))
            return equipmentPacket;

        Profile profile = plugin.getProfiles().getProfile(player);
        Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquippedHat();

        if(cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic hat))
            return equipmentPacket;

        var nmsHat = CraftItemStack.asNMSCopy(hat.getCosmetic());

        equipmentPacket.getSlots().removeIf(pair -> pair.getFirst() == EquipmentSlot.HEAD);
        equipmentPacket.getSlots().add(Pair.of(EquipmentSlot.HEAD, nmsHat));

        return equipmentPacket;
    }

}
