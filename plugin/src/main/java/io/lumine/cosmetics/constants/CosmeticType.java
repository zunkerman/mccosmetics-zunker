package io.lumine.cosmetics.constants;

import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.managers.back.Back;
import io.lumine.cosmetics.managers.hats.Hat;

import java.util.HashMap;
import java.util.Map;

public class CosmeticType {

    private static final Map<Class<? extends Cosmetic>, CosmeticConstant> constants;

    static {
        constants = new HashMap<>();
        constants.put(Hat.class, new CosmeticConstant("HAT", "hats"));
        constants.put(Back.class, new CosmeticConstant("BACK", "backs"));
    }

    public static CosmeticConstant get(Class<? extends Cosmetic> tClass) {
        return constants.get(tClass);
    }

    public static String type(Class<? extends Cosmetic> tClass) {
        return get(tClass).type();
    }

    public static String folder(Class<? extends Cosmetic> tClass) {
        return get(tClass).folder();
    }

    public record CosmeticConstant(String type, String folder) {}

}
