package io.lumine.cosmetics.players;

import java.util.Optional;

import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.CosmeticVariant;
import io.lumine.cosmetics.api.cosmetics.EquippedCosmetic;
import lombok.Data;

@Data
public class ProfileCosmeticData {

    private String type;
    private String id;
    private String variant;
    
    public ProfileCosmeticData(Cosmetic cosmetic) {
        this.type = cosmetic.getType();
        this.id = cosmetic.getKey();
        this.variant = "default";
    }

    public ProfileCosmeticData(CosmeticVariant variant) {
        this.type = variant.getCosmetic().getType();
        this.id = variant.getCosmetic().getKey();
        this.variant = variant.getKey();
    }
    
    public Optional<EquippedCosmetic> toEquippedCosmetic() {
        var maybeManager = MCCosmeticsPlugin.inst().getCosmetics().getManager(type);
        if(maybeManager.isEmpty()) {
            return Optional.empty();
        }
        
        var maybeCosmetic = maybeManager.get().getCosmetic(id);
        if(maybeCosmetic.isEmpty()) {
            return Optional.empty();
        }
        var cosmetic = (Cosmetic) maybeCosmetic.get();
        
        if(cosmetic.hasVariants() && this.variant != null) {
            var maybeVariant = cosmetic.getVariant(this.variant);
            
            if(maybeVariant.isPresent()) {
                return Optional.of(new EquippedCosmetic(maybeVariant.get()));
            }
        };
        return Optional.empty();
    }
     
}
