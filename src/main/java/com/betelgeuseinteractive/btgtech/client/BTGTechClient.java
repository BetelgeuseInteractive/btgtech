package com.betelgeuseinteractive.btgtech.client;

import com.betelgeuseinteractive.btgtech.BTGTech;
import com.betelgeuseinteractive.btgtech.datagen.DataGenerators;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Client entry point of BTG Tech, constructed by NeoForge only on the physical client.
 *
 * <p>It lives in a {@code client} package so that client-only code stays isolated from common code. Each feature with
 * client code is bootstrapped from here with one call, in the same order as in {@link BTGTech}. Data generation is
 * registered here as well, because its providers run only in the client data run.
 */
@Mod(value = BTGTech.MOD_ID, dist = Dist.CLIENT)
public final class BTGTechClient {
    public BTGTechClient(IEventBus modBus) {
        DataGenerators.register(modBus);
    }
}
