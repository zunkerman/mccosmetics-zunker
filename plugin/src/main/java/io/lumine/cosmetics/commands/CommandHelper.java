package io.lumine.cosmetics.commands;

import io.lumine.utils.adventure.text.Component;
import io.lumine.utils.text.Text;
import org.bukkit.command.CommandSender;

public class CommandHelper {

    public static final Component COMMAND_HEADER = Text.parse("<yellow><strikethrough>------------<gold>=====</strikethrough> <bold><gradient:#dc0c1f:#ed858f>MCCosmetics</gradient></bold> <strikethrough><gold>=====<yellow>------------</strikethrough>");
    public static final Component COMMAND_FOOTER = Text.parse("<yellow><strikethrough>--------------------------------------</strikethrough>");
    public static final Component COMMAND_PREFIX = Text.parse("<bold><white>[<#dc0c1f>MCCosmetics<white>]</bold> ");
    
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
        sender.sendMessage(Text.colorizeLegacy("<white>[<#dc0c1f><bold>MCCosmetics<white></bold>] <green>" + message));
    }

    public static void sendPlayerError(CommandSender sender, String message) {
        sender.sendMessage(Text.colorizeLegacy("<white>[<#dc0c1f><bold>MCCosmetics<white></bold>] <#ea6d78>" + message));
    }

}
