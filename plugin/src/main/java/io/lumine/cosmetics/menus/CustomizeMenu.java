package io.lumine.cosmetics.menus;

import java.util.List;

import com.google.common.collect.Lists;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class CustomizeMenu extends CosmeticMenu<Profile> {

    public CustomizeMenu(MCCosmeticsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/customize", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        for(var entry : getPlugin().getCosmetics().getCosmeticManagers().entrySet()) {
            final var type = entry.getKey();
            final var manager = entry.getValue();
            
            builder.getIcon("BUTTON_" + type).ifPresent(icon -> {
                icon.getBuilder().click((profile,player) -> {
                    // TODO: sorting
                    List<Cosmetic> cosmetics = Lists.newArrayList(manager.getAllCosmetics());
                    getMenuManager().getSelectionMenu().open(player, profile, cosmetics);
                });
            });
        }
        
        return builder;
    }

}
