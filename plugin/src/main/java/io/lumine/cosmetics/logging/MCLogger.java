package io.lumine.cosmetics.logging;

import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.logging.ConsoleColor;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitEntityType;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.io.ConfigManager;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.spawning.random.RandomSpawner;

public class MCLogger {

    private static MCCosmeticsPlugin getPlugin() {
        return MCCosmeticsPlugin.inst();
    }
    
	public static void log(String message) {
	    getPlugin().getLogger().log(Level.INFO, message + ConsoleColor.RESET);
    }

	public static void log(String message, Object... params) {
	    getPlugin().getLogger().log(Level.INFO, message + ConsoleColor.RESET, params);
    }
	
	public static void log(Level level, String message) {
	    getPlugin().getLogger().log(level, message + ConsoleColor.RESET);
    }
	
    public static void log(Level level, String message, Object... params) {
        getPlugin().getLogger().log(level, message + ConsoleColor.RESET, params);
    }

    public static void error(String message) {
        log(Level.WARNING, message + ConsoleColor.RESET);
    }
    
    public static void error(String message, Object... params) {
        log(Level.WARNING, message + ConsoleColor.RESET, params);
    }
    
    public static void error(String message, Throwable ex) {
        log(Level.SEVERE, message + ConsoleColor.RESET);
        log(Level.SEVERE, "    " + ex.getMessage() + " (" + ex.getClass().getName() + ")");
        
        if(ConfigManager.debugLevel > 0)	{
        	ex.printStackTrace();
		}

        if(ConfigManager.errorLogging == true) {
            PrintWriter writer = null;
            try {
                File folder = new File(getPlugin().getDataFolder(), "Error Logs");
                if (!folder.exists()) folder.mkdir();
                writer = new PrintWriter(new File(folder, System.currentTimeMillis() + ".txt"));
                Throwable t = ex;
                while (t != null) {
                    t.printStackTrace(writer);
                    writer.println();
                    t = t.getCause();
                }
                error("This error has been saved in the Error Logs folder. Please report it on the MythicCraft forums or discord.");
                writer.println("MCCosmetics version: " + getPlugin().getDescription().getVersion());
                writer.println("Bukkit version: " + Bukkit.getServer().getVersion());
            } catch (Exception x) {
                error("ERROR HANDLING EXCEPTION");
                x.printStackTrace();
                ex.printStackTrace();
            } finally {
                if (writer != null) writer.close();
            }
        } else {
            ex.printStackTrace();
        }
    }
    
    public static void error(String message, Throwable ex, Object... params) {
        log(Level.SEVERE, message + ConsoleColor.RESET, params);
        log(Level.SEVERE, "    " + ex.getMessage() + " (" + ex.getClass().getName() + ")");
        
        if(ConfigManager.debugLevel > 0)    {
            ex.printStackTrace();
        }

        if(ConfigManager.errorLogging == true) {
            PrintWriter writer = null;
            try {
                File folder = new File(getPlugin().getDataFolder(), "Error Logs");
                if (!folder.exists()) folder.mkdir();
                writer = new PrintWriter(new File(folder, System.currentTimeMillis() + ".txt"));
                Throwable t = ex;
                while (t != null) {
                    t.printStackTrace(writer);
                    writer.println();
                    t = t.getCause();
                }
                error("This error has been saved in the Error Logs folder. Please report it on the MythicCraft forums or discord.");
                writer.println("MCCosmetics version: " + MythicMobs.inst().getDescription().getVersion());
                writer.println("Bukkit version: " + Bukkit.getServer().getVersion());
            } catch (Exception x) {
                error("ERROR HANDLING EXCEPTION");
                x.printStackTrace();
                ex.printStackTrace();
            } finally {
                if (writer != null) writer.close();
            }
        } else {
            ex.printStackTrace();
        }
    }
	
    private MCLogger() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
