package io.lumine.cosmetics.commands.admin;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.MCCosmetics;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;

public class SprayCommand extends Command<MCCosmeticsPlugin> {

    public SprayCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        var player = (Player) sender;
        var sprayName = args[0];
        var maybeSpray = getPlugin().getSprayManager().getCosmetic(sprayName);

        if(maybeSpray.isEmpty()) {
            CommandHelper.sendSuccess(sender, "Spray not found.");
            return true;
        }
        
        var spray = maybeSpray.get();

        CommandHelper.sendSuccess(sender, "Using spray "+sprayName+"...");
        getPlugin().getSprayManager().useSpray(player, spray);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_ADMIN;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return "spray";
    }
}