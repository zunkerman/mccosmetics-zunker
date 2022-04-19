package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.back.BackAccessory;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VolatileBackImpl implements VolatileBackHelper {

	@Getter private final MCCosmeticsPlugin plugin;
	private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
	private final Map<Player, ArmorStand> activeProfile = new HashMap<>();

	public VolatileBackImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
		this.plugin = plugin;
		this.nmsHandler = nmsHandler;
	}

	@Override
	public void applyBackPacket(CosmeticProfile profile) {

		if (profile == null)
			return;
		Player player = profile.getPlayer();
		Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquipped(BackAccessory.class);

		if (cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic back))
			return;

		var nmsBack = CraftItemStack.asNMSCopy(back.getCosmetic());

		ArmorStand stand = new ArmorStand(EntityType.ARMOR_STAND, ((CraftWorld) player.getWorld()).getHandle());
		stand.setMarker(true);
		stand.setInvisible(true);
		stand.setSilent(true);
		stand.setItemSlot(EquipmentSlot.HEAD, nmsBack);

		activeProfile.put(player, stand);

		ClientboundAddMobPacket mobPacket = new ClientboundAddMobPacket(stand);
		ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getId(), List.of(Pair.of(EquipmentSlot.HEAD, nmsBack)));

		FriendlyByteBuf bb = new FriendlyByteBuf(Unpooled.buffer());
		bb.writeVarInt(player.getEntityId());
		bb.writeVarIntArray(new int[] { stand.getId() });
		ClientboundSetPassengersPacket passengersPacket = new ClientboundSetPassengersPacket(bb);

		nmsHandler.broadcast(player.getWorld(), mobPacket, equipmentPacket, passengersPacket);

	}

	public void respawnForPlayer(Player wearer, Player observer) {
		if(!activeProfile.containsKey(wearer))
			return;

		final var stand = activeProfile.get(wearer);

		ClientboundAddMobPacket mobPacket = new ClientboundAddMobPacket(stand);
		ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getId(), List.of(Pair.of(EquipmentSlot.HEAD, stand.getItemBySlot(EquipmentSlot.HEAD))));

		FriendlyByteBuf bb = new FriendlyByteBuf(Unpooled.buffer());
		bb.writeVarInt(wearer.getEntityId());
		bb.writeVarIntArray(new int[] { stand.getId() });
		ClientboundSetPassengersPacket passengersPacket = new ClientboundSetPassengersPacket(bb);

		nmsHandler.broadcast(observer, mobPacket, equipmentPacket, passengersPacket);

	}

	public void despawnForPlayer(Player wearer, Player observer) {
		if(!activeProfile.containsKey(wearer))
			return;

		final var stand = activeProfile.get(wearer);
		ClientboundRemoveEntitiesPacket removePacket = new ClientboundRemoveEntitiesPacket(stand.getId());
		nmsHandler.broadcast(observer, removePacket);
	}

}
