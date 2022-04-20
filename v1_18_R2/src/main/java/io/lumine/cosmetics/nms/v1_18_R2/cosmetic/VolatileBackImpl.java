package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.back.BackAccessory;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import io.lumine.cosmetics.players.Profile;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class VolatileBackImpl extends AbstractCosmeticHandler implements VolatileBackHelper {

	@Getter
	private final MCCosmeticsPlugin plugin;
	private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
	private final Map<Player, ArmorStand> activeProfile = Maps.newConcurrentMap();
	private final Map<Integer, Player> playerTracker = Maps.newConcurrentMap();

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

		ArmorStand stand = activeProfile.get(player);
		if(stand == null) {
			playerTracker.put(player.getEntityId(), player);
			stand = new ArmorStand(EntityType.ARMOR_STAND, ((CraftWorld) player.getWorld()).getHandle());
			stand.setMarker(true);
			stand.setInvisible(true);
			stand.setSilent(true);

			activeProfile.put(player, stand);

			ClientboundAddMobPacket mobPacket = new ClientboundAddMobPacket(stand);
			ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(stand.getId(), stand.getEntityData(), true);
			ClientboundSetPassengersPacket passengersPacket = createPassengerPacket(player.getEntityId(), stand.getId());

			nmsHandler.broadcastAround(player, mobPacket, dataPacket, passengersPacket);
		}

		stand.setItemSlot(EquipmentSlot.HEAD, nmsBack);

		ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getId(), List.of(Pair.of(EquipmentSlot.HEAD, nmsBack)));

		nmsHandler.broadcastAround(player, equipmentPacket);
	}

	@Override
	public void read(Player sender, Packet<?> packet) {
		if(packet instanceof ServerboundMovePlayerPacket) {
			final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(sender);
			handleRotate(profile);
		}
	}

	@Override
	public List<Packet<?>> write(Player receiver, Packet<?> packet) {
		if(packet instanceof ClientboundAddPlayerPacket playerPacket) {
			int id = playerPacket.getEntityId();
			if(playerTracker.containsKey(id)) {
				final var spawnedPlayer = playerTracker.get(id);
				final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(spawnedPlayer);
				return handleSpawn(profile);
			}
		}else if(packet instanceof ClientboundRemoveEntitiesPacket removePacket) {
			for(int id : removePacket.getEntityIds()) {
				if(playerTracker.containsKey(id)) {
					return handleDespawn(playerTracker.get(id));
				}
			}
		}

		return null;
	}

	private void handleRotate(Profile profile) {
		if(!hasBack(profile))
			return;

		final var wearer = profile.getPlayer();
		final var nmsPlayer = ((CraftPlayer) wearer).getHandle();
		final var stand = activeProfile.get(wearer);

		ClientboundRotateHeadPacket packet = new ClientboundRotateHeadPacket(stand, VolatileBackHelper.toByte(nmsPlayer.getYRot()));
		nmsHandler.broadcastAround(wearer, packet);
	}

	private List<Packet<?>> handleSpawn(Profile profile) {
		if(!hasBack(profile))
			return null;

		final var wearer = profile.getPlayer();
		final var stand = activeProfile.get(wearer);

		ClientboundAddMobPacket mobPacket = new ClientboundAddMobPacket(stand);
		ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(stand.getId(), stand.getEntityData(), true);
		ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(stand.getId(), List.of(Pair.of(EquipmentSlot.HEAD, stand.getItemBySlot(EquipmentSlot.HEAD))));
		ClientboundSetPassengersPacket passengersPacket = createPassengerPacket(wearer.getEntityId(), stand.getId());

		return List.of(mobPacket, dataPacket, equipmentPacket, passengersPacket);
	}

	private List<Packet<?>> handleDespawn(Player player) {
		final var stand = activeProfile.get(player);
		if(stand == null)
			return null;
		ClientboundRemoveEntitiesPacket removePacket = new ClientboundRemoveEntitiesPacket(stand.getId());
		return List.of(removePacket);
	}

	private boolean hasBack(Profile profile) {
		if(profile == null)
			return false;

		Optional<Cosmetic> maybeBack = profile.getCosmeticInventory().getEquipped(BackAccessory.class);
		return maybeBack.isPresent() && maybeBack.get() instanceof ItemCosmetic && activeProfile.containsKey(profile.getPlayer());
	}

	private ClientboundSetPassengersPacket createPassengerPacket(int mount, int... driver) {
		FriendlyByteBuf bb = new FriendlyByteBuf(Unpooled.buffer());
		bb.writeVarInt(mount);
		bb.writeVarIntArray(driver);
		return new ClientboundSetPassengersPacket(bb);
	}

}