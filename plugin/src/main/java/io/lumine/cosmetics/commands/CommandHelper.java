package io.lumine.cosmetics.commands;

import io.lumine.utils.text.Text;
import net.kyori.adventure.text.Component;

import org.bukkit.command.CommandSender;

public class CommandHelper {

    public static final Component COMMAND_HEADER = Text.parse("<yellow><strikethrough>------------<gold>=====</strikethrough> <bold><gradient:#42a8e8:#EB4ACC>MCCosmetics</gradient></bold> <strikethrough><gold>=====<yellow>------------</strikethrough>");
    public static final Component COMMAND_FOOTER = Text.parse("<yellow><strikethrough>--------------------------------------</strikethrough>");
    public static final Component COMMAND_PREFIX = Text.parse("<bold><white>[<gradient:#42a8e8:#EB4ACC>MCCosmetics</gradient><white>]</bold> ");
    
    public static void sendCommandHeader(CommandSender sender) {
        Text.sendMessage(sender, COMMAND_HEADER);
    }
    
    public static void sendCommandFooter(CommandSender sender)    {
        Text.sendMessage(sender, COMMAND_FOOTER);
    }
    
    public static void sendSuccess(CommandSender sender, String message)    {
        Text.sendMessage(sender, COMMAND_PREFIX.append(Text.parse("<green>" + message)));
    }
    
    public static void sendError(CommandSender sender, String message)  {
        Text.sendMessage(sender, COMMAND_PREFIX.append(Text.parse("<red>" + message)));
    }
    

    public static void sendCommandMessage(CommandSender player, String[]... args) {
        sendCommandHeader(player);
        player.sendMessage(" ");
        for(String[] s : args) {
            for(String ss : s) {
                Text.sendMessage(player, ss);
            }
        }
        player.sendMessage(" ");
        sendCommandFooter(player);
    }
    
    public static void sendCommandMessage(CommandSender player, Component[]... args) {
        sendCommandHeader(player);
        player.sendMessage(" ");
        for(Component[] s : args) {
            for(Component ss : s) {
                Text.sendMessage(player, ss);
            }
        }
        player.sendMessage(" ");
        sendCommandFooter(player);
    }
    
    public static void sendPlayerSuccess(CommandSender sender, String message) {
        sender.sendMessage(Text.colorizeLegacy("<white>[<gradient:#42a8e8:#EB4ACC><bold>MCCosmetics</bold></gradient><white>] <green>" + message));
    }

    public static void sendPlayerError(CommandSender sender, String message) {
        sender.sendMessage(Text.colorizeLegacy("<white>[<gradient:#42a8e8:#EB4ACC><bold>MCCosmetics</bold></gradient><white>] <red>" + message));
    }

}
