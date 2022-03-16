package io.lumine.cosmetics.managers.pets;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import lombok.Getter;

import java.io.File;

public class Pet extends AbstractCosmetic {

	private static final StringProp PET = Property.String(Scope.NONE, "Pet");

	@Getter
	private final String petId;

	public Pet(File file, String key) {
		super(file, CosmeticType.type(Pet.class), key);

		petId = PET.fget(file, this);
	}

	@Override
	public String getPropertyNode() {
		return "Pet." + key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("pet");
	}
}
