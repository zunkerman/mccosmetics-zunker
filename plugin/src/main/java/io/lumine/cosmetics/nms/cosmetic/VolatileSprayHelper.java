package io.lumine.cosmetics.nms.cosmetic;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.sprays.Spray;
import io.lumine.cosmetics.players.Profile;

public interface VolatileSprayHelper {

    public int drawSpray(Spray spray, Location location, BlockFace face, int rotation);

}
