package io.lumine.cosmetics;

import io.lumine.cosmetics.managers.back.BackManager;
import org.bukkit.Bukkit;

import io.lumine.cosmetics.api.MCCosmetics;
import io.lumine.cosmetics.commands.BaseCommand;
import io.lumine.cosmetics.commands.admin.AdminCommand;
import io.lumine.cosmetics.compat.CompatibilityManager;
import io.lumine.cosmetics.config.Configuration;
import io.lumine.cosmetics.logging.MCLogger;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.managers.hats.HatManager;
import io.lumine.cosmetics.menus.MenuManager;
import io.lumine.cosmetics.metrics.bStats;
import io.lumine.cosmetics.nms.VolatileCodeDisabled;
import io.lumine.cosmetics.nms.VolatileCodeHandler;
import io.lumine.cosmetics.players.ProfileManager;
import io.lumine.utils.chat.ColorString;
import io.lumine.utils.logging.ConsoleColor;
import io.lumine.utils.plugin.LuminePlugin;
import io.lumine.utils.version.ServerVersion;
import lombok.Getter;

public class MCCosmeticsPlugin extends LuminePlugin {

    private static MCCosmeticsPlugin plugin;

    @Getter private Configuration configuration;
    @Getter private CompatibilityManager compatibility;
    
    @Getter private ProfileManager profiles;
    @Getter private MenuManager menuManager;
    
    @Getter private BaseCommand baseCommand;
    @Getter private AdminCommand adminCommand;
    
    @Getter private HatManager hatManager;
    @Getter private BackManager backManager;

    private VolatileCodeHandler volatileCodeHandler;

    /*
     * Other Shit
     */ 
    @Getter private Boolean isUpdateAvailable = false;
    
    /** 
     * Bukkit Loader
     * @author Ashijin
     * @exclude
     */
    @Override
    public void load() {
        plugin = this;
    }  
    
    /**
     * Bukkit Initializer
     * @author Ashijin
     * @exclude
     */
    @Override 
    public void enable() {
        MCLogger.log("Loading {0} for {1} {2}...", getDescription().getName(), ServerVersion.isPaper() ? "Paper" : "Spigot", ServerVersion.get().toString());

        if(ServerVersion.isPaper()) {
            MCLogger.log("The server is running PaperSpigot; enabled PaperSpigot exclusive functionality");
        } else {
            MCLogger.log("The server is running Spigot; disabled PaperSpigot exclusive functionality");
        }

        /*
         * Plugin Components
         */
        this.bind(configuration = new Configuration(this));

        volatileCodeHandler = getVolatileCodeHandler();
        compatibility = new CompatibilityManager(this);
        
        profiles = new ProfileManager(this);
        
        getConfiguration().load(this);
        MCLogger.log("MCCosmetics configuration file loaded successfully.");
        
        /*
         * Commands 
         */
        this.baseCommand = new BaseCommand(this);
        this.adminCommand = new AdminCommand(this);

        this.registerCommand("cosmetics", baseCommand);
        this.registerCommand("mccosmetics", adminCommand);

        if(configuration.isAllowingMetrics())  {
            new bStats(this);
        }

        MCLogger.log("" + ConsoleColor.GREEN + ConsoleColor.CHECK_MARK + " MCCosmetics" + (p() ? " Premium" : "") + " v" + getVersion() +  " (build "+ getBuildNumber() +") has been successfully loaded!" + ConsoleColor.RESET);
    }

    @Override
    /**
     * Bukkit Shutdown
     * @author Ashijin
     * @exclude
     */
    public void disable() {
        MCLogger.log("Disabling MCCosmetics...");

        //configuration.save();

        MCLogger.log("All active settings have been saved.");
         
        configuration.unload();
        compatibility.terminate(); 
    }
    
    /**
     * @return MCCosmetics Returns the active MCCosmetics instance.
     */
    public static MCCosmeticsPlugin inst()    {
        return plugin; 
    } 
    
    /** 
     * @exclude
     */
    private static boolean p = false;

    /**
     * @exclude 
     */
    public static final boolean p() {
        return p;
    }

    /**
     * Grabs the active VolatileCodeHandler for the current version of Minecraft
     * @return {@link VolatileCodeHandler}
     */
    public VolatileCodeHandler getVolatileCodeHandler()  {
        if(this.volatileCodeHandler != null) return this.volatileCodeHandler;
        
        VolatileCodeHandler VCH = new VolatileCodeDisabled();
                
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (version.equals("craftbukkit")) {
            version = "pre";
        }   
        try {
            final Class<?> clazz = Class.forName("io.lumine.cosmetics.nms.VolatileCodeEnabled_" + version);
            if (VolatileCodeHandler.class.isAssignableFrom(clazz)) {
                VCH = (VolatileCodeHandler) clazz.getConstructor(MCCosmetics.class).newInstance(this);
            }
        } catch (final ClassNotFoundException e) {  
            MCLogger.error(ColorString.get("&6--====|||| &c&lMCCosmetics &6||||====--"));
            MCLogger.error("This version of MCCosmetics is not fully compatible with your version of Bukkit.");
            MCLogger.error("Some features may be limited or disabled until you use a compatible version.");  
        } catch (final Exception e) {
            throw new RuntimeException("Unknown exception loading version handler. Volatile code has been disabled.", e);
        }
        this.volatileCodeHandler = VCH;
        return VCH;
    }
 
    /**
     * Returns the plugin version
     * @return int
     */
    public String getVersion()  {
        return this.getDescription().getVersion().split("-")[0];
    }

    /**
     * Returns the development build version
     * @return int
     */
    public String getBuildNumber()  {
        final String[] split = this.getDescription().getVersion().split("-");
        if(split.length == 2)   {
            return split[1];
        } else if(split.length == 3)    {
            return split[2];
        } else  {
            return "????";
        }
    }
}
