package io.lumine.cosmetics.listeners;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {
	private MCCosmeticsPlugin plugin;
	public PlayerListeners(MCCosmeticsPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().injectPlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		MCCosmeticsPlugin.inst().getVolatileCodeHandler().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerGetDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		plugin.getGestureManager().stopGesture(plugin.getProfiles().getProfile(player));
	}
}
