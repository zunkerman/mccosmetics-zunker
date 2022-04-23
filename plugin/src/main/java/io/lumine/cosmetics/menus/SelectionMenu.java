package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.managers.MCCosmeticsManager;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class SelectionMenu extends CosmeticMenu<Profile> {

    private final MCCosmeticsManager cosmeticManager;
    
    public SelectionMenu(MCCosmeticsPlugin core, MenuManager manager, MCCosmeticsManager cmanager, String type) {
        super(core, manager, new MenuProp(core, "menus/selection_" + type.toLowerCase(), "Menu", null));
        
        this.cosmeticManager = cmanager;
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {
        
        builder.getIcon("BUTTON_BACK").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                getMenuManager().getCustomizeMenu().open(player, profile);
            });
        });
        
        builder.getIcon("BUTTON_REMOVE").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                profile.unequip(cosmeticManager.getCosmeticClass());
            });
        });
        
        return builder;
    }

}
