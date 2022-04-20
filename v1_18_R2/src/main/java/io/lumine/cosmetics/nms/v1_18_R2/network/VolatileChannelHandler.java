package io.lumine.cosmetics.nms.v1_18_R2.network;

import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.v1_18_R2.cosmetic.AbstractCosmeticHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VolatileChannelHandler extends ChannelDuplexHandler {

	private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
	@Getter private final Player player;

	public VolatileChannelHandler(Player player, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
		this.player = player;
		this.nmsHandler = nmsHandler;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object obj, ChannelPromise promise) {

		try {
			if (!(obj instanceof Packet<?> packet)) {
				super.write(ctx, obj, promise);
				return;
			}

			List<Packet<?>> packets = new ArrayList<>();

			final var hat = ((AbstractCosmeticHandler) nmsHandler.getHatHelper()).write(player, packet);
			if (hat != null)
				packets.addAll(hat);

			final var back = ((AbstractCosmeticHandler) nmsHandler.getBackHelper()).write(player, packet);
			if (back != null)
				packets.addAll(back);

			if(!packets.contains(obj))
				super.write(ctx, obj, promise);
			packets.remove(obj);

			for (var p : packets) {
				super.write(ctx, p, promise.channel().newPromise());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {

		if(obj instanceof Packet<?> packet) {
			((AbstractCosmeticHandler) nmsHandler.getHatHelper()).read(player, packet);
			((AbstractCosmeticHandler) nmsHandler.getBackHelper()).read(player, packet);
		}

		super.channelRead(ctx, obj);
	}

}
