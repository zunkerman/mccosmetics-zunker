package io.lumine.cosmetics.managers.modelengine;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.OrientProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import io.lumine.utils.serialize.Orient;
import lombok.Getter;

import java.io.File;

public class MEGAccessory extends AbstractCosmetic {

	private static final StringProp MODEL = Property.String(Scope.NONE, "ModelId");
	private static final StringProp STATE = Property.String(Scope.NONE, "State");
	private static final OrientProp OFFSET = Property.Orient(Scope.NONE, "Offset");

	@Getter
	private final String modelId;
	@Getter
	private final Orient offset;

	public MEGAccessory(MEGManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(MEGAccessory.class), key);

		modelId = MODEL.fget(file, this);
		offset = OFFSET.fget(file, this);

	}

	@Override
	public String getPropertyNode() {
		return key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("model engine accessory");
	}
}
