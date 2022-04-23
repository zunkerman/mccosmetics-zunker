package io.lumine.cosmetics.constants;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;

public class Permissions {

    public static final String cosmeticPermission(Cosmetic cosmetic) {
        return "mccosmetics.cosmetic." + cosmetic.getKey().toLowerCase();
    }
    
    public static final String COMMAND_BASE   = "mccosmetics.command.base";

    public static final String COMMAND_SPRAY = "mccosmetics.command.spray";
    
    public static final String COMMAND_ADMIN  = "mccosmetics.command.admin";
    public static final String COMMAND_RELOAD = "mccosmetics.command.admin.reload";
}
