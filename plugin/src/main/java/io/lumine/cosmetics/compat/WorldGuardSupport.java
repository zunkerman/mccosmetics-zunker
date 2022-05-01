package io.lumine.cosmetics.compat;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import io.lumine.cosmetics.MCCosmeticsPlugin;

public class WorldGuardSupport {

    public static StateFlag SPRAY_FLAG;

    public WorldGuardSupport(MCCosmeticsPlugin plugin) {

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try{
            StateFlag flag = new StateFlag("mcc-spray-flag", true);
            registry.register(flag);
            SPRAY_FLAG = flag;
        } catch(FlagConflictException e){
            Flag<?> existing = registry.get("mcc-spray-flag");
            if(existing instanceof StateFlag){
                SPRAY_FLAG = (StateFlag) existing;
            }
        }

    }
}
