package io.lumine.cosmetics.managers.gestures;

import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.config.Scope;
import io.lumine.cosmetics.constants.CosmeticType;
import io.lumine.cosmetics.managers.AbstractCosmetic;
import io.lumine.utils.config.properties.Property;
import io.lumine.utils.config.properties.types.BooleanProp;
import io.lumine.utils.config.properties.types.EnumProp;
import io.lumine.utils.config.properties.types.StringProp;
import io.lumine.utils.menu.Icon;
import lombok.Getter;

import java.io.File;

public class Gesture extends AbstractCosmetic {


	private static final StringProp DEFAULT = Property.String(Scope.NONE, "Gesture");
	private static final StringProp SLIM = Property.String(Scope.NONE, "Slim");
	private static final BooleanProp CAN_MOVE = Property.Boolean(Scope.NONE, "CanMove", false);
	private static final BooleanProp CAN_LOOK = Property.Boolean(Scope.NONE, "CanLook", false);
	private static final BooleanProp FORCE_LOOP = Property.Boolean(Scope.NONE, "CanLook", false);
	private static final EnumProp<QuitControl> QUIT_CONTROL = Property.Enum(Scope.NONE, QuitControl.class,"QuitControl", QuitControl.SNEAK);

	@Getter private final String defaultGesture;
	@Getter private final String slimGesture;
	@Getter private final boolean canMove;
	@Getter private final boolean canLook;
	@Getter private final boolean forceLoop;
	@Getter private final QuitControl quitControl;

	public Gesture(GestureManager manager, File file, String key) {
		super(manager, file, CosmeticType.type(Gesture.class), key);

		defaultGesture = DEFAULT.fget(file, this);

		String temp = SLIM.fget(file, this);
		if(temp == null || temp.isEmpty())
			temp = defaultGesture;
		slimGesture = temp;

		canMove = CAN_MOVE.fget(file, this);
		canLook = CAN_LOOK.fget(file, this);
		forceLoop = FORCE_LOOP.fget(file, this);
		quitControl = QUIT_CONTROL.fget(file, this);
	}

	@Override
	public String getPropertyNode() {
		return this.key;
	}

	@Override
	public Icon<CosmeticProfile> getIcon() {
		return buildIcon("gesture");
	}

}
