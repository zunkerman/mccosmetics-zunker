package io.lumine.cosmetics.nms;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.nms.cosmetic.VolatileBackHelper;
import io.lumine.cosmetics.nms.cosmetic.VolatileHatHelper;
import io.lumine.cosmetics.nms.v1_18_R1.cosmetic.VolatileBackImpl;
import io.lumine.cosmetics.nms.v1_18_R1.network.VolatileChannelHandler;
import io.lumine.cosmetics.nms.v1_18_R1.cosmetic.VolatileHatImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VolatileCodeEnabled_v1_18_R1 implements VolatileCodeHandler {

    @Getter private final MCCosmeticsPlugin plugin;
    @Getter private final VolatileHatHelper hatHelper;
    @Getter private final VolatileBackHelper backHelper;
    
    public VolatileCodeEnabled_v1_18_R1(MCCosmeticsPlugin plugin) {
        this.plugin = plugin;
        this.hatHelper = new VolatileHatImpl(plugin);
        this.backHelper = new VolatileBackImpl(plugin);
    }

    @Override
    public void injectPlayer(Player player) {
        ServerPlayer ply = ((CraftPlayer) player).getHandle();
        VolatileChannelHandler cdh = new VolatileChannelHandler(player);

        ChannelPipeline pipeline = ply.connection.getConnection().channel.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof Connection) {
                pipeline.addBefore(name, "mc_cosmetics_packet_handler", cdh);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.getConnection().channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("mc_cosmetics_packet_handler");
            return null;
        });
    }

}
