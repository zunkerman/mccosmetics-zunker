package io.lumine.cosmetics.nms.v1_18_R2.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import org.bukkit.entity.Player;

public class VolatileChannelHandler extends ChannelDuplexHandler {

	@Getter private final Player player;

	public VolatileChannelHandler(Player player) {
		this.player = player;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {



		super.write(ctx, packet, promise);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {

		super.channelRead(ctx, packet);
	}
}
