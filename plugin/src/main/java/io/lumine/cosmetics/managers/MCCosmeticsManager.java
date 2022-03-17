package io.lumine.cosmetics.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.players.Profile;
import io.lumine.cosmetics.players.ProfileManager;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.NodeListProp;
import io.lumine.utils.files.Files;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.ReloadableModule;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class MCCosmeticsManager<T extends Cosmetic> extends ReloadableModule<MCCosmeticsPlugin> {

    protected final NodeListProp KEYS = Property.NodeList(Scope.NONE, "");
    protected final Map<String, T> cosmetics = Maps.newConcurrentMap();
    protected final Class<T> tClass;

    public MCCosmeticsManager(MCCosmeticsPlugin plugin, Class<T> tClass) {
        super(plugin,false); 
        this.tClass = tClass;
        
        load(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        final Collection<File> files = Lists.newArrayList();
        final String type = CosmeticType.folder(tClass);
        for(var packFolder : plugin.getConfiguration().getPackFolders()) {
            final File confFolder = new File(packFolder.getAbsolutePath() + System.getProperty("file.separator") + type);
            if(confFolder.exists() && confFolder.isDirectory()) {
                files.addAll(Files.getAllYaml(confFolder.getAbsolutePath()));
            }
        }

        for(var file : files) {
            for(var node : KEYS.fget(file)) {
                cosmetics.put(node.toUpperCase(), build(file, node));
            }
        }

        plugin.getCosmetics().registerCosmeticManager(CosmeticType.type(tClass), this);
        Log.info("Loaded " + cosmetics.size() + " " + type + ".");
    }

    @Override
    public void unload() {
        cosmetics.clear();
    }

    public abstract T build(File file, String node);

    public Collection<T> getAllCosmetics() {
        return cosmetics.values();
    }

    public Optional<T> getCosmetic(String key) {
        return Optional.ofNullable(cosmetics.getOrDefault(key, null));
    }

    public void equip(Player player) {
        equip(getProfiles().getProfile(player));
    }

    public abstract void equip(Profile profile);

    protected ProfileManager getProfiles() {
        return plugin.getProfiles();
    }

}
