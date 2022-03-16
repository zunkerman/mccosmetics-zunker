package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MCCosmeticsPlugin> {

    @Getter private CustomizeMenu customizeMenu;
    @Getter private SelectionMenu selectionMenu;

    public MenuManager(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        if(customizeMenu == null) {
            customizeMenu = new CustomizeMenu(plugin,this);
            selectionMenu = new SelectionMenu(plugin,this);
        }
        
        customizeMenu.reload();
        selectionMenu.reload();
    }

    @Override
    public void unload() {}

}
