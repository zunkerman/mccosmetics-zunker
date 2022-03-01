package io.lumine.cosmetics.menus;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MCCosmeticsPlugin> {

    @Getter private CustomizeMenu customizeMenu;

    //@Getter private ColorsMenu colorsMenu;
    //@Getter private ChatColorsMenu chatColorsMenu;
    //@Getter private ChatShoutColorsMenu chatShoutColorsMenu;
    //@Getter private GadgetsMenu gadgetsMenu;
    //@Getter private HatsMenu hatsMenu;
    //@Getter private NameColorsMenu nameColorsMenu;
    //@Getter private PetsMenu petsMenu;
    //@Getter private TagsMenu chatTagsMenu;
    //@Getter private SpeedBoostersMenu speedBoostersMenu;
    //@Getter private SpraysMenu spraysMenu;
    //@Getter private TitlesMenu titlesMenu;

    private boolean firstLoad = true;
    
    public MenuManager(MCCosmeticsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MCCosmeticsPlugin plugin) {
        if(customizeMenu == null) {
            customizeMenu = new CustomizeMenu(plugin,this);

            //colorsMenu = new ColorsMenu(plugin,this);
            //chatColorsMenu = new ChatColorsMenu(plugin,this);
            //chatShoutColorsMenu = new ChatShoutColorsMenu(plugin,this);
            //hatsMenu = new HatsMenu(plugin,this);
            //chatTagsMenu = new TagsMenu(plugin,this);
            //nameColorsMenu = new NameColorsMenu(plugin,this);
            //petsMenu = new PetsMenu(plugin,this);
            //gadgetsMenu = new GadgetsMenu(plugin,this);
            //speedBoostersMenu = new SpeedBoostersMenu(plugin,this);
            //spraysMenu = new SpraysMenu(plugin,this);
            //titlesMenu = new TitlesMenu(plugin,this);

            firstLoad = false;
        }
        
        customizeMenu.reload();

        //colorsMenu.reload();
        //chatColorsMenu.reload();
        //chatShoutColorsMenu.reload();
        //hatsMenu.reload();
        //gadgetsMenu.reload();
        //nameColorsMenu.reload();
        //petsMenu.reload();
        //chatTagsMenu.reload();
        //speedBoostersMenu.reload();
        //spraysMenu.reload();
        //titlesMenu.reload();
    }

    @Override
    public void unload() {}

}
