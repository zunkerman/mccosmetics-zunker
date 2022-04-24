package io.lumine.cosmetics.commands;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.cosmetics.managers.gestures.Gesture;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmotesCommand extends Command<MCCosmeticsPlugin> {
    public EmotesCommand(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {

        if(strings.length < 1)
            return false;

        Optional<Profile> profile = plugin.getProfiles().getProfile(((Player)commandSender).getName());

        var cosmetic = getPlugin().getGestureManager().getCosmetic(strings[0]);

        profile.ifPresent(value -> cosmetic.ifPresent(gesture -> gesture.equip(value)));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if(strings.length == 1) {
            List<String> emotes = new ArrayList<>();
            for(Gesture gesture : getPlugin().getGestureManager().getAllCosmetics()){
                emotes.add(gesture.getKey());

            }
            return StringUtil.copyPartialMatches(strings[0], emotes, new ArrayList<>());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_EMOTES;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
