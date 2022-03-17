package io.lumine.cosmetics.nms.v1_18_R2.network;

import io.lumine.cosmetics.nms.VolatileCodeHandler;
import io.lumine.cosmetics.nms.v1_18_R2.cosmetic.VolatileHatImpl;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import org.bukkit.entity.Player;

public class VolatileChannelHandler extends ChannelDuplexHandler {

	private final VolatileCodeHandler nmsHandler;
	@Getter private final Player player;

	public VolatileChannelHandler(VolatileCodeHandler nmsHandler, Player player) {
		this.nmsHandler = nmsHandler;
		this.player = player;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

		if(packet instanceof ClientboundSetEquipmentPacket equipmentPacket) {
			packet = ((VolatileHatImpl) nmsHandler.getHatHelper()).replacePacket(player, equipmentPacket);
		}

		super.write(ctx, packet, promise);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {

		super.channelRead(ctx, packet);
	}
}
