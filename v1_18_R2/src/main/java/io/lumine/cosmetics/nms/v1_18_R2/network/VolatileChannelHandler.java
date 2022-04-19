package io.lumine.cosmetics.nms.v1_18_R2.network;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.players.Profile;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import org.bukkit.entity.Player;

import java.util.Optional;

public class VolatileChannelHandler extends ChannelDuplexHandler {

	private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
	@Getter private final Player player;

	public VolatileChannelHandler(Player player, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
		this.player = player;
		this.nmsHandler = nmsHandler;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

		if(packet instanceof ClientboundAddPlayerPacket playerPacket) {
			final var entity = nmsHandler.getEntity(player.getWorld(), playerPacket.getEntityId());
			if(entity instanceof Player spawnedPlayer) {
				final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(spawnedPlayer);
				handleSpawn(profile);
			}
		}else if(packet instanceof ClientboundRemoveEntitiesPacket removePacket) {
			for(int id : removePacket.getEntityIds()) {
				final var entity = nmsHandler.getEntity(player.getWorld(), id);
				if(entity instanceof Player despawnedPlayer) {
					final var profile = MCCosmeticsPlugin.inst().getProfiles().getProfile(despawnedPlayer);
					handleDespawn(profile);
				}
			}
		}

		super.write(ctx, packet, promise);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {

		super.channelRead(ctx, packet);
	}

	private void handleSpawn(Profile profile) {

		Optional<Cosmetic> maybeBack = profile.getCosmeticInventory().getEquippedBack();
		if (maybeBack.isPresent() && maybeBack.get() instanceof ItemCosmetic) {
			nmsHandler.getBackHelper().respawnForPlayer(profile.getPlayer(), player);
		}

	}

	private void handleDespawn(Profile profile) {

		Optional<Cosmetic> maybeBack = profile.getCosmeticInventory().getEquippedBack();
		if (maybeBack.isPresent() && maybeBack.get() instanceof ItemCosmetic) {
			nmsHandler.getBackHelper().despawnForPlayer(profile.getPlayer(), player);
		}

	}

}
