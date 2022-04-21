package io.lumine.cosmetics.compat;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Optional;

public class CompatibilityManager extends ReloadableModule<MCCosmeticsPlugin> {

    @Getter private Optional<LumineCoreCompat> lumineCore = Optional.empty();
    @Getter private Optional<MCPetsCompat> mcpets = Optional.empty();
    
    public CompatibilityManager(MCCosmeticsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MCCosmeticsPlugin plugin) {
        if(Bukkit.getPluginManager().getPlugin("LumineCore") != null) {
            lumineCore = Optional.of(new LumineCoreCompat(plugin));
        }
        if(Bukkit.getPluginManager().getPlugin("MCPets") != null) {
            mcpets = Optional.of(new MCPetsCompat(plugin));
        }
    }
  
    @Override
    public void unload() {
        
    }

}
