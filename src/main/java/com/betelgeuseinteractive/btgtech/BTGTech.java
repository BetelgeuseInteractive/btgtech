package com.betelgeuseinteractive.btgtech;

import com.betelgeuseinteractive.btgtech.core.CoreFeature;
import com.betelgeuseinteractive.btgtech.material.MaterialFeature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Common entry point of BTG Tech, constructed by NeoForge on both physical sides.
 *
 * <p>Each feature is bootstrapped from here with one call, in the order defined in {@code docs/architecture/core.md}.
 */
@Mod(BTGTech.MOD_ID)
public final class BTGTech {
    public static final String MOD_ID = "btgtech";

    public BTGTech(IEventBus modBus) {
        CoreFeature.register(modBus);
        MaterialFeature.register(modBus);
    }
}
