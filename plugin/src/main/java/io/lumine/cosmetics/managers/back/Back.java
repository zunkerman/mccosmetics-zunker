package io.lumine.cosmetics.managers.back;

import com.google.common.collect.Lists;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.commands.CommandHelper;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
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

public class Back extends Cosmetic {

	private static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Material", Material.EMERALD);
	private static final IntProp MODEL = Property.Int(Scope.NONE, "Model");
	private static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
	private static final LangListProp DESCRIPTION = Property.LangList(Scope.NONE, "Description");
	private static final StringProp TEXTURE = Property.String(Scope.NONE, "Texture");

	private final File file;
	private final String key;
	@Getter
	private List<String> sources = Lists.newArrayList();

	private Material material;
	private int model;
	@Getter private String display;
	private List<String> description;

	@Getter private ItemStack backItem;

	public Back(File file, String key) {
		super(CosmeticType.type(Back.class), key);

		this.file = file;
		this.key = key.toUpperCase();
		this.material = MATERIAL.fget(file,this);
		this.model = MODEL.fget(file,this);
		this.display = DISPLAY.fget(file,this);
		this.description = DESCRIPTION.fget(file,this);

		if(material == Material.PLAYER_HEAD) {
			this.backItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.skullTexture(TEXTURE.get(this))
					.build();
		} else {
			this.backItem = ItemFactory.of(this.material)
					.name(Text.colorize(this.getDisplay()))
					.model(model)
					.hideAttributes()
					.lore(description)
					.build();
		}
	}

	@Override
	public String getPropertyNode() {
		return "Backs." + this.key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return IconBuilder.<CosmeticProfile>create()
				.name(Text.colorize(this.getDisplay()))
				.item(this.backItem)
				.hideFlags()
				.lore(prof -> {
					List<String> desc = Lists.newArrayList(description);
					if(!prof.has(this)) {
						desc.add("");
						desc.add(Text.colorizeLegacy("<red>Not Unlocked"));
					}
					return desc;
				})
				.click((prof,p) -> {
					if(prof.getPlayer().isOp() || prof.has(this)) {
						prof.equip(this);
						CommandHelper.sendSuccess(p, "Set your hat to " + getDisplay());
						p.closeInventory();
					} else {
						CommandHelper.sendError(p, "You haven't unlocked that hat yet!");
					}
				}).build();
	}

}
