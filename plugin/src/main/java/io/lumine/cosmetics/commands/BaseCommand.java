package io.lumine.cosmetics.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.utils.commands.Command;

public class BaseCommand extends Command<MCCosmeticsPlugin> {

    public BaseCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_BASE;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
