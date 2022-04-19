package io.lumine.cosmetics.managers.sprays;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.DoubleProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.files.Files;
import io.lumine.utils.logging.Log;
import io.lumine.utils.numbers.Numbers;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SprayManager extends MCCosmeticsManager<Spray> {

    private static final StringProp SPRAY_SOUND = Property.String(Scope.CONFIG, "", "");
    private static final DoubleProp SPRAY_SOUND_VOL = Property.Double(Scope.CONFIG, "", 1D);
    private static final DoubleProp SPRAY_SOUND_PI = Property.Double(Scope.CONFIG, "", 1D);
    
    private final AtomicInteger currentMapId = new AtomicInteger(Integer.MIN_VALUE);
    
    private final Map<String,SprayImage> images = Maps.newConcurrentMap();
    
    public SprayManager(MCCosmeticsPlugin plugin) {
        super(plugin, Spray.class);   
        
        load(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        loadSprayImages();
        
        super.load(plugin);
    }

    @Override
    public Spray build(File file, String node) {
        return new Spray(this, file, node);
    }

    @Override
    public void equip(CosmeticProfile profile) {}
    
    private void loadSprayImages() {
        images.clear();
        
        final Collection<File> files = Lists.newArrayList();
        final String type = CosmeticType.folder(tClass);
        for(var packFolder : plugin.getConfiguration().getPackFolders()) {
            final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
            if(confFolder.exists() && confFolder.isDirectory()) {
                files.addAll(Files.getAll(confFolder.getAbsolutePath(), Lists.newArrayList("png", "jpg")));
            }
        }
        
        for(var file : files) {
            var img = createSprayImage(file);
            images.put(img.getName(), img);
        }

        Log.info("Loaded " + images.size() + " spray images");
    }
    
    private SprayImage createSprayImage(File file) {
        return new SprayImage(this, file, getNextMapId());
    }

    private int getNextMapId() {
        return currentMapId.updateAndGet(id -> (id + 1));
    }
    
    public SprayImage getSprayImage(String name) {
        return images.getOrDefault(name, null);
    }
    
    public boolean useSpray(Player player) {
        return useSpray(player, null);
    }
    
    public boolean useSpray(Player player, Spray spray) {
        final Location location = player.getEyeLocation();
        
        RayTraceResult result = location.getWorld().rayTrace(location, location.getDirection(), 10, FluidCollisionMode.ALWAYS, false, 1, (entity) -> {
            return false;
        });
        
        if(result == null) {
            return false;
        }
            
        if(result.getHitEntity() != null && result.getHitEntity().getType() == EntityType.ITEM_FRAME) {
            return false;
        }
        
        final int rotation;
        if(result.getHitBlockFace() == BlockFace.UP || result.getHitBlockFace() == BlockFace.DOWN) {
            rotation = getRotation(player.getLocation().getYaw(), false) * 45;
        } else {
            rotation = 0;
        }
        
        Location framelocation = result.getHitBlock().getRelative(result.getHitBlockFace()).getLocation();
        spawnSpray(player, spray, framelocation, result.getHitBlockFace(), rotation);
        return true;
    }
    
    public void spawnSpray(Player player, Spray spray, Location location, BlockFace face, int rotation) {
        int eid = getPlugin().getVolatileCodeHandler().getSprayHelper().drawSpray(spray, location, face, rotation);
    }
    
    private int getRotation(float yaw, boolean allowDiagonals) {
        if(allowDiagonals) {
            return Numbers.floor(((Location.normalizeYaw(yaw) + 180) * 8 / 360) + 0.5F) % 8;
        } else {
            return Numbers.floor(((Location.normalizeYaw(yaw) + 180) * 4 / 360) + 0.5F) % 4;
        }
    }
}
