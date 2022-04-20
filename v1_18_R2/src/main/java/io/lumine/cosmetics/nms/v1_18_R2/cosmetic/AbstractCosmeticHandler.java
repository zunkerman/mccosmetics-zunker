package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractCosmeticHandler {

	public abstract void read(Player sender, Packet<?> packet);

	public abstract List<Packet<?>> write(Player receiver, Packet<?> packet);


}
