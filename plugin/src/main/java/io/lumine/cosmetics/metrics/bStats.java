package io.lumine.cosmetics.metrics;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.logging.MCLogger;
import org.bstats.bukkit.Metrics;

public class bStats {

	public bStats(MCCosmeticsPlugin plugin)	{
		try {
        	Metrics metrics = new Metrics(plugin);

            final String devBuilds = plugin.getDescription().getVersion().contains("SNAPSHOT") ? "Yes" : "No";
            final String preBuilds = MCCosmeticsPlugin.isPremium() ? "Yes" : "No";

            metrics.addCustomChart(new Metrics.SimplePie("premium", () -> preBuilds));
            metrics.addCustomChart(new Metrics.SimplePie("devbuilds", () -> devBuilds));
            
            MCLogger.log("Started up bStats Metrics");
        } catch (Exception e) {
            MCLogger.error("Metrics: Failed to enable bStats Metrics stats.");
        }
	}
}
