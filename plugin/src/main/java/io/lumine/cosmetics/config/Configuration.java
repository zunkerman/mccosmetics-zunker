package io.lumine.cosmetics.config;

import com.google.common.collect.Lists;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.config.properties.types.EnumProp;
import io.lumine.utils.config.properties.types.IntProp;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

import java.io.File;
import java.util.Collection;

public class Configuration extends ReloadableModule<MCCosmeticsPlugin> implements PropertyHolder {
    
    private static final IntProp CLOCK_INTERVAL = Property.Int(Scope.CONFIG, "Clock.Interval", 1);
    private static final EnumProp<StorageDriver> STORAGE_DRIVER = Property.Enum(Scope.CONFIG, StorageDriver.class, "Storage.Driver", StorageDriver.JSON); 
    
    @Getter private boolean allowingMetrics = true;
    
    public Configuration(MCCosmeticsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MCCosmeticsPlugin plugin) {
        
    }
  
    @Override
    public void unload() {}

    @Override
    public String getPropertyNode() {
        return "Configuration";
    }
    
    public Collection<File> getPackFolders() {
        final File packsFolder = new File(getPlugin().getDataFolder() + System.getProperty("file.separator") + "packs");
        final Collection<File> packs = Lists.newArrayList();
        
        if(!packsFolder.exists()) {
            packsFolder.mkdir();
        }
        if(packsFolder.exists() && packsFolder.isDirectory()) {
            for(var packFolder : packsFolder.listFiles()) {
                if(packFolder.isDirectory()) {
                    packs.add(packFolder);
                }
            }
        }
        return packs;
    }
    
    public int getClockInterval() {
        return CLOCK_INTERVAL.get(this);
    }
    
    public StorageDriver getStorageType() {
        return STORAGE_DRIVER.get(this);
    }
    
    
}
