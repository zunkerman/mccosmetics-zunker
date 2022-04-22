package io.lumine.cosmetics.managers.modelengine;

import com.ticxo.modelengine.api.model.ModeledEntity;
import com.ticxo.modelengine.api.model.base.BukkitPlayer;
import com.ticxo.modelengine.api.model.base.EntityData;
import com.ticxo.modelengine.api.util.math.Offset;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.logging.MCLogger;
import io.lumine.utils.serialize.Orient;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FakeEntity extends BukkitPlayer {

	private final Player player;
	private final Vector offset;
	private final double yaw;
	private final double pitch;
	private final ModelAnchor anchor;

	public FakeEntity(Player player, Orient orient, ModelAnchor anchor) {
		super(player);
		this.player = player;
		offset = orient.getLocus().toVector();
		yaw = Math.toRadians(orient.getDirection().getYaw());
		pitch = Math.toRadians(orient.getDirection().getPitch());
		this.anchor = anchor;
	}

	@Override
	public void sendDespawnPacket(ModeledEntity modeledEntity) {

	}

	@Override
	public void sendSpawnPacket(ModeledEntity modeledEntity) {

	}

	@Override
	public void saveModelInfo(ModeledEntity model) {

	}

	@Override
	public EntityData loadModelInfo() {
		return null;
	}

	@Override
	public Location getLocation() {
		Vector offset;
		if (anchor == ModelAnchor.HEAD) {
			MCLogger.log("head");
			double pYaw = Math.toRadians(player.getLocation().getYaw());
			double pPitch = Math.toRadians(player.getLocation().getPitch());
			offset = Offset.rotateYaw(Offset.rotatePitch(this.offset.clone(), pitch + pPitch), yaw + pYaw);
		} else {
			double pYaw = getBodyYaw();
			offset = Offset.rotateYaw(this.offset.clone(), yaw + pYaw);
		}
		return player.getLocation().add(offset);
	}

	@Override
	public void setEntitySize(float width, float height, float eye) {

	}

	private double getBodyYaw() {
		Location location = player.getLocation();
		Vector nV = new Vector(location.getX() - getLastX(), 0, location.getZ() - getLastZ());
		if(nV.getX() != 0 || nV.getZ() != 0)
			MCCosmeticsPlugin.inst().getVolatileCodeHandler().setBodyYaw(player, location.getYaw());

		return Math.toRadians(MCCosmeticsPlugin.inst().getVolatileCodeHandler().getBodyYaw(player));
	}

}
