package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class CustomizeMenu extends CosmeticMenu<Profile> {

    public CustomizeMenu(MCCosmeticsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/customize", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        builder.getIcon("BUTTON_HATS").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                player.performCommand("hats");
            });
        });
        
        return builder;
    }

}
