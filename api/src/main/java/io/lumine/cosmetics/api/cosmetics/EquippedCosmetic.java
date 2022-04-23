package io.lumine.cosmetics.api.cosmetics;

import lombok.Data;

@Data
public class EquippedCosmetic {

    private final Cosmetic cosmetic;
    private final CosmeticVariant variant;
    
    public EquippedCosmetic(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
        this.variant = null;
    }    
    
    public EquippedCosmetic(CosmeticVariant variant) {
        this.cosmetic = variant.getCosmetic();
        this.variant = variant;
    }
}
