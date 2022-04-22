package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.manager.HideableCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.gestures.Gesture;
import io.lumine.cosmetics.managers.gestures.GestureManager;
import io.lumine.cosmetics.managers.gestures.QuitMethod;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class VolatileGestureImpl implements VolatileEquipmentHelper {

	private static final List<Pair<EquipmentSlot, ItemStack>> empty = new ArrayList<>();

	static {
		for(final var slot : EquipmentSlot.values())
			empty.add(Pair.of(slot, ItemStack.EMPTY));
	}

	@Getter
	private final MCCosmeticsPlugin plugin;
	private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
	private final Set<Player> activeProfile = Sets.newConcurrentHashSet();

	private Horse horse;

	public VolatileGestureImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
		this.plugin = plugin;
		this.nmsHandler = nmsHandler;
	}

	@Override
	public void apply(CosmeticProfile profile) {
		if (profile == null)
			return;
		Player player = profile.getPlayer();
		if(activeProfile.contains(player))
			return;

		Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquipped(Gesture.class);
		if (cosmetic.isEmpty() || !(cosmetic.get() instanceof Gesture gesture))
			return;

		getHorsed(player);
		player.setInvisible(true);
		for(final var value : profile.getCosmeticInventory().getEquipped().values()) {
			final var manager = value.getManager();
			if(!(manager instanceof HideableCosmetic hide))
				continue;
			hide.hide(profile, gesture);
		}
	}

	@Override
	public void unapply(CosmeticProfile profile) {
		if (profile == null)
			return;
		Player player = profile.getPlayer();
		if(!activeProfile.contains(player))
			return;

		activeProfile.remove(player);
		nmsHandler.broadcast(player, new ClientboundRemoveEntitiesPacket(horse.getId()));

		player.setInvisible(false);
		for(final var value : profile.getCosmeticInventory().getEquipped().values()) {
			final var manager = value.getManager();
			if(!(manager instanceof HideableCosmetic hide))
				continue;
			hide.show(profile);
		}
	}

	private ClientboundSetPassengersPacket createPassengerPacket(int mount, int... driver) {
		FriendlyByteBuf bb = new FriendlyByteBuf(Unpooled.buffer());
		bb.writeVarInt(mount);
		bb.writeVarIntArray(driver);
		return new ClientboundSetPassengersPacket(bb);
	}

	private void getHorsed(Player player) {
		final var nmsPlayer = ((CraftPlayer) player).getHandle();

		if(horse == null) {
			horse = new Horse(EntityType.HORSE, ((CraftWorld) player.getWorld()).getHandle());
			horse.setInvisible(true);
			horse.setHealth(0);
			horse.getAttribute(CraftAttributeMap.toMinecraft(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(0);
		}
		horse.setPos(nmsPlayer.getX(), nmsPlayer.getY() - horse.getPassengersRidingOffset() - nmsPlayer.getMyRidingOffset(), nmsPlayer.getZ());

		activeProfile.add(player);

		ClientboundAddMobPacket mobPacket = new ClientboundAddMobPacket(horse);
		ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(horse.getId(), horse.getEntityData(), true);
		ClientboundUpdateAttributesPacket attributesPacket = new ClientboundUpdateAttributesPacket(horse.getId(), horse.getAttributes().getSyncableAttributes());
		ClientboundSetPassengersPacket passengersPacket = createPassengerPacket(horse.getId(), nmsPlayer.getId());

		nmsHandler.broadcast(player, mobPacket, dataPacket, attributesPacket, passengersPacket);
	}

	@Override
	public void read(Player sender, Object packet) {
		final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(sender);
		if(profile == null)
			return;
		final var opt = profile.getCosmeticInventory().getEquipped(Gesture.class);
		if(opt.isEmpty() || !(opt.get() instanceof Gesture gesture))
			return;

		if(packet instanceof ServerboundPlayerInputPacket inputPacket) {
			final var manager = (GestureManager) gesture.getManager();
			if(inputPacket.isShiftKeyDown())
				manager.quit(sender, QuitMethod.SNEAK);
			if(inputPacket.isJumping())
				manager.quit(sender, QuitMethod.JUMP);
		}
	}
}
