package io.lumine.cosmetics.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.Permissions;
import io.lumine.cosmetics.menus.CosmeticMenu;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.*;
import io.lumine.utils.items.ItemFactory;
import io.lumine.utils.menu.Icon;
import io.lumine.utils.menu.IconBuilder;
import io.lumine.utils.text.Text;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCosmetic extends Cosmetic {

	protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Material", Material.EMERALD);
	protected static final IntProp MODEL = Property.Int(Scope.NONE, "Model");
	protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
	protected static final LangListProp DESCRIPTION = Property.LangList(Scope.NONE, "Description");
    protected static final StringProp COLOR = Property.String(Scope.NONE, "Color");
	protected static final StringProp TEXTURE = Property.String(Scope.NONE, "SkullTexture");
	
    protected static final BooleanProp COLORABLE = Property.Boolean(Scope.NONE, "Colorable", false);

    protected static final NodeListProp VARIANTS = Property.NodeList(Scope.NONE, "Variants");

	protected final File file;
	protected final String key;
	@Getter protected final List<String> sources = Lists.newArrayList();

	// Menu Item
	protected Material material;
	protected int model;
	@Getter protected String display;
	protected List<String> description;
    protected String color;
    
	@Getter protected ItemStack menuItem;
	
	@Getter protected boolean colorable;
	protected Map<String,CosmeticVariant> variants = Maps.newConcurrentMap();

	public AbstractCosmetic(MCCosmeticsManager manager, File file, String type, String key) {
		super(manager, type, key);

		this.file = file;
		this.key = key;
		this.material = MATERIAL.fget(file,this);
		this.model = MODEL.fget(file,this);
		this.display = DISPLAY.fget(file,this);
		this.description = DESCRIPTION.fget(file,this);
        this.color = COLOR.fget(file,this);
		this.colorable = COLORABLE.fget(file,this);

		for(String node : VARIANTS.fget(file,this)) {
		    var variant = new AbstractCosmeticVariant(this,file,node);
		    variants.put(variant.getKey(), variant);
		}
		
		if(material == Material.PLAYER_HEAD) {
			this.menuItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.skullTexture(TEXTURE.get(this))
					.build();
		} else {
			this.menuItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.build();
		}
	}

	protected Icon<CosmeticProfile> buildIcon(String type) {
		return IconBuilder.<CosmeticProfile>create()
				.name(Text.colorize(this.getDisplay()))
				.item(this.menuItem)
				.hideFlags()
				.lore(prof -> {
					List<String> desc = Lists.newArrayList(description);
					if(!prof.has(this)) {
						desc.add("");
						desc.add(Text.colorizeLegacy("<red>Not Unlocked"));
					}
					return desc;
				})
				.click((prof,player) -> {
					if(prof.getPlayer().isOp() || prof.has(this)) {
					    CosmeticMenu.playMenuClick(player);
						prof.equip(this);
						CommandHelper.sendSuccess(player, "Set your " + type + " to " + getDisplay());
						player.closeInventory();
					} else {
						CommandHelper.sendError(player, "You haven't unlocked that cosmetic!");
					}
				}).build();
	}
	
	public String getPermission() {
	    return Permissions.cosmeticPermission(this);
	}
	
	public boolean hasVariants() {
	    return variants.isEmpty() == false;
	}
	
	public Optional<CosmeticVariant> getVariant(String id) {
	    return Optional.ofNullable(variants.getOrDefault(id, null));
	}

}
